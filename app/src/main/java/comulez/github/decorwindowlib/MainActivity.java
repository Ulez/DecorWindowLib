package comulez.github.decorwindowlib;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
            for (int i = 0; i < 10; i++) {
                strings.add("Item----" + i);
            }
            ListView list = dialogView.findViewById(R.id.listView);
            list.setAdapter(new ArrayAdapter<>(this, R.layout.simle_item, strings));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    decorWindowFocus.dismiss(parent);
                    Toast.makeText(MainActivity.this, "pos=" + position, Toast.LENGTH_SHORT).show();
                }
            });
            decorWindowFocus = DecorWindow.Builder
                    .newBuilder(this)
                    .setView(dialogView, color)
                    .setPosDp(60, 60, 300, 300)
                    .bindClick(R.id.bt2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            decorWindowFocus.dismiss(v);
                        }
                    })
                    .animate(new DecorWindow.Animatesss() {

                        @Override
                        public Animator getShowAnimator(View view) {
                            return ObjectAnimator.ofFloat(view, "translationX", view.getWidth(), 0).setDuration(600);
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
            View dialogView = LayoutInflater.from(this).inflate(R.layout.list_layout, null);
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                strings.add("Item----" + i);
            }
            ListView list = dialogView.findViewById(R.id.listView);
            list.setAdapter(new ArrayAdapter<>(this, R.layout.simle_item, strings));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    decorWindowUnfocus.dismiss(parent);
                    Toast.makeText(MainActivity.this, "pos=" + position, Toast.LENGTH_SHORT).show();
                }
            });
            decorWindowUnfocus = DecorWindow.Builder
                    .newBuilder(this)
                    .setView(dialogView)
                    .setPosPer(0.1, 0.1, 0.9, 0.9)
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
}

