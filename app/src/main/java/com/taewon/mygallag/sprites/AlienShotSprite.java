package com.taewon.mygallag.sprites;

import android.content.Context;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;

//Sprite를 상속받는 외계인 총알을 나타내는 클래스
public class AlienShotSprite extends Sprite{
    private Context context;
    private SpaceInvadersView game;

    //외계인의 총알 생성자

    public AlienShotSprite(Context context, SpaceInvadersView game, float x, float y, int dy){
        super(context, R.drawable.shot_001, x, y);//Sprite의 생성자를 호출 이를통해 총알의 이미지 리소스와 초기위치 설정
        this.game = game;//game과context변수 초기화
        this.context = context;
        setDy(dy); //총알의 수직 이동속도 설정
    }
}
