package pl.edu.pb.wi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {

    // Deklaracja zmiennych składowych
    private boolean correctAnswer;
    Button showCurrentAnswerButton;
    TextView answerTextView;

    // Stała służąca jako klucz do przekazywania danych między aktywnościami
    public static final String KEY_EXTRA_ANSWER_SHOWN = "pb.edu.pl.wi.answerShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ustawienie widoku dla aktywności
        setContentView(R.layout.activity_prompt);

        // Inicjalizacja przycisku i pola tekstowego poprzez odniesienie do ich identyfikatorów w pliku XML
        showCurrentAnswerButton = findViewById(R.id.show_answer_button);
        answerTextView = findViewById(R.id.answer_text_view);

        // Pobieranie informacji przekazanej z głównej aktywności
        correctAnswer = getIntent().getBooleanExtra(MainActivity.KEY_EXTRA_ANSWER, true);

        // Ustawienie listenera dla przycisku, który pokazuje odpowiedź
        showCurrentAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wybór odpowiedniego tekstu w zależności od wartości correctAnswer
                int answer = correctAnswer ? R.string.button_true : R.string.button_false;
                answerTextView.setText(answer);

                // Informacja, że odpowiedź została pokazana
                setAnswerShownResult(true);
            }
        });
    }

    // Metoda do ustawiania wyniku aktywności
    private void setAnswerShownResult(boolean answerWasShown) {
        Intent resultIntent = new Intent();

        // Dodawanie informacji do intencji
        resultIntent.putExtra(KEY_EXTRA_ANSWER_SHOWN, answerWasShown);

        // Ustawianie wyniku aktywności
        setResult(RESULT_OK, resultIntent);
    }
}