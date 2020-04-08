package com.webcheckers.model;

import java.util.Comparator;

/**
 * This class exists solely for sorting purposes to make sure that rows make
 * sense when displayed
 */
public class SpaceComparator implements Comparator<Space> {
    @Override
    public int compare(Space o1, Space o2) {
        return o1.compareTo(o2);
    }
}
