package dev.bbejeck.concurrency;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import io.vavr.collection.HashSet;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * User: Bill Bejeck
 * Date: 10/18/25
 * Time: 3:49 PM
 */
public class Crawler {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private final AtomicInteger concurrency = new AtomicInteger(0);
    private final ConcurrentLinkedQueue<CompletableFuture<Void>> queueFutures = new ConcurrentLinkedQueue<>();
    private final int maxConcurrency;
    private final java.util.Set<URI> urisSeen = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final String destination;

    public Crawler(int maxConcurrency, String destination) {
        this.maxConcurrency = maxConcurrency;
        this.destination = destination;
    }

    private void checkPermission() {
        CompletableFuture<Void> qf = queueFutures.poll();
        if (qf != null) {
            qf.complete(null);
        } else {
            concurrency.getAndDecrement();
        }
    }

    <V> CompletableFuture<V> getPermission(Supplier<CompletableFuture<V>> cFToRun) {
        int current = concurrency.get();
        if (current < maxConcurrency) {
            if(!concurrency.compareAndSet(current, current+1)) {
                getPermission(cFToRun);
            }
            return cFToRun.get().whenCompleteAsync((ignoreLeft, ignoreRight) -> checkPermission());
        }

        CompletableFuture<Void> cf = new CompletableFuture<>();
        queueFutures.offer(cf);
        return cf.thenComposeAsync(ignore -> cFToRun.get())
                .whenCompleteAsync((ignoreLeft, ignoreRight) -> checkPermission());
    }

    private HashSet<String> extractLinks(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("a[href]");
        return HashSet.ofAll(elements).map(element -> element.attr("abs:href"));
    }

    private Set<URI> parseURI(Set<String> strUris) {
        return
                strUris.flatMap(uriStr -> {
                    try {
                        URI uri = URI.create(uriStr);
                        return HashSet.of(new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), null, null));
                    } catch (Exception e) {
                        return HashSet.empty();
                    }
                })
                        .filter(uri -> Objects.equals(uri.getScheme(), "https"))
                        .filter(uri -> uri.getHost() != null && !uri.getHost().isBlank())
                        .filter(uri -> uri.getPath().endsWith(".html") || !uri.getPath().contains("."));
    }

    private CompletableFuture<Set<URI>> scrapeExternalLinks(URI uri) {
        HttpRequest req;
        try {
            req = HttpRequest.newBuilder(uri).GET().timeout(java.time.Duration.ofSeconds(2)).build();
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
        return httpClient
                .sendAsync(req, HttpResponse.BodyHandlers.ofString())
                .thenApplyAsync(res -> extractLinks(res.body()))
                .thenApplyAsync(this::parseURI);
    }

    CompletableFuture<List<URI>> crawlFrom(URI parent) {

        return getPermission(() -> scrapeExternalLinks(parent)
                .thenApplyAsync(children -> children.filter(uri -> !urisSeen.contains(uri)))
                .thenComposeAsync(
                        children -> {
                            Option<URI> finalPageOpt = children.find(child -> child.getAuthority().contains(destination));
                            if (finalPageOpt.isDefined()) {
                                return CompletableFuture.completedFuture(List.of(parent, finalPageOpt.get()));
                            }
                            CompletableFuture<List<URI>> successPath = anySuccessOf(children.map(this::crawlFrom));
                            return successPath.thenApplyAsync(thePath ->  {
                                thePath.addFirst(parent);
                                return thePath;
                            });
                        }
                ));
    }

    <T> CompletableFuture<T> anySuccessOf(Set<CompletableFuture<T>> cFs) {
        if (cFs.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("No futures passed in"));
        }
        AtomicInteger counter = new AtomicInteger(cFs.size());
        CompletableFuture<T> cfToReturn = new CompletableFuture<>();
        cFs.forEach(
                cf -> cf.whenCompleteAsync(
                        (success, error) ->  {
                            if (success != null) {
                                cfToReturn.complete(success);
                            } else {
                                if (counter.decrementAndGet() == 0) {
                                    cfToReturn.completeExceptionally(new CompletionException("All other futures failed.", error));
                                }
                            }
                        }
                ));
        return cfToReturn;
    }


    public static void main(String[] args) {
        CompletableFuture<List<URI>> childrenF = new Crawler(10, "java.com").crawlFrom(URI.create("https://reddit.com/r/java"));
        List<URI> children = childrenF.join();
        children.forEach(System.out::println);
    }
}
