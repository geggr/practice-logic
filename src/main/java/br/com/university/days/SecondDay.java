package br.com.university.days;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import br.com.university.utils.ProblemReader;

public class SecondDay implements PuzzleResolver {

    record Round(Integer roundNumber, List<Bag> bags) {
        private static final Pattern GAME_ID_PATTERN = Pattern.compile("Game (\\d+)");

        public boolean isValidRound(Bag reference) {
            return bags.stream().allMatch(bag -> bag.permits(reference));
        }

        public Long power() {
            var red = 1;
            var green = 1;
            var blue = 1;

            for (var bag : bags) {

                if (bag.red() > red) {
                    red = bag.red();
                }

                if (bag.green() > green) {
                    green = bag.green();
                }

                if (bag.blue() > blue) {
                    blue = bag.blue;
                }
            }

            return Long.valueOf(red * green * blue);
        }

        private static Round of(String input, List<Bag> bags) {
            final var gameMatcher = GAME_ID_PATTERN.matcher(input);

            gameMatcher.find();

            final var id = gameMatcher.group(1);

            return new Round(Integer.valueOf(id), bags);
        }
    }

    record Bag(int red, int green, int blue) {

        private static final Pattern LINE_PATTERN = Pattern.compile("(\\d+)\\s+(\\w+)");

        public boolean permits(Bag other) {
            return red <= other.red && green <= other.green && blue <= other.blue;
        }

        public static List<Bag> of(String[] rounds) {

            final var bags = Arrays.stream(rounds).map(round -> {
                final var items = new HashMap<String, Integer>(3);

                final var matcher = LINE_PATTERN.matcher(round);

                while (matcher.find()) {
                    items.put(matcher.group(2), Integer.valueOf(matcher.group(1)));
                }

                return new Bag(
                        items.getOrDefault("red", 0),
                        items.getOrDefault("green", 0),
                        items.getOrDefault("blue", 0));

            }).toList();

            return bags;
        }

    }


    private Round parse(String line) {
        final var split = line.split(":");

        final var game = split[0];
        final var inputs = split[1];

        final var rounds = inputs.split(";");

        final var bags = Bag.of(rounds);

        return Round.of(game, bags);
    }

    @Override
    public String solveDefaultProblem() {
        final var reference = new Bag(12, 13, 14);

        final var count = ProblemReader.read("problems/day-two", lines -> {
            return lines
                    .map(line -> parse(line))
                    .filter(round -> round.isValidRound(reference))
                    .mapToInt(Round::roundNumber)
                    .sum();
        });

        return String.valueOf(count);
    }

    @Override
    public String solveCompleteProblem() {
        final var power = ProblemReader.read("problems/day-two", lines -> {
            return lines
                    .map(line -> parse(line))
                    .mapToLong(Round::power)
                    .sum();
        });

        return String.valueOf(power);
    }
}
