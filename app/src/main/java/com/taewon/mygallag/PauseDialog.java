package com.taewon.mygallag;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import java.time.chrono.MinguoChronology;

public class PauseDialog extends Dialog {
    RadioGroup bgMusicOnOff;
    RadioGroup effectSoundOnOff;
    public PauseDialog(@NonNull Context context){
        super(context);
        setContentView(R.layout.pause_dialog);  //pause_dialog.xml 와 연결
        bgMusicOnOff = findViewById(R.id.bgMusicOnOff); //브금온오프 라디오 그룹과 연결
        effectSoundOnOff = findViewById(R.id.effectSoundOnOff); //효과음온오프 라디오 그룹과 연결
        init();////시스템 실행시 필요한 여러가지 작업 실행
    }





    public void init(){
        bgMusicOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //bgMusicOnOff 라디오버튼 체크가 바뀌면 작동
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){//브금온오프
                    case R.id.bgMusicOn:
                        MainActivity.bgMusic.setVolume(1, 1);
                        break;
                    case R.id.bgMusicOff:
                        MainActivity.bgMusic.setVolume(0, 0);
                        break;
                }
            }
        });
        effectSoundOnOff.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){//효과음 온오프
                    case R.id.effectSoundOn:
                        MainActivity.effectVolumn =1.0f;
                        break;
                    case R.id.effectSoundOff:
                        MainActivity.effectVolumn = 0;
                        break;
                }
            }
        });
    findViewById(R.id.dialogCancelBtn).setOnClickListener(new View.OnClickListener() { //다이얼로그 화면 닫기
        @Override
        public void onClick(View view) {
            dismiss();  //cancel보다 안전하게 화면 종료 , cancel과의 차이 이해하기
            //dismiss는 닫고 메모리에서 제거
            //cancel은 닫고 메로리에 유지
        }
    });

    findViewById(R.id.dialogOkBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dismiss();
        }
    });
    }
}
