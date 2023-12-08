package br.com.university.days;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import br.com.university.utils.ProblemReader;

public class SeventhDay implements PuzzleResolver {

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

    enum Card {
        CARD_A(12),
        CARD_K(11),
        CARD_Q(10),
        CARD_J(9),
        CARD_T(8),
        CARD_9(7),
        CARD_8(6),
        CARD_7(5),
        CARD_6(4),
        CARD_5(3),
        CARD_4(2),
        CARD_3(1),
        CARD_2(-1);

        private Integer power;

        Card(Integer power){
            this.power = power;
        }

        public Integer getPower() {
            return power;
        }

        public String getOriginalValue(){
            return name().replace("CARD_", "");
        }

        public static Card find(String card){
            return Card.valueOf("CARD_" + card);
        }

        public int hasMorePower(Card other){
            return power.compareTo(other.power);
        }
    }

    enum HandType {
        FIVE_OF_KIND(5),
        FOUR_OF_KIND(4),
        FULL_HOUSE(3),
        THREE_OF_KIND(2),
        TWO_PAIR(1),
        ONE_PAIR(0),
        HIGH_CARD(-1);

        private Integer power;

        HandType(Integer power){
            this.power = power;
        }

        public Integer getPower() {
            return power;
        }

        public static HandType find(List<Card> cards){
            final var grouped = cards.stream().collect(groupingBy(identity(), counting()));

            var numberOfFiveKind = 0;
            var numberOfFourKind = 0;
            var numberOfThreeKind = 0;
            var numberOfTwoKind = 0;

            for (var entry : grouped.entrySet()) {
                var total = (long) entry.getValue();

                if (total == 5L){
                    numberOfFiveKind++;
                }
                else if (total == 4){
                    numberOfFourKind++;
                }
                else if (total == 3){
                    numberOfThreeKind++;
                }
                else if (total == 2) {
                    numberOfTwoKind++;
                }
            }

            if (numberOfFiveKind == 1){
                return FIVE_OF_KIND;
            }

            if (numberOfFourKind == 1){
                return FOUR_OF_KIND;
            }

            if (numberOfThreeKind == 1 && numberOfTwoKind == 1){
                return FULL_HOUSE;
            }

            if (numberOfThreeKind == 1){
                return THREE_OF_KIND;
            }

            if (numberOfTwoKind == 2){
                return TWO_PAIR;
            }

            if (numberOfTwoKind == 1){
                return ONE_PAIR;
            }

            return HIGH_CARD;
        }

    }

    record Hand(HandType type, List<Card> cards, Long amount) implements Comparable {
        
        public static Hand of(String[] rawCards, String rawAmount) {
            final var cards = Arrays.stream(rawCards).map(Card::find).toList();
            final var amount = Long.valueOf(rawAmount);
            final var type = HandType.find(cards);
            return new Hand(type, cards, amount);
        }

        public Card getCardAt(int index){
            return cards.get(index);
        }

        public String getPrettyCardHand(){
            return cards.stream().map(Card::getOriginalValue).collect(joining(""));
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Hand other){
                final var handTypePower = this.type.getPower().compareTo(other.type().getPower());

                if (handTypePower != 0) return handTypePower;

                for(var position = 0; position < cards.size(); position++){

                    final var cardPower = getCardAt(position).hasMorePower(other.getCardAt(position));

                    if (cardPower != 0) return cardPower;
                }

                return 0;
            }

            return -1;
        }
    }

    public static void main(String[] args) {
        ProblemReader.read("problem/day-seven", lines -> {
            final var hands = lines.map(line -> {
                final var splitted = line.split("\\s+");
                final var joinedCards = splitted[0];
                final var amount = splitted[1];
                final var cards = joinedCards.split("");

                return Hand.of(cards, amount);
            })
            .sorted()
            .toList();

            final var total = IntStream
                .range(0, hands.size())
                .mapToLong(index -> {
                    final var rank = index + 1;
                    final var hand = hands.get(index);


                    return hand.amount() * rank;
                })
                .sum();

            System.out.println(total);

            return null;
        });
    }
    
}
