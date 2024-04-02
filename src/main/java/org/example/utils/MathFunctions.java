package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.ListUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static org.example.errors.Error.ILLEGAL_NEGATIVE;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathFunctions {

    public static BigInteger factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(ILLEGAL_NEGATIVE.messageWith("число"));
        } else if (number < 2) {
            return BigInteger.ONE;
        } else if (number == 2) {
            return BigInteger.TWO;
        }

        return IntStream.rangeClosed(2, number)
                .parallel()
                .mapToObj(String::valueOf)
                .map(BigInteger::new)
                .reduce(BigInteger.ONE, BigInteger::multiply);
    }

    public static List<BigInteger> fibonacciSequence(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(ILLEGAL_NEGATIVE.messageWith("число"));
        }

        return IntStream.rangeClosed(0, number)
                .parallel()
                .mapToObj(String::valueOf)
                .map(BigInteger::new)
                .map(MathFunctions::fibonacci)
                .toList();
    }

    private static BigInteger fibonacci(BigInteger number) {
        if (number.equals(BigInteger.ZERO) || number.equals(BigInteger.ONE)) {
            return number;
        }
        BigInteger previous = number.subtract(BigInteger.ONE);
        BigInteger prePrevious = number.subtract(BigInteger.TWO);

        return fibonacci(previous).add(fibonacci(prePrevious));
    }

    public static List<BigInteger> primeNumbersFromRage(int start, int end) {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException(ILLEGAL_NEGATIVE.messageWith("число"));
        }
        if (start > end) {
            throw new IllegalArgumentException("начало диапазона должно быть меньше конца");
        }

        List<Integer> range = IntStream.rangeClosed(start, end).boxed().toList();
        List<List<Integer>> rangeGroups = splitRangeForConcurrency(range);
        List<CompletableFuture<List<BigInteger>>> futures = new ArrayList<>();

        for (List<Integer> group : rangeGroups) {
            CompletableFuture<List<BigInteger>> future = CompletableFuture.supplyAsync(() -> filterPrimeNumber(group));
            futures.add(future);
        }

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .toList();
    }

    private static <T> List<List<T>> splitRangeForConcurrency(List<T> range) {
        int processors = Runtime.getRuntime().availableProcessors();
        int size = range.size();
        int threads = size > processors ? processors - 1 : size;

        return ListUtils.partition(range, size / threads);
    }

    private static List<BigInteger> filterPrimeNumber(List<Integer> numbers) {
        return numbers.stream()
                .mapToLong(Long::valueOf)
                .mapToObj(BigInteger::valueOf)
                .filter(number -> number.isProbablePrime(100))
                .toList();
    }
}