package com.taewon.mygallag.sprites;


import android.content.Context;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.taewon.mygallag.MainActivity;
import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.items.HealitemSprite;
import com.taewon.mygallag.items.PowerItemSprite;
import com.taewon.mygallag.items.SpeedItemSprite;

import java.util.ArrayList;

public class StarshipSprite extends Sprite{
    Context context;
    SpaceInvadersView game;
    public float speed;
    private int bullets, life=3, powerLevel;
    private int specialShotCount;
    private boolean isSpecialShooting;
    private static ArrayList<Integer> bulletSprites = new ArrayList<Integer>();
    private final static float MAX_SPEED = 3.5f;
    private final static int MAX_HEART = 3;
//    ArrayList<Integer> effectSoundIdList;
    private RectF rectF;
    private boolean isReloading = false;


    //우주선 스프라이트 클래스

    public StarshipSprite(Context context, SpaceInvadersView game, int resId, int x, int y, float speed){
        super(context, resId, x, y); //초기 위치 설정
        this.context = context;
        this.game = game;
        this.speed = speed;
        init();// 메서드에서 초기화 작업을 수행
    }

    public void init(){
        dx=dy=0; //이동속도 0으로 초기화
        bullets=30;//총알
        life=3;//생명
        specialShotCount = 3;//필살기 가능 횟수
        powerLevel=0;//우주선의 레벨
        Integer [] shots = {R.drawable.shot_001,R.drawable.shot_002,R.drawable.shot_003,R.drawable.shot_004,R.drawable.shot_005,R.drawable.shot_006,R.drawable.shot_007};
        //우주선이 발사할수 있는 총알의 종류의 변수

        for(int i =0; i<shots.length; i++){
            bulletSprites.add(shots[i]); //bulletSprites에 shot배열의 총알이미지 저장
        }
    }

    @Override
    public void move() {
        //벽에 부딪히면 못 가게 하기위해 조건문
        if((dx<0) && (x <120)) return; //벽에 닿으면 이동을 막음
        if((dx>0) && (x > game.screenW - 120)) return;
        if((dy<0) && (y <120)) return;
        if((dy>0) && (y > game.screenH-120)) return;
        super.move(); //return이 발동하지 않았으면 move로 위치 이동
    }

    //총알 개수 리턴
    public int getBulletsCount() {return bullets;}

    //위, 아래, 오른쪽, 왼쪽 이동하기
    public void moveRight(double force) {setDx((float)(1*force*speed));} //force값에 따라 이동 속도 결정
    public void moveLeft(double force) {setDx((float)(-1*force*speed));}
    public void moveDown(double force) {setDy((float)(1*force*speed));}
    public void moveUp(double force) {setDy((float)(-1*force*speed));}
    public void resetDx(){ setDx(0); }
    public void resetDy(){ setDy(0); }

    //스피드 제어
    public void plusSpeed(float speed) { this.speed += speed;} //스피드업

    //총알 발사
    public void fire(){
        if(isReloading | isSpecialShooting){return;} //isReloading 또는 isSpecialShooting가 트루면 총알 발사 x 메서드 종료 //총알 발사 안됨
        MainActivity.effectSound(MainActivity.PLAYER_SHOT); // 메인액티비티의 이펙트사운드 메서드로 총알발사 효과음 재생
        //ShotSprite 생성자구현
        ShotSprite shot = new ShotSprite(context, game, bulletSprites.get(powerLevel), getX()+10, getY()-30, -16 );
        //생성자를 호출해 총알 스프라이트 생성

        //SpaceInvadersView 의 getSprites()구현
        game.getSprites().add(shot);//스프라이트 목록에 shot 총알 스프라이트를 추가하는 코드//////////??
        bullets--; //총알 감소

        MainActivity.bulletCount.setText(bullets + "/30"); //텍스트 변경
        Log.d("bullets", bullets + "/30");
        if(bullets ==0){//총알이 0이면
            reloadBullets();//리로드 불렛 실행
            return;
        }
    }

    public void powerUp(){
        if(powerLevel >= bulletSprites.size() - 1){ //파워레벨이 최대레벨에 도달한지 확인
            game.setScore(game.getScore() + 1);//스코어 1증가
            MainActivity.scoreTv.setText(Integer.toString(game.getScore()));//스코어 화면 업데이트
            return;
        }
        powerLevel++;//레벨 1증가
        MainActivity.fireBtn.setImageResource(bulletSprites.get(powerLevel)); //fireBtn의 이미지를 해당 레벨에 맞는 이미지로 변경
        MainActivity.fireBtn.setBackgroundResource(R.drawable.round_button_shape);//fireBtn의 배경 round_button_shape로 변경
    }
    //총알 다시 셋팅
    public void reloadBullets(){//재장전 메소드
        isReloading = true; //isReloading(재장전 중)을 true로 바꾸어 장전중 다른작업 불가능하게(총쏘기)
        MainActivity.effectSound(MainActivity.PLAYER_RELOAD); //재장전 소리 재생
        MainActivity.fireBtn.setEnabled(false);//총알 발사 버튼 비활성화
        MainActivity.reloadBtn.setEnabled(false);//재장전 버튼 비활성화
        //Thread sleep 사용하지 않고 지연시키는 클래스
        new Handler().postDelayed(new Runnable(){ //핸들러를 통해 2초 동안 코드 진행을 지연시킴
            @Override
            public void run() {
                bullets=30;//총알 30발
                MainActivity.fireBtn.setEnabled(true);//비활성화 한것들 다시 활성화로
                MainActivity.reloadBtn.setEnabled(true);
                MainActivity.bulletCount.setText(bullets + "/30");//텍스트 0인걸 30으로 변경
                MainActivity.bulletCount.invalidate(); //화면새로고침
                isReloading=false;
            }
        }, 2000);
    }

    //필살기
    public void specialShot(){
        specialShotCount--; //specialShotCount하나 감소
        //SpecialshotSprite 구현
        SpecialshotSprite shot = new SpecialshotSprite(context, game, R.drawable.laser, getRect().right -
                getRect().left, 0); //SpecialshotSprite 객체생성 생성된 SpecialshotSprit는 게임화면에서 필살기로 표시됨
        
        //game -> SpaceInvadersView의 getSprites() : sprite에 shot추가하기
        game.getSprites().add(shot);//생성된 SpecialshotSprite 스프라이트를 게임의 스프라이트 리스트에 추가함으로서 화면에 나타날수있게 됨
    }

    public int getSpecialShotCount(){ return specialShotCount; }
    public boolean isSpecialShooting() { return isSpecialShooting;}
    public void setSpecialShooting(boolean specialShooting)
    { isSpecialShooting=specialShooting; }

    public int getLife() { return life;}
    //생명 잃었을때
    public void hurt(){
        life--; //라이프 1감소
        if(life<=0) { //라이프가 0보다 작거나 같으면
            ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(
                    (R.drawable.ic_baseline_favorite_border_24)); //생명을 나타내는 이미지 뷰 중 제일 마지막 체력을 나타내는 이미지뷰로 변경
            // SpaceInvadersView의 endGame() 에서 game종료시키기
            game.endGame();//게임종료
            return;//리턴
        }
            Log.d("hurt", Integer.toString(life));  //생명 확인하기
            ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource(
                    R.drawable.ic_baseline_favorite_border_24); //아니면 지금 생명값에 해당하는 이미지 뷰로 출력

    }

    //생명 얻었을 때
    public void heal(){
        Log.d("heal", Integer.toString(life));
        if(life + 1  > MAX_HEART){ //life + 1이 MAX_HEART 보다 큰지 확인하고  클경우 플레이어에게 추가 점수 부여 후 종료
            game.setScore(game.getScore() +1);
            MainActivity.scoreTv.setText(Integer.toString(game.getScore())); //스코어판에 등록
            return;
        }
        ((ImageView) MainActivity.lifeFrame.getChildAt(life)).setImageResource( //그렇지 않은경우 현재 생명값에 해당하는 이미지 뷰로 전환
                R.drawable.ic_baseline_favorite_24);
        life++;
    }

    //속도 올리기
    private void speedUp(){
        if(MAX_SPEED >= speed + 0.2f) plusSpeed(0.2f); //speed + 0.2f가 MAX_SPEED  보다작거나 같은지 확인후 최대속도를 초과하지않았으면 0.2f속도 추가시킴
        else{//초과하였으면
            game.setScore(game.getScore() + 1); // 플레이어에게 추가 점수 부여
            MainActivity.scoreTv.setText(Integer.toString((game.getScore()))); //텍스트 뷰 변경
        }
    }

    //Sprite 의 handleCollision()의 override. 받는 아이템에 따라 생명, 스피드, 파워, 목숨의 값을 변경한다.
    @Override //다른 스프라이트와의 충돌 처리
    public void handleCollision(Sprite other) {
        if(other instanceof AlienSprite){ //충돌한게 AlienSprite면
            // Alien 아이템이면
            game.removeSprite(other); //해당 스프라이트 제거
            MainActivity.effectSound((MainActivity.PLAYER_HURT)); //다친 효과음 재생
            hurt();//hurt메서드 실행
        }
        if(other instanceof SpeedItemSprite){
        //스피드 아이템이면
            game.removeSprite(other);
            MainActivity.effectSound((MainActivity.PLAYER_GET_ITEM));
            speedUp();
        }
        if(other instanceof AlienShotSprite){
            //총알 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_HURT);
            game.removeSprite(other);
            hurt();
        }
        if(other instanceof PowerItemSprite){
            //아이템을 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            powerUp();
            game.removeSprite(other);
        }
        if(other instanceof HealitemSprite){
            //생명 아이템 맞으면
            MainActivity.effectSound(MainActivity.PLAYER_GET_ITEM);
            game.removeSprite(other);
            heal();
        }
    }
    public int getPowerLevel(){ return powerLevel; }
}
