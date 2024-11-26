package com.soen357.cards.data;

public class Card {
    private String question;
    private String answer;
    private int imageResId; // Resource ID of the image
    private int correctCount;
    private int incorrectCount;

    // Constructor for cards without images
    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.imageResId = -1; // No image
        this.correctCount = 0;
        this.incorrectCount = 0;
    }

    // Constructor for cards with images
    public Card(String question, String answer, int imageResId) {
        this.question = question;
        this.answer = answer;
        this.imageResId = imageResId;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean hasImage() {
        return imageResId != -1;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void incrementCorrectCount() {
        correctCount++;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public void incrementIncorrectCount() {
        incorrectCount++;
    }
}
