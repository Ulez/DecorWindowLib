package comulez.github.decorwindowlib;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button, buttonFocus;
    private DecorWindow decorWindowUnfocus, decorWindowFocus;
    private String TAG = "MainActivity";
    private int color = 0xaa000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.bt_unFocus);
        button.setOnClickListener(this);
        buttonFocus = findViewById(R.id.bt_focus);
        buttonFocus.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_unFocus:
                showUnFocus();
                break;
            case R.id.bt_focus:
                showFocus();
                break;
        }
    }

    private void showFocus() {
        if (decorWindowFocus == null) {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.list_layout, null);
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                strings.add("Item----" + i);
            }
            ListView list = dialogView.findViewById(R.id.listView);
            list.setAdapter(new ArrayAdapter<>(this, R.layout.simle_item, strings));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    decorWindowFocus.dismiss(view);
                    t("pos=" + position);
                }
            });
            decorWindowFocus = DecorWindow.Builder
                    .newBuilder(this)
                    .setView(dialogView, color)
                    .setPosDp(60, 60, 300, 300)
                    .animator(new DecorWindow.Animatesss() {
                        @Override
                        public Animator getShowAnimator(View view) {
                            AnimatorPath path = new AnimatorPath.Builder()
                                    .moveTo(500, -500)
                                    .lineTo(0, 0)
                                    .build();
                            ObjectAnimator animator = ObjectAnimator
                                    .ofObject(decorWindowFocus, "location", new PathEvaluator(), path.getPoints())
                                    .setDuration(400);
                            animator.setInterpolator(new LinearInterpolator());
                            return animator;
                        }

                        @Override
                        public Animator getHideAnimator(View view) {
                            return ObjectAnimator.ofFloat(view, "translationX", 0, view.getWidth()).setDuration(600);
                        }
                    })
                    .build()
                    .showOrHide();
        } else {
            decorWindowFocus.showOrHide();
        }
    }

    private void showUnFocus() {
        if (decorWindowUnfocus == null) {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.message_layout, null);
            decorWindowUnfocus = DecorWindow.Builder
                    .newBuilder(this)
                    .setView(dialogView)
                    .setPosPer(0.1, 0.1, 0.9, 0.9)
                    .bindClick(R.id.tv, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            t(((TextView) v).getText().toString());
                        }
                    })
                    .bindClick(R.id.bt2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decorWindowUnfocus.dismiss(v);
                        }
                    })
                    .build()
                    .showOrHide();
        } else {
            decorWindowUnfocus.showOrHide();
        }
    }

    protected void t(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}

