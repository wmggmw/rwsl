package com.wmg.rwsl.animationTest.test2;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wmg.rwsl.R;

public class AnimationTest02Activity extends AppCompatActivity {
    private Button mBtnMoveIt;
    private Button mBtnTheTarget;
    private Animation translateAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_test01_main);

        mBtnMoveIt = findViewById(R.id.btn_move_it);
        mBtnTheTarget = findViewById(R.id.btn_the_target);

        translateAnimation = new TranslateAnimation(0,500,0,500);
//        使用java代码的方式创建TranslateAnimation，传入六个参数，fromXType、fromXValue、toXType、toXValue和fromYType、fromYValue、toYType、toYValue，使用如下构造方法。
//        参数说明：
//        fromXType：动画开始前的X坐标类型。取值范围为ABSOLUTE（绝对位置）、RELATIVE_TO_SELF（以自身宽或高为参考）、RELATIVE_TO_PARENT（以父控件宽或高为参考）。
//        fromXValue：动画开始前的X坐标值。当对应的Type为ABSOLUTE时，表示绝对位置；否则表示相对位置，1.0表示100%。
//        toXType：动画结束后的X坐标类型。
//        toXValue：动画结束后的X坐标值。
//        fromYType：动画开始前的Y坐标类型。
//        fromYValue：动画开始前的Y坐标值。
//        toYType：动画结束后的Y坐标类型。
//        toYValue：动画结束后的Y坐标值。
        translateAnimation.setDuration(5000);
        translateAnimation.setInterpolator(new DecelerateInterpolator());

        mBtnMoveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnTheTarget.startAnimation(translateAnimation);
            }
        });
        mBtnTheTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnimationTest02Activity.this, "你点到我了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
