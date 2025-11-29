package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// List helpers shared by models, providers, and services.
// Keeps defensive copies and filtering in one place.
public final class Lists {
    private Lists() {
    }

    // Always returns a new mutable list, never the caller's list.
    public static <T> List<T> copyOf(List<T> source) {
        ArrayList<T> copy = new ArrayList<T>();
        if (source == null) {
            return copy;
        }
        copy.addAll(source);
        return copy;
    }

    // Keeps only entries that match the rule; null input gives an empty list.
    public static <T> List<T> filter(List<T> source, Predicate<? super T> rule) {
        ArrayList<T> result = new ArrayList<T>();
        if (source == null || rule == null) {
            return result;
        }
        for (int i = 0; i < source.size(); i++) {
            T entry = source.get(i);
            if (rule.test(entry)) {
                result.add(entry);
            }
        }
        return result;
    }
}
