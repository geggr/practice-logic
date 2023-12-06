package br.com.university.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class ParserUtils {
    
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    public static List<Long> findNumberValuesAsLong(String input){
        return findNumberValues(input, value -> Long.valueOf(value));
    }

    public static <T> List<T> findNumberValues(String input, Function<String, T> parser){
        final var matcher = NUMBER_PATTERN.matcher(input);
        
        final var values = new ArrayList<T>();

        while(matcher.find()){
            values.add(parser.apply(matcher.group()));
        }

        return values;
    }
}
