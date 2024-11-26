package com.soen357.cards;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.soen357.cards.data.Card;
import com.soen357.cards.data.CardData;
import com.soen357.cards.logic.StudyManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionText;
    private TextView answerText;
    private TextView progressText;
    private TextView scoreText;
    private ImageView cardImage;
    private Button showAnswerButton;
    private Button correctButton;
    private Button incorrectButton;
    private StudyManager studyManager;
    private GestureDetectorCompat gestureDetector;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        progressText = findViewById(R.id.progressText);
        scoreText = findViewById(R.id.scoreText);
        cardImage = findViewById(R.id.cardImage);
        showAnswerButton = findViewById(R.id.showAnswerButton);
        correctButton = findViewById(R.id.correctButton);
        incorrectButton = findViewById(R.id.incorrectButton);
        calendarView = findViewById(R.id.calendarView);

        // Set up date selection listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            Toast.makeText(MainActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            // Add logic to track progress or show specific content for the selected date
        });

        // Initialize study manager
        List<Card> fullDeck = CardData.getFullDeck();
        studyManager = new StudyManager(fullDeck);

        // Gesture detector for swipe navigation
        gestureDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(velocityX)) {
                    if (diffX > 0) {
                        studyManager.moveToPreviousCard();
                    } else {
                        studyManager.moveToNextCard();
                    }
                    updateUI();
                }
                return true;
            }
        });

        // Button listeners
        showAnswerButton.setOnClickListener(v -> {
            answerText.setText(studyManager.getCurrentCard().getAnswer());
            answerText.setVisibility(View.VISIBLE);
        });

        correctButton.setOnClickListener(v -> {
            studyManager.markAnswer(true);
            updateUI();
        });

        incorrectButton.setOnClickListener(v -> {
            studyManager.markAnswer(false);
            updateUI();
        });

        // Initial UI update
        updateUI();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private void updateUI() {
        if (studyManager.isStudyComplete()) {
            questionText.setText("You've completed today's study set!");
            answerText.setVisibility(View.GONE);
            showAnswerButton.setVisibility(View.GONE);
            correctButton.setVisibility(View.GONE);
            incorrectButton.setVisibility(View.GONE);
            cardImage.setVisibility(View.GONE);
        } else {
            Card currentCard = studyManager.getCurrentCard();
            questionText.setText(currentCard.getQuestion());
            answerText.setVisibility(View.GONE);

            if (currentCard.hasImage()) {
                cardImage.setImageResource(currentCard.getImageResId());
                cardImage.setVisibility(View.VISIBLE);
            } else {
                cardImage.setVisibility(View.GONE);
            }

            progressText.setText(studyManager.getProgress());
            scoreText.setText(studyManager.getScore());
        }
    }
}