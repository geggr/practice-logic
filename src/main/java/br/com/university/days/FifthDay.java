package br.com.university.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import br.com.university.utils.ParserUtils;
import br.com.university.utils.ProblemReader;

public class FifthDay implements PuzzleResolver {

    @Override
    public String solveDefaultProblem() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solveDefaultProblem'");
    }

    @Override
    public String solveCompleteProblem() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solveCompleteProblem'");
    }

    private static Almanac parseAlmanac(String[] lines){

        List<FifthDay.AlmanacMap> maps = new ArrayList<>();

        maps.add(new AlmanacMap());

        for(var line : lines){
            if (line.isBlank()){
                maps.add(new AlmanacMap());
            }
            else {
                final var current = maps.get(maps.size() - 1);

                AlmancItem.of(line).ifPresentOrElse(
                    item -> current.add(item),
                    () -> current.changeName(line)
                );
            }

        }

        return new Almanac(maps);
    }

    private static List<Long> parseSeedPositions(String line){
        final var pattern = Pattern.compile("(\\d+)");
        final var matcher = pattern.matcher(line);
        final var seeds = new ArrayList<Long>();

        while(matcher.find()){
            final var seed = Long.valueOf(matcher.group());
            seeds.add(seed);
        }

        return seeds;
    }

    
    public static class AlmanacMap {
        private String name;
        private List<AlmancItem> items = new ArrayList<>();
        
        public void changeName(String name){
            this.name = name;    
        }

        public void add(AlmancItem item){
            items.add(item);
        }

        public Long convert(Long source){
            return items
                .stream()
                .filter(item -> {
                    final var maxValueInRange = item.source() + item.range();

                    return (source >= item.source()) && (maxValueInRange - source) > 0;
                })
                .findFirst()
                .map(item -> {
                    final var position = (item.source() + item.range());
                    final var total = source + item.range();

                    return item.destination() + (total % position);
                })
                .orElse(source);
        }

        @Override
        public String toString() {
            return """
                {
                    "name": %s,
                    "items": %s
                }        
            """.formatted(name, items);
        }
    }

    record AlmancItem(long destination, long source, long range){
        public static Optional<AlmancItem> of(String line) {
            final var numbers = ParserUtils.findNumberValuesAsLong(line);

            if (numbers.isEmpty()) return Optional.empty();

            return Optional.of(new AlmancItem(numbers.get(0), numbers.get(1), numbers.get(2)));
        }
    }

    public static class Almanac {
        
        private List<AlmanacMap> maps;

        public Almanac(List<AlmanacMap> maps){
            this.maps = maps;
        }

        public Long findLocation(Long position){
            var location = position;

            for (var map : maps){
                location = map.convert(location);
            }

            return location;
        }

        public List<AlmanacMap> maps(){
            return maps;
        }

    }

    public static void main(String[] args) {
        final var minimum = ProblemReader.read("problems/day-five", (file) -> file.split("\\n"), maps -> {

            final var seeds = parseSeedPositions(maps[0]);
            final var lines = Arrays.copyOfRange(maps, 1, maps.length);

            final var almanac = parseAlmanac(lines);

            return seeds
                .stream()
                .mapToLong(seed -> almanac.findLocation(seed))
                .min()
                .getAsLong();
        });

        System.out.println(minimum);
    }
    
}
