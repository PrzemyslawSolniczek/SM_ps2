package pl.edu.pb.wi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    // Stałe używane do przekazywania danych między aktywnościami oraz do logowania
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.correctAnswer";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    private static final int REQUEST_CODE_PROMPT =0;
    private static final String QUIZ_TAG = "MojTag";

    // Zmienne składowe do śledzenia postępów w quizie
    private boolean answerWasShown;
    private int currentIndex = 0;
    private int correctAnswersCount = 0;
    private int currentQuestionIndex = 0;

    // Stała do opóźnienia resetowania quizu
    private static final int RESET_DELAY = 4000;

    // Elementy interfejsu użytkownika
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;

    // Tablica pytań dla quizu
    private Question[] questions = new Question[]{
            new Question(R.string.q_1, true),
            new Question(R.string.q_2, true),
            new Question(R.string.q_3, true),
            new Question(R.string.q_4, true),
            new Question(R.string.q_5, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Mojtag","Wywołanie onCreate");
        setContentView(R.layout.activity_main);

        // Inicjalizacja elementów interfejsu
        questionTextView = findViewById(R.id.question_text_view);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        promptButton = findViewById(R.id.prompt_button);

        // Wyświetlenie bieżącego pytania
        showCurrentQuestion();

        // Ustawienie listenerow dla przycisków
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswerCorrectness(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Przejście do następnego pytania
                currentQuestionIndex = (currentQuestionIndex + 1) % questions.length;
                showCurrentQuestion();
            }
        });

        // Ustawienie listenera dla przycisku podpowiedzi
        promptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromptActivity.class);
                boolean correctAnswer = questions[currentIndex].getTrueAnswer();
                intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
                startActivityForResult(intent, REQUEST_CODE_PROMPT);
            }
        });
    }

    // Metoda do wyświetlania bieżącego pytania
    private void setNextQuestion() {
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    // Metody cyklu życia aktywności (tutaj tylko do logowania)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Mojtag","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Mojtag","onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Mojtag","onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Mojtag","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Mojtag","onStart");
    }

    // Zapisywanie stanu aktywności
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(QUIZ_TAG, "onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    // Klasa reprezentująca pytanie
    public class Question {
        private int questionId;
        private boolean trueAnswer;

        public Question(int questionId, boolean trueAnswer) {
            this.questionId = questionId;
            this.trueAnswer = trueAnswer;
        }

        public int getQuestionId() {
            return questionId;
        }

        public boolean getTrueAnswer() {
            return trueAnswer;
        }
    }

    // Sprawdzenie poprawności odpowiedzi
    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentQuestionIndex].getTrueAnswer();
        int messageId;
        if (answerWasShown){
            messageId = R.string.answer_was_shown;
        } else {
            if (userAnswer == correctAnswer) {
                messageId = R.string.correct_answer;
                correctAnswersCount++;
            } else {
                messageId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();

        // Wyświetlenie wyniku po zakończeniu quizu
        if (currentQuestionIndex == questions.length - 1) {
            displayFinalScore();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetGame();
                }
            }, RESET_DELAY);
        }
    }

    // Wyświetlenie końcowego wyniku
    private void displayFinalScore() {
        String message = String.format("Uzyskałeś %d z %d poprawnych odpowiedzi!", correctAnswersCount, questions.length);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // Wyświetlenie bieżącego pytania
    private void showCurrentQuestion() {
        questionTextView.setText(questions[currentQuestionIndex].getQuestionId());
        currentIndex = currentQuestionIndex;
        answerWasShown = false;
    }

    // Resetowanie quizu
    private void resetGame() {
        currentQuestionIndex = 0;
        correctAnswersCount = 0;
        showCurrentQuestion();
    }

    // Odbiór wyniku z PromptActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null)
                return;
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }
}