package br.com.university.days;

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

    record Number(int number, int row, int start, int end){}
    record Symbol(char character, int row, int start, int end){}

    @Override
    public String solveCompleteProblem() {
        
        return ProblemReader.read("problems/day-three", lines -> {

            char[][] matrix = lines.map(line -> line.toCharArray()).toArray(char[][]::new);

            var total = 0L;

            for (var row = 0; row < matrix.length; row++) {

                var number = new StringBuilder();
                var isAdjacent = false;

                for (var column = 0; column < matrix[row].length; column++) {

                    var current = matrix[row][column];

                    if (current == '.'){

                    }

                }

            }

            return String.valueOf(total);
        });
    }

    private static boolean isASymbol(char character) {
        return character != '.' && !Character.isDigit(character);
    }

    private static boolean isAdjacentToASymbol(char[][] matrix, int row, int column) {
        final var top = Math.max(0, row - 1);
        final var bottom = Math.min(matrix.length - 1, row + 1);
        
        final var right = Math.min(matrix[row].length - 1, column + 1);
        final var left = Math.max(0, column - 1);

        
        if (isASymbol(matrix[top][column])){
            return true;
        }

        if (isASymbol(matrix[bottom][column])){
            return true;
        }

        if (isASymbol(matrix[row][right])){
            return true;
        }

        if (isASymbol(matrix[row][left])){
            return true;

        }

        if (isASymbol(matrix[top][right])){
            return true;

        }

        if (isASymbol(matrix[top][left])){
            return true;

        }

        if (isASymbol(matrix[bottom][right])){
            return true;

        }

        if (isASymbol(matrix[bottom][left])){
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        
    }

}
