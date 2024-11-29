package com.soen357.cards;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.soen357.cards.ui.EventDecorator;
import com.soen357.cards.data.Card;
import com.soen357.cards.data.CardData;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends Activity {

    private MaterialCalendarView calendarView;
    private TextView questionText, answerText;
    private ImageView cardImage;
    private Button showCalendarButton, showAnswerButton, correctButton, incorrectButton;

    private List<Card> currentStudySet;
    private int currentCardIndex = 0;

    private HashMap<String, Boolean> completedDates; // Tracks completion status by date
    private String todayDate;
    private String currentStudyDate;
    private boolean isCalendarVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        showCalendarButton = findViewById(R.id.showCalendarButton);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setVisibility(View.GONE);
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

        showCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCalendarVisible = !isCalendarVisible; // Toggle visibility state

                if (isCalendarVisible) {
                    calendarView.setVisibility(View.VISIBLE); // Show
                } else {
                    calendarView.setVisibility(View.GONE); // Hide
                }
            }
        });

        // Check if today's study set is complete
        if (isDailyStudyComplete(todayDate)) {
            showCompletionMessage();
        } else {
            currentStudySet = CardData.generateStudySet();
            showCard();
        }

        // Show answer button functionality
        showAnswerButton.setOnClickListener(v -> {
            showAnswerButton.setVisibility(View.GONE); // Hide
            Card currentCard = currentStudySet.get(currentCardIndex);
            answerText.setText(currentCard.getAnswer());
            answerText.setVisibility(View.VISIBLE); // Show
            correctButton.setVisibility(View.VISIBLE);
            incorrectButton.setVisibility(View.VISIBLE);
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
            calendarView.removeDecorators();
            highlightDates();
            if (!selectedDate.equals(currentStudyDate)) {
                // Load the study set for the selected date
                currentStudyDate = selectedDate;
                loadStudySetForDate(selectedDate);
            }
        });
    }

    private void moveToNextCard() {
        currentCardIndex++;
        showAnswerButton.setVisibility(View.VISIBLE);
        answerText.setVisibility(View.GONE);
        correctButton.setVisibility(View.GONE);
        incorrectButton.setVisibility(View.GONE);
        if (currentCardIndex >= currentStudySet.size()) {
            markDailyStudyComplete(currentStudyDate);
            highlightDates();
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
        CalendarDay selectedDate = calendarView.getSelectedDate();
        CalendarDay today = CalendarDay.today();
        if (selectedDate.equals(today)) {
            questionText.setText("Daily study set complete! Come back tomorrow.");
        }
        else {
            questionText.setText("You've completed this date's study set!");
        }
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
        correctButton.setVisibility(View.GONE);
        incorrectButton.setVisibility(View.GONE);
    }


    private void highlightDates() {
        CalendarDay selectedDate = calendarView.getSelectedDate();
        CalendarDay today = CalendarDay.today();
        // Set today as BLUE until it's done
        if (!today.equals(selectedDate)) {
            int blue = ContextCompat.getColor(this, R.color.blue);
            calendarView.addDecorator(new EventDecorator(blue, Collections.singleton(today)));
        }

        // Set completed dates as GREEN
        HashSet<CalendarDay> completed = new HashSet<>();
        for (String date : completedDates.keySet()) {
            CalendarDay calendarDay = parseDate(date);
            if (completedDates.get(date) && !calendarDay.equals(selectedDate)) {
                completed.add(parseDate(date));
            }
        }
        int green = ContextCompat.getColor(this, R.color.green);
        calendarView.addDecorator(new EventDecorator(green, completed));

        // Set uncompleted dates as RED
        HashSet<CalendarDay> uncompleted = new HashSet<>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        // This marks the last 4 days as uncompleted by default
        for (int i = 1; i <= 4; i++) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, -1);
            CalendarDay pastDay = CalendarDay.from(calendar);
            String dateString = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
            if (!completedDates.containsKey(dateString) && !pastDay.equals(selectedDate)) {
                uncompleted.add(pastDay);
            }
        }
        int red = ContextCompat.getColor(this, R.color.red);
        calendarView.addDecorator(new EventDecorator(red, uncompleted));
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.equals(today);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(5, Color.WHITE));
            }
        });
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
