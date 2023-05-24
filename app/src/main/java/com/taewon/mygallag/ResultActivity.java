package com.taewon.mygallag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) { // 액티비티 생성때 호출
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_dialog);//game_over_dialog레이아웃 파일을 액티비티에 연결
        init();
    }
    private void init(){
        findViewById(R.id.goMainBtn).setOnClickListener(new View.OnClickListener() { //goMainBtn버튼이 클릭되었을때 작동
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, StartActivity.class); //ResultActivity에서 StartActivity로 이동하기 위한 Intent 생성
                //ResultActivity.this는 현재 액티비티의 참조를 의미 StartActivity.class는 이동하고자 하는 대상 액티비티의 클래스 객체를 의미.
                startActivity(intent);//생선된 intent를 실행 startActivity로 이동
                finish();//종료
            }
        });
        ((TextView)findViewById(R.id.userFinalScoreText)).setText( //userFinalScoreText라는 텍스트뷰를 찾아서 setText로 해당 텍스트 뷰에 점수를 설정
                getIntent().getIntExtra("score", 0)+""); //이전 액티비티에서 score라는 키로 가져온 점수값을 가져옴
    }
}
