package br.com.university;

import java.util.function.Function;

import br.com.university.utils.ProblemReader;

public class Main {
    public static void main(String[] args) {
        ProblemReader.read("day-one", Function.identity());
    }
}