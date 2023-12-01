package br.com.university.days;

import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import br.com.university.utils.ProblemReader;

public class FirstDay implements PuzzleResolver {

    record NumberMatch(String number, int start, int end){
        private static NumberMatch empty(){
            return new NumberMatch(null, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }
    }

    private String convert(NumberMatch match){
        return convert(match.number());
    }

    private String convert(String input){
        if (input.isBlank() || Character.isDigit(input.codePointAt(0))) return input;

        return switch(input){
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> throw new RuntimeException("Failed to parse number %s".formatted(input));
        };
    }

    @Override
    public String solveDefaultProblem() {
        return ProblemReader.read("problems/day-one", lines -> {
            final var pattern = Pattern.compile("(\\d)");

            final var response = Arrays.stream(lines).map(line -> pattern.matcher(line)).mapToInt(matcher -> {

                var isEmpty = !(matcher.find());

                if (isEmpty) {
                    return 0;
                }

                var first = matcher.group();

                var last = "";

                while (matcher.find()) {
                    last = matcher.group();
                }

                final var number = last.isBlank()
                        ? first.concat(first)
                        : first.concat(last);

                return Integer.valueOf(number);

            }).sum();

            return String.valueOf(response);
        });
    }

    @Override
    public String solveCompleteProblem() {
        return ProblemReader.read("problems/day-one", lines -> {

            final var patterns = Stream
                .of("1", "2", "3", "4", "5", "6", "7", "8", "9", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
                .map(Pattern::compile)
                .toList();

            final var total = Arrays.stream(lines).mapToInt(line -> {
                
                final var matches = patterns.stream().map(pattern -> {

                    final var element = pattern.pattern();

                    final var match = pattern.matcher(line);

                    final var isEmpty = !match.find();

                    if (isEmpty){
                        return NumberMatch.empty();
                    }

                    var start = match.start();
                    var end = match.end();

                    while(match.find()){
                        end = match.end();
                    }

                    return new NumberMatch(element, start, end);
                })
                .toList();

                final var first = matches.stream().min(Comparator.comparingInt(NumberMatch::start)).map(this::convert).orElse("");
                final var last = matches.stream().max(Comparator.comparingInt(NumberMatch::end)).map(this::convert);
                
                final var number = last.isEmpty()
                    ? first.concat(first)
                    : first.concat(last.get());
                
                return Integer.valueOf(number);
            })
            .sum();

            return String.valueOf(total);
        });
    }
}
