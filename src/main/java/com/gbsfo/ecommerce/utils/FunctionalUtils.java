package com.gbsfo.ecommerce.utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FunctionalUtils {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> {
            var val = keyExtractor.apply(t);
            if (val == null) {
                return false;
            }
            return seen.add(val);
        };
    }
}