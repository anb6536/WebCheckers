package com.webcheckers.model;

import java.util.Comparator;

public class SpaceComparator implements Comparator<Space> {
    @Override
    public int compare(Space o1, Space o2) {
        return o1.compareTo(o2);
    }
}
