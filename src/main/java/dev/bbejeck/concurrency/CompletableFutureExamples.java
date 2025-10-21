package dev.bbejeck.concurrency;

import javax.swing.*;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * User: Bill Bejeck
 * Date: 10/14/25
 * Time: 9:20 PM
 */
public class CompletableFutureExamples {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();
    private final Random random = new Random();

    public void exampleOne() {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> 5 * 5);
        cf1.thenApplyAsync(i -> 1 + 100)
                .thenApplyAsync(i -> i * 10)
                .thenApplyAsync(i -> i / 2)
                .thenAcceptAsync(i -> System.out.println("The answer is " + i))
                .thenRunAsync(() -> System.out.println("All done running the code"));
    }

    public void exampleTwo() {
      getNumberBetween(500)
                .thenComposeAsync(this::square)
                .thenAcceptAsync(i -> System.out.println("The answer is " + i))
                .thenRunAsync(() -> System.out.println("All done running the code"))
                .join();
    }

    public void exampleThree() {
        getNumberBetween(333)
                .applyToEitherAsync(square(5), i -> i * 10 )
                .thenAcceptAsync(i -> System.out.println("The answer should be 250 -> " + i))
                .thenRunAsync(() -> System.out.println("All done running the code"))
                .join();
    }

    public void exampleFour() {
        getNumberBetween(100)
                .thenCombineAsync(square(5), (i, j) -> i * j * 10 )
                .thenAcceptAsync(i -> System.out.println("The answer should really big -> " + i))
                .thenRunAsync(() -> System.out.println("All done running the code"))
                .join();
    }

    public void exampleFive() {
        getNumberBetween(100)
                .handle((i, ex) -> {
                    if (ex != null) {
                        System.out.println("Error occurred: " + ex.getMessage());
                        return null;
                    }
                    System.out.printf("No Error passing number through%n");
                    return i;
                })
                .thenCombineAsync(square(5), (i, j) -> i * j * 10 )
                .handle((i, ex) -> {
                    if (ex != null) {
                        System.out.println("Error occurred: " + ex.getMessage());
                        return null;
                    }
                    System.out.printf("No Error in this phase, going through%n");
                    return i;
                })
                .thenAcceptAsync(i -> System.out.println("The answer should really big -> " + i))
                .thenRunAsync(() -> System.out.println("All done running the code"))
                .join();
    }

    public void exampleSix() {
        CompletableFuture<Integer> cf1 = getRandomNumberWithDelay();
        CompletableFuture<Integer> cf2 = getRandomNumberWithDelay();
        CompletableFuture<Integer> cf3 = getRandomNumberWithDelay();
        List<CompletableFuture<Integer>> futures = List.of(cf1, cf2, cf3);
        CompletableFuture.allOf(cf1, cf2, cf3).join();
        futures.stream().map(CompletableFuture::join).forEach(System.out::println);
    }

    public void exampleSeven() {
        CompletableFuture<Integer> cf1 = getRandomNumberWithDelay();
        CompletableFuture<Integer> cf2 = getRandomNumberWithDelay();
        CompletableFuture<Integer> cf3 = getRandomNumberWithDelay();
        CompletableFuture.anyOf(cf1, cf2, cf3)
                .thenAcceptAsync(i -> System.out.println("The completed value is " + i))
                .join();
    }

    public CompletableFuture<Integer> getRandomNumberWithDelay() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(random.nextInt(3000));
            return random.nextInt(100);
        });
    }

    public CompletableFuture<Integer> getNumberBetween(int num) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(3000);
            return random.nextInt(num);
        });
    }

    CompletableFuture<Integer> square(int num) {
        return CompletableFuture.supplyAsync(() -> num * num);
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        CompletableFutureExamples examples = new CompletableFutureExamples();
        examples.exampleOne();
        examples.exampleTwo();
        examples.exampleThree();
        examples.exampleFour();
        examples.exampleFive();
        examples.exampleSix();
        examples.exampleSeven();
    }

    
}
