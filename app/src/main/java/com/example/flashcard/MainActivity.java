
package com.example.flashcard;

        import androidx.appcompat.app.AppCompatActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;

        import com.google.android.material.snackbar.Snackbar;

        import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView questionText;
    TextView answerText;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int cardIndex =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.flashcard_q_textview);
        answerText = findViewById(R.id.flashcard_a_textview);
        questionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionText.setVisibility(View.INVISIBLE);
                answerText.setVisibility(View.VISIBLE);
            }});

        answerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerText.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.VISIBLE);
            }});

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCard.class);
                MainActivity.this.startActivityForResult(intent, 100);

            }
        });
        //in oncreate because after oncreate we have context to give the constructor
        flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();

        if(allFlashcards != null && allFlashcards.size() > 0) {
            Flashcard firstCard = allFlashcards.get(0);
            questionText.setText(allFlashcards.get(0).getQuestion());
            answerText.setText(allFlashcards.get(0).getAnswer());
//            questionText.setText(firstCard.getQuestion());
//            answerText.setText(firstCard.getQuestion());
        }

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allFlashcards == null || allFlashcards.size() == 0){
                    return;
                }
                cardIndex +=1;
               if(cardIndex >= allFlashcards.size()) {
                   Snackbar.make(view, "you've reached the end of cards.",Snackbar.LENGTH_SHORT).show();
                cardIndex = 0;
               }
                Flashcard currentCard = allFlashcards.get(cardIndex);
                questionText.setText(currentCard.getQuestion());
                answerText.setText(currentCard.getAnswer());
            }
        });
        findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flashcardDatabase.deleteCard(questionText.getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
                if (allFlashcards == null || allFlashcards.size() == 0) {
                    Intent intent = new Intent(MainActivity.this, Empty.class);
                    MainActivity.this.startActivity(intent);
                }
                else {
                    cardIndex -= 1;
                    Flashcard currentCard = allFlashcards.get(cardIndex);
                    questionText.setText(currentCard.getQuestion());
                    answerText.setText(currentCard.getAnswer());
                }
                }
            });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) { // this 100 needs to match the 100 we used when we called startActivityForResult!
            String newQuestion = data.getExtras().getString("q"); // 'string1' needs to match the key we used when we put the string in the Intent
            String newAnswer = data.getExtras().getString("a");
            questionText.setText(newQuestion);
            answerText.setText(newAnswer);

            Flashcard flashcard = new Flashcard(newQuestion,newAnswer);
            flashcardDatabase.insertCard(flashcard);

            allFlashcards = flashcardDatabase.getAllCards();
        }
    }
}
