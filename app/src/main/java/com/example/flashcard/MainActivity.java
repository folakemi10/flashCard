
package com.example.flashcard;

        import androidx.appcompat.app.AppCompatActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView questionText = findViewById(R.id.flashcard_q_textview);
        TextView answerText = findViewById(R.id.flashcard_a_textview);
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


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) { // this 100 needs to match the 100 we used when we called startActivityForResult!
            String newQuestion = data.getExtras().getString("q"); // 'string1' needs to match the key we used when we put the string in the Intent
            String newAnswer = data.getExtras().getString("a");
            ((TextView) findViewById(R.id.flashcard_q_textview)).setText(newQuestion);
            ((TextView) findViewById(R.id.flashcard_a_textview)).setText(newAnswer);
        }
    }
}
