package com.example.mystore.utils;

import java.util.ArrayList;
import java.util.List;



public class CollectionUtils {

    /**
     * Partitions a given list into sublists of a specified maximum size.
     *
     * For example, partitioning a list of 10 elements with size 3 would result in:
     * [0-2], [3-5], [6-8], [9]
     *
     * @param list the original list to partition
     * @param size the maximum size of each partition
     * @param <T> the type of elements in the list
     * @return a list of partitions (each partition is a list of elements)
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}