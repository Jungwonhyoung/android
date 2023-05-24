package com.taewon.mygallag.sprites;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;
import java.util.Random;

public class AlienSprite extends Sprite{
    private Context context;
    private SpaceInvadersView game;
    ArrayList<AlienShotSprite> alienShotSprites;
    Handler fireHandler = null;
    boolean isDestroyed = false;
    public AlienSprite(Context context, SpaceInvadersView game, int resId, int x, int y){
        //외계인 만들기
        super(context, resId, x, y); //초기 위치 등 외계인 기본속성 설정
        this.context = context;
        this.game=game;  //SpaceInvadersView
        alienShotSprites = new ArrayList<>(); //외계인 발사하는 총알 저장 스프라이트 생성(배열)
        Random r = new Random();//초기 이동방향을 랜덤하게 이동
        int randomDx = r.nextInt(5); //0~4까지 숫자 랜덤으로 하나 받음
        int randomDy = r.nextInt(5);
        if(randomDy <= 0) dy=1;
        dx = randomDx; dy=randomDy;//랜덤으로 이동

        //스레드 충돌방지를 위해 main 스레드로 전달하기 위한 Handler생성
        fireHandler = new Handler(Looper.getMainLooper());
        fireHandler.postDelayed( //postDelayed를 줘서 일정한 간격으로 실행
                //delay주는 함수
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d("run", "동작");
                        Random r = new Random();
                        boolean isFire = r.nextInt(100)+1 <=30; //isFire 값이 true이고 isDestroyed가 false인 경우 fire()를 호출하여 외계인이 총알을 발사.
                        //fire가 없으면 생성
                        if(isFire && !isDestroyed){
                            fire();
                            fireHandler.postDelayed(this, 1000);
                        }
                    }
                }, 1000);
    }

    @Override
    public void move() {
        super.move();
        if(((dx<0) && (x <10)) || ((dx >0) && (x >800))){ // 벽에 부딪히면 각각의 값 반대로
            dx = -dx;
            if(y > game.screenH){ game.removeSprite(this);}  //아래쪽을 넘어가면 게임에서 외계인 제거
        }
    }

    @Override //handleCollision 메서드는 외계인과 다른 스프라이트 간의 충돌을 처리.
    public void handleCollision(Sprite other) {
        if(other instanceof ShotSprite){ //ShotSprite인 경우 외계인을 게임에서 제거
            game.removeSprite(other); //other스프라이트 제거
            game.removeSprite(this);//외계인 제거
            destroyAlien();//destroyAlien() 실행
            return;
        }
        if(other instanceof SpecialshotSprite){
            game.removeSprite(this); //외계인제거
            destroyAlien();//destroyAlien() 실행
            return;
        }
    }
    private void destroyAlien(){
        isDestroyed = true; //isDestroyed true로 외계인 파괴를 표시
        game.setCurrEnemyCount(game.getCurrEnemyCount()-1); //game의 현재 적 수 하나 감소
        for(int i =0; i<alienShotSprites.size(); i++)//파괴시 외계인의 총알 없앰
            game.removeSprite(alienShotSprites.get(i));
        spawnHealItem(); //실행
        spawnPowerItem();
        spawnSpeedItem();
        game.setScore(game.getScore() + 1);//점수 1증가
        MainActivity.scoreTv.setText(Integer.toString(game.getScore()));//점수 표 재설정
    }


    private void fire(){ //외계인의 총알 발사
        AlienShotSprite alienShotSprite = new AlienShotSprite(context, game,
                getX(), getY()+30, 16);
        alienShotSprites.add(alienShotSprite);  //AlienShotSprite 을 위한 Arraylist에 추가
        game.getSprites().add(alienShotSprite);
        //SpaceInvadersView의 Arraylist에 추가(이를 통해 외계인이 발사한 총알이 게임화면에 등장)
    }



    private void spawnSpeedItem(){
        Random r = new Random();
        int speedItemDrop = r.nextInt(100) + 1; //00~99 랜덤값 생성 후 speedItemDrop 변수에 저장
        if(speedItemDrop <=5 ){ //speedItemDrop 변수의 값이 5이하 일 경우에만 실행
            int dx = r.nextInt(10) + 1;//랜덤한 값을 dx dy에 저장 (아이템의 속도와 방향)
            int dy = r.nextInt(10) + 5;
            game.getSprites().add(new SpeedItemSprite(context, game, //생성한 아이템스프라이트를 game리스트에 추가 시킴 -> 게임에 생성
                    (int)this.getX(), (int)this.getY(), dx, dy));
        }
    }

    private void spawnPowerItem(){
        Random r = new Random();
        int powerItemDrop = r.nextInt(100) +1;
        if(powerItemDrop <=3 ){
            int dx = r.nextInt(10) + 1;
            int dy = r.nextInt(10) + 10;
            game.getSprites().add(new PowerItemSprite(context, game,
                    (int)this.getX(), (int)this.getY(), dx, dy));
        }
    }
    private void spawnHealItem(){
        Random r = new Random();
        int powerItemDrop = r.nextInt(100) + 1;
        if(powerItemDrop <= 1){
            int dx = r.nextInt(10) + 1;
            int dy = r.nextInt(10) + 10;
            game.getSprites().add(new HealitemSprite(context, game,
                    (int) this.getX(), (int)this.getY(), dx, dy));
        }
    }

}
