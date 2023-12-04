package br.com.university.days;

import static java.util.Map.entry;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

            final var response = lines.map(line -> pattern.matcher(line)).mapToInt(matcher -> {

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

            final var total = lines.mapToInt(line -> {
                
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

    private String reverse(String input){
        return new StringBuilder(input).reverse().toString(); 
    }

    public String solveCompleteProblemInOtherWay(){

        final var patterns = new TreeMap<String, String>(Map.ofEntries(
            entry("1", "1"),
            entry("2", "2"),
            entry("3", "3"),
            entry("4", "4"),
            entry("5", "5"),
            entry("6", "6"),
            entry("7", "7"),
            entry("8", "8"),
            entry("9", "9"),
            entry("one", "1"),
            entry("two", "2"),
            entry("three", "3"),
            entry("four", "4"),
            entry("five", "5"),
            entry("six", "6"),
            entry("seven", "7"),
            entry("eight", "8"),
            entry("nine", "9")
        ));

        final var defaultPattern = patterns.keySet().stream().collect(Collectors.joining("|"));
        final var reversePattern = patterns.keySet().stream().map(pattern -> reverse(pattern)).collect(Collectors.joining("|"));

        final var pattern = Pattern.compile(defaultPattern);
        final var reversedPattern = Pattern.compile(reversePattern);

        final var total = ProblemReader.read("problems/day-one", lines -> {
            
            return lines.map(line -> {
                final var reversedLine = reverse(line);

                final var matcher = pattern.matcher(line);
                final var reversedMatcher = reversedPattern.matcher(reversedLine);


                matcher.find();
                reversedMatcher.find();

                final var first = matcher.group();
                final var second = reversedMatcher.group();

                final var firstNumber = patterns.get(first);
                final var secondNumber = patterns.getOrDefault(reverse(second), firstNumber);

                return firstNumber.concat(secondNumber);

            })
            .mapToInt(Integer::valueOf)
            .sum();
        });

        return String.valueOf(total);

    }

}
