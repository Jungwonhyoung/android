package com.taewon.mygallag;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    int characterId, effectId;
    ImageButton startBtn;
    TextView guideTv;
    MediaPlayer mediaPlayer;
    ImageView imgView[] = new ImageView[8];
    Integer img_id[] = {R.id.ship_001, R.id.ship_002, R.id.ship_003, R.id.ship_004, R.id.ship_005, R.id.ship_006, R.id.ship_007, R.id.ship_008};
    Integer img[] = {R.drawable.ship_0000, R.drawable.ship_0001, R.drawable.ship_0002, R.drawable.ship_0003, R.drawable.ship_0004, R.drawable.ship_0005, R.drawable.ship_0006, R.drawable.ship_0007};
    SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mediaPlayer = MediaPlayer.create(this, R.raw.robby_bgm); //mediaPlayer이란 클래스를 통해 로비 배경음악 생성
        mediaPlayer.setLooping(true);
        mediaPlayer.start(); //배경음악 시작 명령
        soundPool = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE, 0); //소리 높낮의 조절
        effectId = soundPool.load(this, R.raw.reload_sound, 1);
        startBtn = findViewById(R.id.startBtn); // 시작 버튼
        guideTv = findViewById(R.id.guideTv);   // 마지막 TextView

        for (int i = 0; i < imgView.length; i++) { //이미지뷰 길이만큼 넣어서
            imgView[i] = findViewById(img_id[i]);
            int index = i;  // 선택한 이미지 번호 알기
            imgView[i].setOnClickListener(new View.OnClickListener() { //비행기 8개를 선택 seonclicklistner for문으로 묶음
                @Override
                public void onClick(View view) {
                    characterId = img[index];
                    startBtn.setVisibility(View.VISIBLE);
                    startBtn.setEnabled(true);
                    startBtn.setImageResource(characterId); // 버튼에 선택한 이미지 넣기
                    guideTv.setVisibility(View.INVISIBLE);  // 마지막 TextView 숨기기
                    soundPool.play(effectId, 1, 1, 0, 0, 1.0f); // 소리 재생
                }
            });
        }
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT); //화면 세로로 돌리기
        init();
    }

    private void init() {
        findViewById(R.id.startBtn).setVisibility(View.GONE);   // 버튼 위치는 남겨 두고 숨기기
        findViewById(R.id.startBtn).setEnabled(false);  // 선택 안되게 하기
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class); //들어간 그림 onclick으로 선택 가능하게 됨
                intent.putExtra("character", characterId);  // 선택한 이미지 넘기기
                startActivity(intent); //메인으로 선택한 캐릭터 넘기기
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() { //Activity가 소멸되기 직전에 호출됨, mediaPlayer이 살아있으면 리소스를 소멸시킨다 //첫화면에 나오는 음악 끊기
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); //미디어플레이어를 소멸시키는 작업
            mediaPlayer = null;
        }
    }
}