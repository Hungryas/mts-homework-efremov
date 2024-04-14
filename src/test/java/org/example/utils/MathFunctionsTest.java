package org.example.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MathFunctionsTest {

    @ParameterizedTest(name = "{displayName} [{index}] number={0}")
    @DisplayName("Позитивный тест factorial")
    @CsvSource(textBlock = """
            0  ,1
            1  ,1
            2  ,2
            25 ,15511210043330985984000000
            """)
    void successFactorial(int number, BigInteger expectedFactorial) {
        assertThat(MathFunctions.factorial(number)).isEqualTo(expectedFactorial);
    }

    @Test
    @DisplayName("Негативный тест factorial")
    void failureFactorial() {
        assertThatThrownBy(() -> MathFunctions.factorial(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("число не может быть отрицательным");
    }

    @ParameterizedTest(name = "{displayName} [{index}] number={0}")
    @DisplayName("Позитивный тест fibonacciSequence")
    @CsvSource(quoteCharacter = '"', textBlock = """
            0,"0"
            1,"0,1"
            3,"0,1,1,2"
            25,"0,1,1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,1597,2584,4181,6765,10946,17711,28657,46368,75025"
            """)
    void successFibonacciSequence(int number, String expectedFibonacciString) {
        List<BigInteger> expectedFibonacciSequence = getBigIntegerListFromString(expectedFibonacciString);
        assertThat(MathFunctions.fibonacciSequence(number)).containsExactlyElementsOf(expectedFibonacciSequence);
    }

    private List<BigInteger> getBigIntegerListFromString(String expectedPrimeNumbersString) {
        return Arrays.stream(expectedPrimeNumbersString.split(","))
                .map(BigInteger::new)
                .toList();
    }

    @Test
    @DisplayName("Негативный тест fibonacciSequence")
    void failureFibonacciSequence() {
        assertThatThrownBy(() -> MathFunctions.factorial(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("число не может быть отрицательным");
    }

    @ParameterizedTest(name = "{displayName} [{index}] start={0}, end={1}")
    @DisplayName("Позитивный тест primeNumbersFromRange")
    @CsvSource(quoteCharacter = '"', textBlock = """
            2, 2  , "2"
            0, 100, "2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59,61,67,71,73,79,83,89,97",
            101, 228, "101,103,107,109,113,127,131,137,139,149,151,157,163,167,173,179,181,191,193,197,199,211,223,227"
            """)
    void successPrimeNumbersFromRange(int start, int end, String expectedPrimeNumbersString) {
        List<BigInteger> expectedPrimeNumbers = getBigIntegerListFromString(expectedPrimeNumbersString);
        assertThat(MathFunctions.primeNumbersFromRange(start, end)).containsExactlyElementsOf(expectedPrimeNumbers);
    }

    @ParameterizedTest(name = "{displayName} [{index}] start={0}, end={1}")
    @DisplayName("Негативный тест primeNumbersFromRange с отрицательными параметрами")
    @CsvSource({
            "-1, 1",
            " 1,-1"
    })
    void failurePrimeNumbersFromRangeWithNegativeNumber(int start, int end) {
        assertThatThrownBy(() -> MathFunctions.primeNumbersFromRange(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("число не может быть отрицательным");
    }

    @Test
    @DisplayName("Негативный тест primeNumbersFromRange с отрицательной длиной диапазона")
    void failurePrimeNumbersFromRangeWithReverseRange() {
        assertThatThrownBy(() -> MathFunctions.primeNumbersFromRange(2, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("начало диапазона должно быть меньше конца");
    }

    @Test
    @DisplayName("Позитивный тест generateNumbersInSingleThreadMode")
    void successGenerateNumbersInSingleThreadMode() {
        int size = RandomGenerator.getDefault().nextInt(1, 1000);
        List<Long> numbersInSingleThreadMode = MathFunctions.generateNumbersInSingleThreadMode(size);
        assertThat(numbersInSingleThreadMode).hasSize(size);
    }

    @Test
    @DisplayName("Негативный тест generateNumbersInSingleThreadMode")
    void failureGenerateNumbersInSingleThreadMode() {
        assertThatThrownBy(() -> MathFunctions.generateNumbersInSingleThreadMode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("число должно быть больше 0");
    }

    @Test
    @DisplayName("Позитивный тест generateNumbersInMultiThreadedMode")
    void successGenerateNumbersInMultiThreadedMode() {
        int size = RandomGenerator.getDefault().nextInt(1, 1000);
        List<Long> numbersInSingleThreadMode = MathFunctions.generateNumbersInMultiThreadedMode(size);
        assertThat(numbersInSingleThreadMode).hasSize(size);
    }

    @Test
    @DisplayName("Негативный тест generateNumbersInMultiThreadedMode")
    void failureGenerateNumbersInMultiThreadedModer() {
        assertThatThrownBy(() -> MathFunctions.generateNumbersInMultiThreadedMode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("число должно быть больше 0");
    }
}