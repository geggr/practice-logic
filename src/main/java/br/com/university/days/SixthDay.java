package br.com.university.days;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import br.com.university.utils.ParserUtils;
import br.com.university.utils.ProblemReader;

public class SixthDay implements PuzzleResolver {

    @Override
    public String solveDefaultProblem() {
        final var total = ProblemReader.read("problems/day-six", (lines) -> {

            final var items = lines.map(line -> ParserUtils.findNumberValues(line, Integer::valueOf)).toList();

            final var points = IntStream
                .range(0, items.getFirst().size())
                .mapToObj(index -> new Race(items.get(0).get(index), items.get(1).get(index)))
                .mapToLong(Race::possibilitiesToBeatWorldRecord)
                .reduce(1, (accumulator, current) -> accumulator * current);

            return points;
        });

        return String.valueOf(total);
    }

    @Override
    public String solveCompleteProblem() {
        final var total = ProblemReader.read("problems/day-six", (lines) -> {

            final var items = lines.map(line -> {
                var splitted = line.split(":");
                var values = splitted[1];

                var trimmed = values.replaceAll("\\s+", "");

                return Long.valueOf(trimmed);

            }).toList();

            final var race = new Race(items.get(0), items.get(1));

            final var possibilites = race.possibilitiesToBeatWorldRecord();

            return possibilites;
        });
        
        return String.valueOf(total);
    }

    record Race(long time, long record){
        public Long possibilitiesToBeatWorldRecord(){
            return LongStream
            .range(1, time)
            .parallel()
            .filter(current -> {
                final var distance = (time - current) * current;
                
                return distance > record;
            })
            .count();
        }

    }

    public static void main(String[] args) {
        ProblemReader.read("problems/day-six", (lines) -> {

            final var items = lines.map(line -> {
                var splitted = line.split(":");
                var values = splitted[1];

                var trimmed = values.replaceAll("\\s+", "");

                return Long.valueOf(trimmed);

            }).toList();

            final var race = new Race(items.get(0), items.get(1));

            final var possibilites = race.possibilitiesToBeatWorldRecord();

            System.out.println(possibilites);

            return null;
        });        
    }
}
