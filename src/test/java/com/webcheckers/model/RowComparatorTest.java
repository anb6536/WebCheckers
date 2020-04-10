package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RowComparatorTest {

    private List<Row> rows = new ArrayList<>();

    private final RowComparator comparator = new RowComparator();
    private final Random random = new Random();

    @Test
    void testSortedBackwards() {
        for (int i = 10; i > 0; i--)
            rows.add(new Row(i, new ArrayList<>()));
        rows.sort(comparator);
        int lastIndex = 0;
        for (Row row : rows) {
            assert lastIndex <= row.getIndex();
            lastIndex = row.getIndex();
        }
    }

    @Test
    void testUnsorted() {
        for (int i = 20; i > 0; i--)
            rows.add(new Row((int) (random.nextDouble() * 10), new ArrayList<>()));
        rows.sort(comparator);
        int lastIndex = 0;
        for (Row row : rows) {
            assert lastIndex <= row.getIndex();
            lastIndex = row.getIndex();
        }
    }
}
