package com.taewon.mygallag;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    private Intent userIntent; //보낸 인텐트를 userIntent로 잠깐 받아와서 밑에서 사용
    ArrayList<Integer> bgMusicList;
    public static SoundPool effectSound; //볼륨조절
    public static float effectVolumn;
    ImageButton specialShotBtn;
    public static ImageButton fireBtn, reloadBtn;
    JoystickView joyStick;
    public static TextView scoreTv;
    LinearLayout gameFrame; //액티비티 메인의 LinearLayout의 이름이 gameFrame

    ImageView pauseBtn;
    public static LinearLayout lifeFrame;
    SpaceInvadersView spaceInvadersView;
    public static MediaPlayer bgMusic;
    int bgMusicIndex;
    public static TextView bulletCount;
    private static ArrayList<Integer> effectSoundList;
    public static final int PLAYER_SHOT = 0;
    public static final int PLAYER_HURT = 1;
    public static final int PLAYER_RELOAD = 2;
    public static final int PLAYER_GET_ITEM = 3;


    @Override //생성자
    protected void onCreate(Bundle savedInstanceState) { //Activity가 생성될때 실행됨
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userIntent = getIntent(); //start에서 보낸 getIntent로 인텐트 받아옴
        bgMusicIndex = 0;  //index 0부터시작
        bgMusicList = new ArrayList<Integer>(); //음악넣을 arrylist생성
        bgMusicList.add(R.raw.main_game_bgm1); //음악1
        bgMusicList.add(R.raw.main_game_bgm2); //음악2
        bgMusicList.add(R.raw.main_game_bgm3); //음악3


        effectSound = new SoundPool(5, AudioManager.USE_DEFAULT_STREAM_TYPE, 0);
        effectVolumn = 1;

        specialShotBtn = findViewById(R.id.specialShotBtn); //번개
        joyStick = findViewById(R.id.joyStick);
        scoreTv = findViewById(R.id.score); //TextView
        fireBtn = findViewById(R.id.fireBtn); //ImageButton
        reloadBtn = findViewById(R.id.reloadBtn);//ImageButton
        gameFrame = findViewById(R.id.gameFrame); //첫번째 LinearLayout
        pauseBtn = findViewById(R.id.pauseBtn);//ImageButton
        lifeFrame = findViewById(R.id.lifeFrame);//윗부분 LinearLayout

        init(); //시스템 실행시 필요한 여러가지 작업 ex)화면 넓이랑 높이 구하는 거드,bgm 등등
        setBtnBehavior();  //조이스틱들 작동함수 실행
    }

    @Override
    protected void onResume() { //onResume을 통해 액티비티 메인에 음악 적용시킴 //생명주기 중 하나
        super.onResume();
        bgMusic.start(); //bgm스타트
        spaceInvadersView.resume(); // 액티비티 위에 덮어서 그리는 느낌
    }

    private void init() { //시스템 실행시 필요한 여러가지 작업
        Display display = getWindowManager().getDefaultDisplay();  //view의 display를 얻어와 화면 넓이랑 높이 구하는 거
        Point size = new Point();
        display.getSize(size);
        //??? 아이템 화면에 띄우기
        spaceInvadersView = new SpaceInvadersView(this, userIntent.getIntExtra("character", R.drawable.ship_0000), size.x, size.y);
        //여기서 userIntent로 spaceInvadersView한테 고른 캐릭터를 가져다줌
        //spaceInvadersView 각각의 아이템들을 구현하는view
        gameFrame.addView(spaceInvadersView); //gameFrame에 만든 아이템들 넣기 //spaceInvadersView를 gameFrame에 올리기
        //음악 바꾸기
        changeBgMusic();
        bgMusic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //음악이 끝나면 동작하는 리스너
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                changeBgMusic();
            } //changeBgMusic에 넣기
        });

        bulletCount = findViewById(R.id.bulletCount);   //총알 개수

        // spaceInvadersView의 getPlayer() 구현
        bulletCount.setText(spaceInvadersView.getPlayer().getBulletsCount() + "/30");  //Textview로 총알 갯수 30/30표시
        scoreTv.setText(Integer.toString(spaceInvadersView.getScore())); //Textview로 score:0 표시

        effectSoundList = new ArrayList<>();//effectSoundList배열로 총쏠때,다쳣을때,재장전할때,아이템을 얻엇을때 소리
        effectSoundList.add(PLAYER_SHOT, effectSound.load(MainActivity.this, R.raw.player_shot_sound, 1));
        effectSoundList.add(PLAYER_HURT, effectSound.load(MainActivity.this, R.raw.player_hurt_sound, 1));
        effectSoundList.add(PLAYER_RELOAD, effectSound.load(MainActivity.this, R.raw.reload_sound, 1));
        effectSoundList.add(PLAYER_GET_ITEM, effectSound.load(MainActivity.this, R.raw.player_get_item_sound, 1));
        bgMusic.start();  //음악 재생
    }

    private void changeBgMusic() {
        bgMusic = MediaPlayer.create(this, bgMusicList.get(bgMusicIndex)); //bgMusicList(bgMusicIndex)의 음악하나 가져와 만들기
        bgMusic.start();
        bgMusicIndex++; //음악 바꾸기 위해 미리 증가해 놓기
        bgMusicIndex = bgMusicIndex % bgMusicList.size(); //음악 개수만큼만 바뀌게 하기 위해
    }

    @Override
    protected void onPause() {   //일시 정지시
        super.onPause();
        bgMusic.pause();
        spaceInvadersView.pause();
    }

    public static void effectSound(int flag) {
        effectSound.play(effectSoundList.get(flag), effectVolumn, effectVolumn, 0, 0, 1.0f);
        //Soundpool 실행
    }

    private void setBtnBehavior() {
        joyStick.setAutoReCenterButton(true);//조이스틱 자동 중간으로
        joyStick.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("keycode", Integer.toString((i)));
                return false;
            }
        });

        //조이스틱 이동방향으로 비행기 이동하게 한다
        joyStick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d("angle", Integer.toString(angle));
                Log.d("force", Integer.toString(strength));
                if (angle > 67.5 && angle < 112.5) {
                    //위
                    spaceInvadersView.getPlayer().moveUp(strength / 10);
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle > 247.5 && angle < 292.5) {
                    //아래
                    spaceInvadersView.getPlayer().moveDown(strength / 10);
                    spaceInvadersView.getPlayer().resetDx();
                } else if (angle > 112.5 && angle < 157.5) {
                    //왼쪽 대각선 위
                    spaceInvadersView.getPlayer().moveUp(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveLeft(strength / 10 * 0.5);
                } else if (angle > 157.5 && angle < 202.5) {
                    //왼쪽
                    spaceInvadersView.getPlayer().moveLeft(strength / 10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle > 202.5 && angle < 247.5) {
                    //왼쪽 대각선 아래
                    spaceInvadersView.getPlayer().moveLeft(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength / 10 * 0.5);
                } else if (angle > 22.5 && angle < 67.5) {
                    //왼쪽 대각선 위
                    spaceInvadersView.getPlayer().moveUp(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveRight(strength / 10 * 0.5);
                } else if (angle > 337.5 || angle < 22.5) {
                    //오른쪽
                    spaceInvadersView.getPlayer().moveRight(strength / 10);
                    spaceInvadersView.getPlayer().resetDy();
                } else if (angle > 292.5 && angle < 337.5) {
                    //오른쪽 아래
                    spaceInvadersView.getPlayer().moveRight(strength / 10 * 0.5);
                    spaceInvadersView.getPlayer().moveDown(strength / 10 * 0.5);
                }
            }
        });
        // 총알 버튼 눌렀을 때
    fireBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            spaceInvadersView.getPlayer().fire();
        }//눌렸을때 플레이어가 총알 발사
        //SpaceInvadersView->StarshipSprite -> fire()
    });
        // 재장전 버튼 눌렀을 때
    reloadBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            spaceInvadersView.getPlayer().reloadBullets();//눌렸을때 플레이어가 총알 재장전
            //SpaceInvadersView->StarshipSprite -> reloadButtlets()
        }
    });
        // 정지 버튼 눌렀을 때
    pauseBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            spaceInvadersView.pause();  //spaceInvadersView 일시정지
            PauseDialog pauseDialog = new PauseDialog(MainActivity.this);
            pauseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) { //??
                    spaceInvadersView.resume(); //spaceInvadersView 종료
                }
            });
            pauseDialog.show();//pauseDialog 띄움
        }
    });

    specialShotBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(spaceInvadersView.getPlayer().getSpecialShotCount() >= 0) //SpecialShotCount가 0보다 클거나 같을때 스페셜샷
                spaceInvadersView.getPlayer().specialShot();
            }
        });
    }
}