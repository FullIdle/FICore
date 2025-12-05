package me.fullidle.ficore.ficore.common.api.inventory.actions;

import java.util.function.Predicate;

public class HeadMatcher implements Predicate<String> {
    private final String head;

    public HeadMatcher(String head){
        this.head = head;
    }

    @Override
    public boolean test(String string) {
        return string.startsWith(head + ": ");
    }

    public String cropped(String string) {
        if (test(string)) return string.substring(head.length() + 2);
        throw new IllegalArgumentException("String does not match head");
    }
}
