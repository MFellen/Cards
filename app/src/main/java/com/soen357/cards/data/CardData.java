package com.soen357.cards.data;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.soen357.cards.MainActivity;
import com.soen357.cards.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardData {

    public static List<Card> getFullDeck() {
        List<Card> deck = new ArrayList<>();

        deck.add(new Card(
                createBoldText("What is the capital of France?", "France"),
                new SpannableString("Paris"),
                R.drawable.paris
        ));
        deck.add(new Card(
                new SpannableString("What is 2 + 2?"),
                new SpannableString("4")
        ));
        deck.add(new Card(
                new SpannableString("What is the largest planet in our solar system?"),
                new SpannableString("Jupiter"),
                R.drawable.jupiter
        ));
        deck.add(new Card(
                new SpannableString("Who wrote '1984'?"),
                new SpannableString("George Orwell")
        ));
        deck.add(new Card(
                createColouredText("What is the chemical symbol for water?", "water", Color.CYAN),
                new SpannableString("H2O")
        ));
        deck.add(new Card(
                createBoldText("Which element has the atomic number 1?", "1"),
                new SpannableString("Hydrogen")
        ));
        deck.add(new Card(
                createBoldText("What is the capital of Japan?", "Japan"),
                new SpannableString("Tokyo"),
                R.drawable.tokyo
        ));
        deck.add(new Card(
                new SpannableString("Who painted the Mona Lisa?"),
                new SpannableString("Leonardo da Vinci"),
                R.drawable.monalisa
        ));
        deck.add(new Card(
                createBoldText("What is the fastest land animal?", "fastest"),
                createColouredText("Cheetah", "Cheetah", Color.YELLOW)
        ));
        deck.add(new Card(
                new SpannableString("How many continents are there on Earth?"),
                new SpannableString("7")
        ));
        deck.add(new Card(
                createBoldText("What is the capital of Australia?", "Australia"),
                new SpannableString("Canberra")
        ));
        deck.add(new Card(
                createColouredText("Which planet is known as the Red Planet?", "Red Planet", Color.RED),
                new SpannableString("Mars"),
                R.drawable.mars
        ));
        deck.add(new Card(
                createColouredText("What is the smallest prime number?", "prime", Color.RED),
                new SpannableString("2")
        ));
        deck.add(new Card(
                createColouredText("Who discovered penicillin?", "penicillin", Color.BLUE),
                new SpannableString("Alexander Fleming")
        ));
        deck.add(new Card(
                createColouredText("What is the tallest mountain in the world?", "mountain", Color.rgb(165, 42, 42)), // Brown Colour
                createBoldText("Mount Everest ⛰️ in Asia", "Everest")
        ));
        deck.add(new Card(
                createBoldText("Which gas do plants use for photosynthesis?", "plants"),
                new SpannableString("Carbon Dioxide")
        ));
        deck.add(new Card(
                createColouredText("What is the largest ocean on Earth?", "ocean", Color.CYAN),
                createBoldText("Pacific Ocean \uD83C\uDF0A", "Pacific"),
                R.drawable.pacificocean
        ));
        deck.add(new Card(
                createBoldText("In which year did the Titanic sink?", "Titanic"),
                new SpannableString("1912")
        ));
        deck.add(new Card(
                new SpannableString("What is the square root of 64?"),
                new SpannableString("8")
        ));
        deck.add(new Card(
                createColouredText("What is the longest river in the world?", "river", Color.CYAN),
                new SpannableString("Nile River")
        ));

        return deck;
    }

    private static SpannableString createColouredText(String fullText, String textToColour, int colour) {
        SpannableString string = new SpannableString(fullText);
        int start = fullText.indexOf(textToColour);
        int end = start + textToColour.length();
        string.setSpan(new ForegroundColorSpan(colour), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);

        return string;
    }

    private static SpannableString createBoldText(String fullText, String textToBold) {
        SpannableString string = new SpannableString(fullText);
        int start = fullText.indexOf(textToBold);
        int end = start + textToBold.length();
        string.setSpan(new StyleSpan(Typeface.BOLD), start, end, SPAN_EXCLUSIVE_EXCLUSIVE);

        return string;
    }


    public static List<Card> generateStudySet() {
        // Prioritize incorrect answers
        List<Card> incorrectCards = new ArrayList<>();
        List<Card> remainingCards = new ArrayList<>();
        List<Card> fullDeck = getFullDeck();

        for (Card card : fullDeck) {
            if (card.getIncorrectCount() > 0) {
                incorrectCards.add(card);
            } else {
                remainingCards.add(card);
            }
        }

        // Shuffle incorrect cards and remaining cards
        Collections.shuffle(incorrectCards);
        Collections.shuffle(remainingCards);

        // Combine the sets, prioritizing incorrect cards
        List<Card> studySet = new ArrayList<>(incorrectCards);
        studySet.addAll(remainingCards);

        // Limit study set size to 10 cards
        return studySet.subList(0, Math.min(10, studySet.size()));
        }
    }


