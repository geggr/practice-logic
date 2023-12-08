package br.com.university.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public final class ProblemReader {

    public static final Function<String, String[]> FILE_AS_ARRAY = (file) -> file.split("\\n");
    private static final Function<String, Stream<String>> PARSE_FILE_AS_LINE_STREAM = (file) -> Arrays.stream(file.split("\\n"));

    public static <T> T read(String input, Function<Stream<String>, T> handler){
        return ProblemReader.read(input, PARSE_FILE_AS_LINE_STREAM, handler);
    }

    public static <P, T> T read(String input, Function<String, P> parser, Function<P, T> handler){
        try {

            final var fileName = "%s.txt".formatted(input);
            final var url = ProblemReader.class.getClassLoader().getResource(fileName);


            if (url == null){
                throw new RuntimeException("File %s not found on resources folder".formatted(input));
            }

            final var path = Path.of(url.toURI());

            final var problem = Files.readString(path);

            final var parsed = parser.apply(problem);

            return handler.apply(parsed);
            
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException("File %s not found on resources folder".formatted(input));
        }
    }
}
