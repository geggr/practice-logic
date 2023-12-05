package br.com.university.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.com.university.utils.ProblemReader;

public class FourthDay implements PuzzleResolver {

    record Round(Integer number, Set<Integer> winnningNumbers, Set<Integer> currentNumbers) {

        private final static Pattern GAME_PATTERN = Pattern.compile("(\\d+)");

        public Integer roundPoints() {
            var total = 0;

            for (var number : currentNumbers) {
                if (winnningNumbers.contains(number)) {
                    total = Math.max(1, total * 2);
                }
            }

            return total;
        }

        public Integer overlap() {
            return (int) currentNumbers
                    .stream()
                    .filter(number -> winnningNumbers.contains(number))
                    .count();
        }

        public static Round of(String line) {
            final var splitted = line.split(":");

            final var name = splitted[0];

            final var matcher = GAME_PATTERN.matcher(name);

            matcher.find();

            final var id = matcher.group(0);

            final var cards = splitted[1];
            final var splittedCards = cards.split("\\|");

            final var winning = lineToSet(splittedCards[0]);
            final var current = lineToSet(splittedCards[1]);

            return new Round(Integer.valueOf(id), winning, current);
        }
    }

    private static Set<Integer> lineToSet(String input) {
        return Arrays
                .stream(input.split("\\s+"))
                .filter(value -> !value.trim().isEmpty())
                .map(value -> Integer.valueOf(value.trim()))
                .collect(Collectors.toSet());
    }

    @Override
    public String solveDefaultProblem() {
        final var total = ProblemReader.read("problems/day-four", lines -> {
            return lines
                    .map(Round::of)
                    .mapToInt(Round::roundPoints)
                    .sum();
        });

        return String.valueOf(total);
    }

    @Override
    public String solveCompleteProblem() {
        final var sum = ProblemReader.read("problems/day-four", lines -> {

            final var total = new HashMap<Integer, Integer>();
            final var rounds = lines.map(Round::of).toList();

            for (var round : rounds) {
                final var current = round.number();
                final var overlap = round.overlap();

                total.compute(current, (currentKey, currentValue) -> {
                    if (currentValue == null) return 1;

                    return currentValue + 1;
                });

                final var powerup = total.get(current);

                for (var index = 1; index <= overlap; index++){
                    final var next = current + index;

                    total.compute(next, (currentKey, currentValue) -> {
                        if (currentValue == null) return powerup;

                        return currentValue + powerup;
                    });
                }
            }

            return total.values().stream().reduce(0, (acc, current) -> acc + current);
        });

        return String.valueOf(sum);
    }
}
