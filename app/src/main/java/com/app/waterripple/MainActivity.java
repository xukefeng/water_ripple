package com.app.waterripple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.waterripple.ui.RippleAnimatorView;
import com.app.waterripple.utils.UIUtils;
import com.app.waterripple.utils.ViewCalculateUtil;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private RippleAnimatorView rippleAnimatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtils.getInstance(this);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        //对view进行时适配，大多数情况用android会自动适配，特殊情况需要自己适配
        ViewCalculateUtil.setViewLayoutParam(imageView,
                100, 100, 0, 0, 0, 0);
        rippleAnimatorView = findViewById(R.id.rippleView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rippleAnimatorView.isAnimationRunning()) {
                    rippleAnimatorView.stopRippAnimation();
                } else {
                    rippleAnimatorView.startRippleAnimation();
                }
            }
        });
    }
}
