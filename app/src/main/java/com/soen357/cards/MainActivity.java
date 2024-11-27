package com.soen357.cards;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.soen357.cards.ui.EventDecorator;
import com.soen357.cards.R;
import com.soen357.cards.data.Card;
import com.soen357.cards.data.CardData;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView questionText, answerText;
    private ImageView cardImage;
    private Button showAnswerButton, correctButton, incorrectButton;

    private List<Card> currentStudySet;
    private int currentCardIndex = 0;

    private HashMap<String, Boolean> completedDates; // Tracks completion status by date
    private String todayDate;
    private String currentStudyDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        cardImage = findViewById(R.id.cardImage);
        showAnswerButton = findViewById(R.id.showAnswerButton);
        correctButton = findViewById(R.id.correctButton);
        incorrectButton = findViewById(R.id.incorrectButton);

        completedDates = new HashMap<>();
        todayDate = getTodayDate();
        currentStudyDate = todayDate;

        // Highlight dates
        highlightDates();

        // Check if today's study set is complete
        if (isDailyStudyComplete(todayDate)) {
            showCompletionMessage();
        } else {
            currentStudySet = CardData.generateStudySet();
            showCard();
        }

        // Show answer button functionality
        showAnswerButton.setOnClickListener(v -> {
            Card currentCard = currentStudySet.get(currentCardIndex);
            answerText.setText(currentCard.getAnswer());
            answerText.setVisibility(View.VISIBLE);
        });

        // Correct button functionality
        correctButton.setOnClickListener(v -> {
            currentStudySet.get(currentCardIndex).incrementCorrectCount();
            moveToNextCard();
        });

        // Incorrect button functionality
        incorrectButton.setOnClickListener(v -> {
            currentStudySet.get(currentCardIndex).incrementIncorrectCount();
            moveToNextCard();
        });

        // Calendar selection functionality
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            String selectedDate = date.getDay() + "/" + (date.getMonth() + 1) + "/" + date.getYear();
            if (!selectedDate.equals(currentStudyDate)) {
                // Load the study set for the selected date
                currentStudyDate = selectedDate;
                loadStudySetForDate(selectedDate);
            }
        });
    }

    private void moveToNextCard() {
        currentCardIndex++;
        if (currentCardIndex >= currentStudySet.size()) {
            markDailyStudyComplete(currentStudyDate);
            showCompletionMessage();
        } else {
            showCard();
        }
    }


    private void showCard() {
        if (currentStudySet.isEmpty()) {
            questionText.setText("No cards available.");
            answerText.setVisibility(View.GONE);
            cardImage.setVisibility(View.GONE);
            showAnswerButton.setVisibility(View.GONE);
            correctButton.setVisibility(View.GONE);
            incorrectButton.setVisibility(View.GONE);
            return;
        }

        Card currentCard = currentStudySet.get(currentCardIndex);
        questionText.setText(currentCard.getQuestion());
        answerText.setVisibility(View.GONE);

        if (currentCard.getImageResId() != -1) {
            cardImage.setImageResource(currentCard.getImageResId());
            cardImage.setVisibility(View.VISIBLE);
        } else {
            cardImage.setVisibility(View.GONE);
        }
    }

    private boolean isDailyStudyComplete(String date) {
        return Boolean.TRUE.equals(completedDates.get(date));
    }


    private void markDailyStudyComplete(String date) {
        completedDates.put(date, true);
    }


    private void showCompletionMessage() {
        questionText.setText("Daily study set complete! Come back tomorrow.");
        showAnswerButton.setVisibility(View.GONE);
        correctButton.setVisibility(View.GONE);
        incorrectButton.setVisibility(View.GONE);
    }

    private void loadStudySetForDate(String date) {
        if (isDailyStudyComplete(date)) {
            showCompletionMessage();
        } else {
            currentStudySet = CardData.generateStudySet(); // Generate or fetch study set for the date
            currentCardIndex = 0; // Reset progress
            resetUIForStudySession();
            showCard();
        }
    }

    private void resetUIForStudySession() {
        questionText.setText(""); // Clear the question text
        answerText.setVisibility(View.GONE); // Hide the answer initially
        cardImage.setVisibility(View.GONE); // Hide the card image until necessary
        showAnswerButton.setVisibility(View.VISIBLE);
        correctButton.setVisibility(View.VISIBLE);
        incorrectButton.setVisibility(View.VISIBLE);
    }


    private void highlightDates() {
        CalendarDay today = CalendarDay.today();
        calendarView.addDecorator(new EventDecorator(Color.BLUE, Collections.singleton(today)));

        HashSet<CalendarDay> completed = new HashSet<>();
        for (String date : completedDates.keySet()) {
            if (completedDates.get(date)) {
                completed.add(parseDate(date));
            }
        }
        calendarView.addDecorator(new EventDecorator(Color.GREEN, completed));

        HashSet<CalendarDay> uncompleted = new HashSet<>();
        for (String date : completedDates.keySet()) {
            if (!completedDates.get(date)) {
                uncompleted.add(parseDate(date));
            }
        }
        calendarView.addDecorator(new EventDecorator(Color.RED, uncompleted));
    }

    private CalendarDay parseDate(String date) {
        try {
            return CalendarDay.from(new SimpleDateFormat("dd/MM/yyyy").parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getTodayDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }
}
