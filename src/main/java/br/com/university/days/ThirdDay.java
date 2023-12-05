package br.com.university.days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import br.com.university.utils.DayReview;
import br.com.university.utils.ProblemReader;

public class ThirdDay implements PuzzleResolver {

    @Override
    public String solveDefaultProblem() {
        return ProblemReader.read("problems/day-three", lines -> {

            char[][] matrix = lines.map(line -> line.toCharArray()).toArray(char[][]::new);

            var total = 0L;

            for (var row = 0; row < matrix.length; row++) {

                var number = new StringBuilder();
                var isAdjacent = false;

                for (var column = 0; column < matrix[row].length; column++) {

                    var current = matrix[row][column];

                    if (Character.isDigit(current)) {
                        number.append(current);

                        isAdjacent = isAdjacent || isAdjacentToASymbol(matrix, row, column);

                        if(column < matrix[row].length -1 ){
                            continue;
                        }
                    }

                    if (!number.isEmpty()) {

                        if (isAdjacent){
                            total += Long.valueOf(number.toString());
                        }

                        isAdjacent = false;
                        number.delete(0, number.length());
                    }

                }

            }

            return String.valueOf(total);
        });
    }


    final class Engine {

        private final String name;
        private int start;
        private int end;

        private int count;
        private long total;
        private long ratio = 1L;

        public Engine(int start, int end){
            this.name = "Engine_[%d][%d]".formatted(start, end);
            this.start = start;
            this.end = end;
            this.count = 0;
        }

        public void add(Long value){
            this.total += value;
            this.count += 1;
            this.ratio *= value;
        }

        public void add(String value){
            add(Long.valueOf(value));
        }

        public boolean hasOnlyTwoMembers(){
            return this.count == 2;
        }

        public Long total(){
            return total;
        }

        public Long ratio(){
            return ratio;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, start, end);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Engine other){
                return name.equals(other.name);
            }

            return false;
        }

        @Override
        public String toString() {
            return """
                {
                    "start": %s,
                    "end": %s,
                    "total": %d,
                    "count": %d
                }        
            """.formatted(start, end, total, count);
        }

    }
    

    @Override
    @DayReview(thought = """
        Estou 2 dias atrasados e não quero perder muito tempo nesse problema...
        
        Maneira mais inteligente de resolver:

        1) Encapsular o "Engine" para conter um HashMap<Coordenada, Pair<Long, Total>>
        2) Não usar ArrayList
        3) Melhorar o método "isAdjacentToASymbol"
        
        Mas por hoje isso funciona...

    """)
    public String solveCompleteProblem() {
        
        return ProblemReader.read("problems/day-three", lines -> {

            char[][] matrix = lines.map(line -> line.toCharArray()).toArray(char[][]::new);

            var adjacency = new ArrayList<Engine>();

            for (var row = 0; row < matrix.length; row++) {

                var number = new StringBuilder();
                var rowAdjancency = new HashSet<Engine>();

                for (var column = 0; column < matrix[row].length; column++) {

                    var current = matrix[row][column];

                    if (Character.isDigit(current)) {

                        number.append(current);

                        isAdjacentToASymbol(matrix, row, column, (character) -> character == '*', (symbolRow, symbolColumn) -> {
                            if (symbolRow == null || symbolColumn == null) return Optional.<Engine>empty();

                            final var engine = new Engine(symbolRow, symbolColumn);

                            return Optional.of(engine);
                        })
                        .ifPresent(engine -> {
                            rowAdjancency.add(engine);
                        });

                        if(column < matrix[row].length -1 ){
                            continue;
                        }
                    }

                    if (!number.isEmpty()){
                        
                        for (var engine : rowAdjancency){

                            engine.add(number.toString());
                            
                            adjacency
                                .stream()
                                .filter(it -> it.equals(engine))
                                .findFirst()
                                .ifPresentOrElse(
                                    it -> it.add(engine.total()),
                                    () -> adjacency.add(engine));

                        }

                        number.delete(0, number.length());
                        rowAdjancency.clear();
                    }
                }
            }

            final var total = adjacency
                .stream()
                .filter(Engine::hasOnlyTwoMembers)
                .mapToLong(Engine::ratio)
                .sum();

            return String.valueOf(total);
        });
    }

    private static boolean isASymbol(char character) {
        return character != '.' && !Character.isDigit(character);
    }

    private static boolean isAdjacentToASymbol(char[][] matrix, int row, int column) {
        return isAdjacentToASymbol(matrix, row, column, ThirdDay::isASymbol, (symbolRow, symbolColumn) -> Objects.nonNull(symbolColumn) && Objects.nonNull(symbolColumn));
    }

    private static <T> T isAdjacentToASymbol(char[][] matrix, int row, int column, Predicate<Character> filter, BiFunction<Integer, Integer, T> parser) {
        final var top = Math.max(0, row - 1);
        final var bottom = Math.min(matrix.length - 1, row + 1);
        
        final var right = Math.min(matrix[row].length - 1, column + 1);
        final var left = Math.max(0, column - 1);

        
        if (filter.test(matrix[top][column])){
            return parser.apply(top, column);
        }

        if (filter.test(matrix[bottom][column])){
            return parser.apply(bottom, column);
        }

        if (filter.test(matrix[row][right])){
            return parser.apply(row, right);
        }

        if (filter.test(matrix[row][left])){
            return parser.apply(row, left);

        }

        if (filter.test(matrix[top][right])){
            return parser.apply(top, right);

        }

        if (filter.test(matrix[top][left])){
            return parser.apply(top, left);

        }

        if (filter.test(matrix[bottom][right])){
            return parser.apply(bottom, right);

        }

        if (filter.test(matrix[bottom][left])){
            return parser.apply(bottom, left);
        }

        return parser.apply(null, null);
    }

    public static void main(String[] args) {
        System.out.println(new ThirdDay().solveCompleteProblem());
    }

}
