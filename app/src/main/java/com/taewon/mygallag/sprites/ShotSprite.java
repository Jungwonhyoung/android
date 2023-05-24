package com.taewon.mygallag.sprites;


import android.content.Context;

import com.taewon.mygallag.SpaceInvadersView;

//내가 쏘는 공격
public class ShotSprite extends Sprite{
    private SpaceInvadersView game;

    public ShotSprite(Context context,SpaceInvadersView game, int resId, float x, float y, int dy){
        super(context, resId, x, y);//Sprite의 생성자 호출후 총알의 이미지 리소스와 초기위치 설정
        this.game = game;// game변수 초기화
        setDy(dy);//총알의 수직 이동 속도 설정
    }
}
