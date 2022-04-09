
package com.example.flashcard;

        import androidx.appcompat.app.AppCompatActivity;

        import android.animation.Animator;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.view.ViewAnimationUtils;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
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
                View answerSideView = findViewById(R.id.flashcard_a_textview);

                // get the center for the clipping circle
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;
                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

// hide the question and show the answer to prepare for playing the animation!
                answerText.setVisibility(View.VISIBLE);
                questionText.setVisibility(View.INVISIBLE);


                anim.setDuration(3000);
                anim.start();
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
                overridePendingTransition(R.anim.right_in, R.anim.left_in);
            }
        });
        //in oncreate because after oncreate we have context to give the constructor
        flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();

        if(allFlashcards != null && allFlashcards.size() > 0) {
            questionText.setText(allFlashcards.get(0).getQuestion());
            answerText.setText(allFlashcards.get(0).getAnswer());

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

                final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_in);
                final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        Flashcard currentCard = allFlashcards.get(cardIndex);
                        questionText.setText(currentCard.getQuestion());
                        answerText.setText(currentCard.getAnswer());
                        questionText.setVisibility(View.VISIBLE);
                        answerText.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

                questionText.startAnimation(leftOutAnim);
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
