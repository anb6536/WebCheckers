package com.webcheckers.model;

import java.util.Comparator;

/**
 * This class solely exists for sorting inside lists
 */
public class RowComparator implements Comparator<Row> {

    @Override
    public int compare(Row o1, Row o2) {
        return o1.compareTo(o2);
    }
}