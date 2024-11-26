package com.soen357.cards.data;
import com.soen357.cards.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

    public class CardData {
        public static List<Card> getFullDeck() {
            List<Card> deck = new ArrayList<>();
            deck.add(new Card("What is the capital of France?", "Paris", R.drawable.paris));
            deck.add(new Card("What is 2 + 2?", "4"));
            deck.add(new Card("What is the largest planet in our solar system?", "Jupiter", R.drawable.jupiter));
            deck.add(new Card("Who wrote '1984'?", "George Orwell"));
            deck.add(new Card("What is the chemical symbol for water?", "H2O"));
            deck.add(new Card("Which element has the atomic number 1?", "Hydrogen"));
            deck.add(new Card("What is the capital of Japan?", "Tokyo", R.drawable.tokyo));
            deck.add(new Card("Who painted the Mona Lisa?", "Leonardo da Vinci", R.drawable.monalisa));
            deck.add(new Card("What is the fastest land animal?", "Cheetah"));
            deck.add(new Card("How many continents are there on Earth?", "7"));
            deck.add(new Card("What is the capital of Australia?", "Canberra"));
            deck.add(new Card("Which planet is known as the Red Planet?", "Mars", R.drawable.mars));
            deck.add(new Card("What is the smallest prime number?", "2"));
            deck.add(new Card("Who discovered penicillin?", "Alexander Fleming"));
            deck.add(new Card("What is the tallest mountain in the world?", "Mount Everest"));
            deck.add(new Card("Which gas do plants use for photosynthesis?", "Carbon Dioxide"));
            deck.add(new Card("What is the largest ocean on Earth?", "Pacific Ocean"));
            deck.add(new Card("In which year did the Titanic sink?", "1912"));
            deck.add(new Card("What is the square root of 64?", "8"));
            deck.add(new Card("What is the longest river in the world?", "Nile River"));
            return deck;
        }

        public static List<Card> getDailyStudySet(List<Card> deck, int size) {
            List<Card> shuffledDeck = new ArrayList<>(deck);
            Collections.shuffle(shuffledDeck);
            return shuffledDeck.subList(0, Math.min(size, shuffledDeck.size()));
        }
    }


