package com.taewon.mygallag.items;

//heal아이템 스프라이트를 생성하고 초기화 하는 역할
import android.content.Context;
import android.os.health.HealthStats;

import com.taewon.mygallag.R;
import com.taewon.mygallag.SpaceInvadersView;
import com.taewon.mygallag.sprites.Sprite;

import java.util.Timer;
import java.util.TimerTask;
//sprite에서 상속받음
public class HealitemSprite extends Sprite {

    SpaceInvadersView game;

    public HealitemSprite(Context context, SpaceInvadersView game, int x, int y, int dx, int dy){
        super(context, R.drawable.heal_item, x, y);
        this.game = game;
        this.dx = dx;
        this.dy = dy;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                autoRemove();
            }
        }, 10000);//10초간 아이템 먹지 않으면 삭제
    }

    //sprite 자동제거 명령
    private void autoRemove() { game.removeSprite(this); }

    //힐 아이템의 이동
    @Override
    public void move() {
        if((dx < 0) && (x <120)){
            dx *= -1; return;
        }
        if((dx>0) && (x > game.screenW -120)){
            dx *= -1; return;
        }
        if((dy<0) && (y < 120)){
            dy *= -1; return;
        }
        if((dy>0) && (y> game.screenH-120)){
            dy *= -1; return;
        }
        super.move();
    }
}
