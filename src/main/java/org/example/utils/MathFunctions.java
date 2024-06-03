package org.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathFunctions {

    private static final String ILLEGAL_NEGATIVE_ERROR_MESSAGE = "число не может быть отрицательным";

    private static final String ILLEGAL_ZERO_ERROR_MESSAGE = "число должно быть больше 0";

    public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static BigInteger factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(ILLEGAL_NEGATIVE_ERROR_MESSAGE);
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
            throw new IllegalArgumentException(ILLEGAL_NEGATIVE_ERROR_MESSAGE);
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

    public static List<BigInteger> primeNumbersFromRange(int start, int end) {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException(ILLEGAL_NEGATIVE_ERROR_MESSAGE);
        }
        if (start > end) {
            throw new IllegalArgumentException("начало диапазона должно быть меньше конца");
        }

        List<Integer> range = IntStream.rangeClosed(start, end).boxed().toList();
        List<List<Integer>> rangeGroups = splitListForConcurrency(range);
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

    private static <T> List<List<T>> splitListForConcurrency(List<T> range) {
        int size = range.size();
        int threads = Math.min(size, PROCESSORS);
        int segment = Math.ceilDiv(size, threads);
        List<List<T>> resultList = new ArrayList<>();

        for (int start = 0; start < size; start += segment) {
            int batch = Math.min(segment, size - start);
            resultList.add(range.subList(start, start + batch));
        }

        return resultList;
    }

    private static List<BigInteger> filterPrimeNumber(List<Integer> numbers) {
        return numbers.stream()
                .mapToLong(Long::valueOf)
                .mapToObj(BigInteger::valueOf)
                .filter(number -> number.isProbablePrime(100))
                .toList();
    }

    public static List<Long> generateNumbersInSingleThreadMode(int size) {
        if (size < 0) {
            throw new IllegalArgumentException(ILLEGAL_ZERO_ERROR_MESSAGE);
        }

        return Stream.generate(() -> RandomGenerator.getDefault().nextLong())
                .limit(size)
                .toList();
    }

    public static List<Long> generateNumbersInMultiThreadedMode(int size) {
        if (size < 1) {
            throw new IllegalArgumentException(ILLEGAL_ZERO_ERROR_MESSAGE);
        }

        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            int threads = Math.min(size, PROCESSORS);
            int segment = Math.ceilDiv(size, threads);
            List<Future<List<Long>>> futures = new ArrayList<>();

            for (int tasks = 0; tasks < size; tasks += segment) {
                int batch = Math.min(segment, size - tasks);
                Callable<List<Long>> task = () -> generateNumbersInSingleThreadMode(batch);
                futures.add(executor.submit(task));
            }
            List<Long> resultList = new ArrayList<>();

            for (Future<List<Long>> future : futures) {
                try {
                    resultList.addAll(future.get());
                } catch (InterruptedException e) {
                    log.warn("Interrupted!", e);
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    log.error(e.getMessage());
                }
            }
            return resultList;
        }
    }
}