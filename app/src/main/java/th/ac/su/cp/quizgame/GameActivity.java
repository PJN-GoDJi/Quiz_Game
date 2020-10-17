package th.ac.su.cp.quizgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import th.ac.su.cp.quizgame.model.WordItem;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mQuestionImageView;
    private Button[] mButtons = new Button[4];

    private String mAnswerWord;
    private Random mRandom;
    private List<WordItem> mItemList;
    private int score=0,count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        /*WordItem[] items = WordListActivity.items;*/

        mQuestionImageView = findViewById(R.id.question_image_view);
        mButtons[0] = findViewById(R.id.choice_1_button);
        mButtons[1] = findViewById(R.id.choice_2_button);
        mButtons[2] = findViewById(R.id.choice_3_button);
        mButtons[3] = findViewById(R.id.choice_4_button);

        /*MyButtonListener listener = new MyButtonListener();*/

        mButtons[0].setOnClickListener(this);
        mButtons[1].setOnClickListener(this);
        mButtons[2].setOnClickListener(this);
        mButtons[3].setOnClickListener(this);


        mRandom = new Random();

            newQuiz(mItemList, mRandom);


    }

    private void newQuiz(List<WordItem> itemList, Random r) {
        mItemList = new ArrayList<>(Arrays.asList(WordListActivity.items));
        TextView scoreTextView = findViewById(R.id.score_text_view);
        scoreTextView.setText(score+" คะแนน");
        //สุ่ม Index ของคำศัพท์
        int answerIndex = r.nextInt(mItemList.size());
        //เข้าถึง WordItem ตาม Index ที่สุ่มได้
        WordItem item = mItemList.get(answerIndex);
        //แสดงรูปคำถาม
        mQuestionImageView.setImageResource(item.imageResId);
        //เก็บตัวแปรคำตอบที่ถูกต้อง
        mAnswerWord = item.word;
        //สุ่มปุ่มว่าจะเอาคำศัพท์ไปใส่ที่ปุ่มไหน
        int randomButton = r.nextInt(4);
        mButtons[randomButton].setText(item.word);
        //ลบ WordItem ที่เป็นคำตอบออกจาก List
        mItemList.remove(item);
        //นำ List ที่เหลือมา shuffle(สับไพ่)
        Collections.shuffle(mItemList);

        //นำ List มา Shuffle แล้วมาแสดงที่ปุ่ม 3 ปุ่ม ที่ไม่ใช่คำตอบ
        for (int i = 0; i < 4; i++) {
            if (i == randomButton) {
                continue;
            }
            mButtons[i].setText(mItemList.get(i).word);
        }
    }

    @Override
    public void onClick(View view) {
        Button b = findViewById(view.getId());
        String buttonText = b.getText().toString();
        TextView scoreTextView = findViewById(R.id.score_text_view);
        if (buttonText.equals(mAnswerWord)) {
            Toast.makeText(GameActivity.this, "ถูกต้องครับ", Toast.LENGTH_SHORT).show();
            score++;

        } else {
            Toast.makeText(GameActivity.this, "ผิดครับ", Toast.LENGTH_SHORT).show();

        }
    /*private static class MyButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

        }*/
        scoreTextView.setText(score+" คะแนน");
        if(count!=4){
            count++;
        }else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(GameActivity.this);
            dialog.setTitle("สรุปผล");
            dialog.setMessage("คุณได้ "+score+" คะแนน\nต้องการเล่นเกมใหม่หรือไม่");
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    score=0;
                    TextView scoreTextView = findViewById(R.id.score_text_view);
                    scoreTextView.setText(score+" คะแนน");
                    count=0;
                    newQuiz(mItemList, mRandom);
                }
            });
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            dialog.show();
        }

        newQuiz(mItemList, mRandom);
    }
}