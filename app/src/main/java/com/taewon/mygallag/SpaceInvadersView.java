package com.taewon.mygallag;

//Sprite  움직이는 2차원 비트맵 개체를 가리키는 용어

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.taewon.mygallag.sprites.AlienSprite;
import com.taewon.mygallag.sprites.Sprite;
import com.taewon.mygallag.sprites.StarshipSprite;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
//spaceInvadersView 각각의 아이템들을 구현하는view

//SurfaceView는 안드로이드에서 화면 그리기 작업을 수행하는 뷰의 하위 클래스

public class SpaceInvadersView extends SurfaceView implements Runnable, SurfaceHolder.Callback {  //??
    //SurfaceView 는 스레드를 이용해 강제로 화면에 그려주므로 View보다 빠르다.애니메이션, 영상 처리에 이용
    //SurfaceHolder.Callback Surface의 변화감지를 위해 필요. 지금처럼 SurfaceView와 거의 같이 사용한다.

    private static int MAX_ENEMY_COUNT = 10;
    private Context context;
    private int characterId;
    private SurfaceHolder ourHolder; //화면에 그리는데 View보다 빠르게 그려준다
    private Paint paint;
    public int screenW, screenH;
    private Rect src, dst; //사각형 그리는 클래스
    private ArrayList sprites = new ArrayList();
    private Sprite starship;
    private int score, currEnemyCount;
    private Thread gameThread = null;
    private volatile boolean running; //휘발성 부울 함수
    private Canvas canvas;
    int mapBitmapY = 0;

    public SpaceInvadersView(Context context, int characterId, int x, int y) {  //MainActivity, Intent(몬스터), point x,y가 넘어온다 //SpaceInvadersView의 생성자
        super(context); //상위 클래스인 SurfaceView의 생성자를 호출
        this.context = context; //생성자의 context를 context에 저장
        this.characterId = characterId;
        ourHolder = getHolder();  //현재 SurfaceView를 리턴받는다.
        paint = new Paint(); //그리기 작업에 사용
        screenW = x;
        screenH = y;  //받아온 x, y
        src = new Rect();  //원본 사각형
        dst = new Rect();  //사본 사각형
        dst.set(0, 0, screenW, screenH); //시작 x,y와 끝 x,y
        startGame();//startGame 메소드 호출
    }

    //sprites 39번쨰 줄
    //private ArrayList sprites = new ArrayList();
    private void startGame() {
        sprites.clear();  //ArrayList 비우기
        initSprites();  //initSprites메소드 실행으로 ArrayList에 침략자 아이템들 추가하기
        score = 0; //스코어는 0부터 시작
    }


    //context는 안드로이드 앱에서 현재 활성화된 컨텍스트(즉, 액티비티)를 나타내는 객체
    public void endGame(){
        Log.e("GmaeOver", "GameOver");
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtra("score", score); //putExtra를 통해서 score라는 이름의 데이터를 넣음
        context.startActivity(intent);//생성한 인텐트로 새로운 액티비티 실행
        gameThread.stop();//게임스레드 중지
    }
    //Sprite 객체를 받아서 sprites 리스트에서 해당 스프라이트를 제거함
    public void removeSprite(Sprite sprite) { sprites.remove(sprite); }


    private void initSprites() {  //sprite 초기화
        starship = new StarshipSprite(context, this, characterId, screenW / 2, screenH - 400, 1.5f);//StartshipSprite생성 아이템들 생성
        sprites.add(starship); //ArrayList에 시작 아이템 추가
        spawnEnemy();
        spawnEnemy();
    }

    //sprites 39번쨰 줄
    //private ArrayList sprites = new ArrayList();
    public void spawnEnemy() { //몬스터 생성
        Random r = new Random(); //랜덤 클래스로 랜덤 값 생성
        int x = r.nextInt(300) + 100; //메서드를 사용하여 100부터 399까지의 랜덤한 값을 생성하여 x와 y 변수에 할당
        int y = r.nextInt(300) + 100;
        //외계인 아이템
        Sprite alien = new AlienSprite(context, this, R.drawable.ship_0002, 100 + x, 100 + y);//AlienSprite 객체를 생성하고, 생성자에 필요한 매개변수들을 전달
        sprites.add(alien);//생성된 alien 스프라이트를 sprites에 추가
        currEnemyCount++;  //외계인 아이템 개수 증가
    }

    public ArrayList getSprites() { //이 메서드를 호출하면 sprites 리스트 받음 (게임에 쓰는 스프라이트 객체들).
        return sprites;
    }

    public void resume() {  //사용자가 만드는 resume()함수
        running = true; //running 변수를 true로 바꿔 게임이 실행 중임을 표시
        gameThread = new Thread(this); //this를 사용하여 현재 객체를 기반으로 새로운 스레드 객체 gameThread를 생성
        gameThread.start(); //gameThread 스레드를 시작하여 게임로직 실행
    }

    // Sprite 를 StarshipSprite로 형변환하여 리턴하기
    public StarshipSprite getPlayer() {  return (StarshipSprite) starship; }

    public int getScore() {  return score; } //score리턴
    public void setScore(int score) { this.score = score;} // score 값 재설정
    public void setCurrEnemyCount(int currEnemyCount) { this.currEnemyCount = currEnemyCount; } //currEnemyCount값 설정
    public int getCurrEnemyCount() { return currEnemyCount; } //currEnemyCount 값 리턴

    public void pause() {
        running = false;// running변수를 flase로 바꿔 게임이 멈춤을 표시
        try {
            gameThread.join();  // 스레드 종료 대기하기
        } catch (InterruptedException e) {
        }
    }

    //    public void removeSprite(Sprite sprite) {sprites.remove(sprite);}
//    private ArrayList getSprites(){return sprites;}

    @Override
    public void run() {
        while (running) { //running 변수가 트루인동안 반복
            Random r = new Random(); //랜덤변수 생성
            boolean isEnemySpawn = r.nextInt(100) + 1 < (getPlayer().speed + (int) (getPlayer().getPowerLevel() / 2)); // boolean으로 외게인 스프라이트 생성할지 결정 /플레이어의 속도와 강화 레벨에 따라서 ??
            if (isEnemySpawn && currEnemyCount < MAX_ENEMY_COUNT) spawnEnemy();   // isEnemySpawn가 true상태이고 currEnemyCount가  MAX_ENEMY_COUNT보다 작을때에만 spawnEnemy메서드 호출


            //게임에서 등록된 스프라이트 들을 순회하며 움직임과 충돌체크


            //sprites에있는 각각의 스프라이트들을 순회하며 move명령을 처리
            for (int i = 0; i < sprites.size(); i++) {
                Sprite sprite = (Sprite) sprites.get(i);  //Arraylist 에서 하나씩 가져와서 움직이기
                sprite.move();
            }

            //스프라이트 간의 충돌체크
            for (int p = 0; p < sprites.size(); p++) {
                for (int s = p + 1; s < sprites.size(); s++) {
                    try {
                        Sprite me = (Sprite) sprites.get(p);
                        Sprite other = (Sprite) sprites.get(s);
                        //충돌체크
                        if (me.checkCollision(other)) {
                            me.handleCollision(other);
                            other.handleCollision(me);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            draw();//게임화면 그리는 draw메서드 호출
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

    //게임화면을 그리는 메서드
    public void draw(){
        if(ourHolder.getSurface().isValid()){ //Surface가 유효한지 확인
            canvas= ourHolder.lockCanvas(); //Canvas객체를 얻음 이를 통해 그리기 작업 수행가능
            canvas.drawColor(Color.BLACK); //배경을 검은색으로 덮기
            mapBitmapY++; //수직스크롤효과 구현
            if(mapBitmapY<0) mapBitmapY = 0;
            paint.setColor(Color.BLUE);
            for(int i=0; i< sprites.size(); i++){
                Sprite sprite = (Sprite) sprites.get(i);//sprites리스트에 있는 각각의 Sprite객체를 가져와 
                // sprite.draw(canvas, paint)로 화면에 그림 그림
                sprite.draw(canvas, paint);
            }
            ourHolder.unlockCanvasAndPost(canvas); //그리기 작업이 끝났음을 알려 실제로 화면에 표시되게 함
        }
    }
        //SurfaceView의 Surface가 생성될때 호출됨 startGame(); 를 호출해 게임시작 즉  게임 화면이 생성되었을 때 필요한 설정 및 초기화를 처리하는 역할을 합니다.
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) { startGame();  }

        //작업안됨??
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) { }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {   }

}
