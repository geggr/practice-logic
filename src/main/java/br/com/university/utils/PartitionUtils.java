package br.com.university.utils;

import java.util.ArrayList;
import java.util.List;

public class PartitionUtils {

    public static <T> List<List<T>> partition(List<T> items, int size){
        final var response = new ArrayList<List<T>>();
        
        response.add(new ArrayList<T>());

        for(var index = 0; index < items.size(); index++){

            var current = response.get(response.size() - 1);
            var element = items.get(index);

            if (current.size() < size){
                current.add(element);
            }
            else {
                var next = new ArrayList<T>();
                next.add(element);
                response.add(next);
                
            }
        }

        return response;
    }
}
