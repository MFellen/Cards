package com.soen357.cards.logic;

import com.soen357.cards.data.Card;
import com.soen357.cards.data.CardData;

import java.util.List;

public class StudyManager {
    private final List<Card> dailyStudySet;
    private int currentCardIndex = 0;
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;

    public StudyManager(List<Card> fullDeck) {
        this.dailyStudySet = CardData.getDailyStudySet(fullDeck, 10);
    }

    public Card getCurrentCard() {
        return dailyStudySet.get(currentCardIndex);
    }

    public boolean isStudyComplete() {
        return currentCardIndex >= dailyStudySet.size();
    }

    public void markAnswer(boolean correct) {
        if (correct) {
            correctAnswers++;
        } else {
            incorrectAnswers++;
        }
        moveToNextCard();
    }

    public void moveToNextCard() {
        if (!isStudyComplete()) {
            currentCardIndex++;
        }
    }

    public void moveToPreviousCard() {
        if (currentCardIndex > 0) {
            currentCardIndex--;
        }
    }

    public String getProgress() {
        return currentCardIndex + "/" + dailyStudySet.size() + " cards completed";
    }

    public String getScore() {
        return "Correct: " + correctAnswers + " | Incorrect: " + incorrectAnswers;
    }
}
