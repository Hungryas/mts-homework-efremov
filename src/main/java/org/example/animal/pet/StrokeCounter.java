package org.example.animal.pet;

import java.util.concurrent.atomic.LongAdder;

public interface StrokeCounter {

    LongAdder counter = new LongAdder();

    default void stroke() {
        counter.increment();
    }

    default Long strokeSum() {
        return counter.sumThenReset();
    }
}
