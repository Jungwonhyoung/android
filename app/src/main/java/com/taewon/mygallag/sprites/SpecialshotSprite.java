package com.taewon.mygallag.sprites;


import android.content.Context;

import com.taewon.mygallag.SpaceInvadersView;

import java.util.Timer;
import java.util.TimerTask;

public class SpecialshotSprite extends Sprite{
    private SpaceInvadersView game;

    public SpecialshotSprite(Context context, SpaceInvadersView game, int resId, float x, float y){
        super(context, resId, x, y);//Sprite의 생성자를 호출
        this.game=game;
        game.getPlayer().setSpecialShooting(true);//게임객체에서 플레이어의 setSpecialShooting(true);호출해 특수발사 모드를 true로
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                autoRemove();
            }
        }, 5000); //5초후에 자동으로 특수샷이 제거되게함
    }

    @Override
    public void move() { //메서드 오버라이딩으로 move메서드 호출후 플레이어 위치에 맞춰 특수샷 위치 업데이트
        super.move();
        this.x = game.getPlayer().getX() - getWidth() + 240;
        this.y = game.getPlayer().getY() - getHeight();
    }

    public void autoRemove(){
        game.getPlayer().setSpecialShooting(false);//게임객체에서 플레이어의 setSpecialShooting(false);호출해 특수발사 모드를 false로
        game.removeSprite(this);//특수샷 스프라이트 제거
    }
}
