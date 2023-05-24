package com.taewon.mygallag.sprites;
//Sprite  움직이는 2차원 비트맵 개체를 가리키는 용어

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Sprite  {
    protected float x, y;
    protected int width, height;
    protected float dx, dy;
    private Bitmap bitmap;
    protected int id;
    private RectF rect;// 스프라이트의 충돌 영역을 나타내는 사각형

    public Sprite(Context context,int resourceId, float x, float y){
        this.id=resourceId;//resourceId저장
        this.x = x; this.y = y; //초기위치 설정
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);//resourceId에 있는 비트맵이미지 로드
        width = bitmap.getWidth();//비트맵이미지의 넓이 높이 저장
        height = bitmap.getHeight();
        rect = new RectF();//충돌영역 초기화
    }
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public void draw(Canvas cnavas, Paint paint){
        cnavas.drawBitmap(bitmap, x,y,paint);
    } //스프라이트를 캔버스에 그림
    public void move(){ //스프라이트를 이동시킴. 현재 위치에 dx와 dy를 더하고, 충돌 영역인 rect의 좌표를 갱신.
        x=x+dx; y=y+dy;
        rect.left=x; rect.right = x + width;
        rect.top = y; rect.bottom = y + height;
    }
    public float getX() {return x;}
    public float getY() {return y;}
    public float getDx() {return dx;}
    public float getDy() {return dy;}
    public void setDx(float dx) { this.dx = dx;}//x,y방향 이동속도 정함
    public void setDy(float dy) { this.dy = dy;}
    public RectF getRect() {return rect;}//충돌영역 반환
    public boolean checkCollision(Sprite other){ //충돌검사 true나 false로 반환
        return RectF.intersects(this.getRect(), other.getRect());
        //지정된 사각형이 이 사각형과 교차하면 true를 반환하고 이 사각형을 해당 교차점으로 설정하고, 그렇지 않으면 false를 반환하고 이 사각형을 변경하지 않는다
    }
    public void handleCollision(Sprite other){ }//충돌처리 비어있음
    public Bitmap getBitmap(){ return bitmap; }
    public void setBitmap(Bitmap bitmap) {this.bitmap = bitmap;}

}
