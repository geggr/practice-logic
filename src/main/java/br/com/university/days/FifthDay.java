package br.com.university.days;

import static br.com.university.utils.ProblemReader.FILE_AS_ARRAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import br.com.university.utils.DayReview;
import br.com.university.utils.ParserUtils;
import br.com.university.utils.PartitionUtils;
import br.com.university.utils.ProblemReader;

public class FifthDay implements PuzzleResolver {

    @Override
    public String solveDefaultProblem() {
         final var minimum = ProblemReader.read("statements/day-five", (file) -> file.split("\\n"), maps -> {

            final var seeds = parseSeedPositions(maps[0]);
            final var lines = Arrays.copyOfRange(maps, 1, maps.length);

            final var almanac = parseAlmanac(lines);

            return seeds
                .stream()
                .mapToLong(seed -> {
                    final var location = almanac.findLocation(seed);

                    System.out.println("Seed with source [%d]  has location [%d]".formatted(seed, location));

                    return location;
                })
                .min()
                .getAsLong();
        });

        return String.valueOf(minimum);
    }

    @Override
    @DayReview(thought = """
        Eu nunca deveria estar fazendo isso via brute force
        Se eu chequei a seed (83) que me leva para => 47
        não há razão para checar a <84, ..., 92> pois todas serão maiores...
    """)
    public String solveCompleteProblem() {
        final var response = ProblemReader.read("statements/day-five", FILE_AS_ARRAY, maps -> {

            var minimum = Long.MAX_VALUE;

            final var lines = Arrays.copyOfRange(maps, 1, maps.length);
            final var almanac = parseAlmanac(lines);
            
            List<Long> parsed = parseSeedPositions(maps[0]);

            var partitions = PartitionUtils.partition(parsed, 2);

            for (var index = 0; index < partitions.size(); index++){

                var partition = partitions.get(index);

                System.out.println("Partition %d / %d".formatted(index, partitions.size()));

                var start = partition.get(0);
                var end = partition.get(1);

                var local = LongStream
                    .range(0, end)
                    .map(it -> start + it)
                    .map(seed -> {
                        final var location = almanac.findLocation(seed);

                        System.out.println("Seed with source [%d] has location [%s]".formatted(seed, location));

                        return location;
                    })
                    .min()
                    .getAsLong();


                if (local < minimum){
                    minimum = local;
                }
            }

            return minimum;
        });

        return String.valueOf(response);
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
        var total = new FifthDay().solveCompleteProblem();
        System.out.println(total);
    }
    
}
