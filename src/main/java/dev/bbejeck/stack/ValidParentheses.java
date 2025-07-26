package dev.bbejeck.stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * User: Bill Bejeck
 * Date: 7/26/25
 * Time: 10:36 AM
 */
public class ValidParentheses {

    public boolean isValid(String s) {
        if (s.length() == 1) {
            return false;
        }
        Stack<Character> parens = new Stack();
        Map<Character, Character> openClose = new HashMap();

        char op = '(';
        char cp = ')';
        char ocb = '{';
        char ccb = '}';
        char ob = '[';
        char cb = ']';

        openClose.put(cp, op);
        openClose.put(ccb, ocb);
        openClose.put(cb, ob);

        for (char c : s.toCharArray()) {
            if (isOpen(c)) {
                parens.push(c);
            } else {
                if (parens.isEmpty() || !(openClose.get(c) == parens.pop())) {
                    return false;
                }
            }
        }
        return parens.isEmpty();
    }

    public boolean isOpen(char c) {
        return c == '(' || c == '{' || c == '[';
    }
}
