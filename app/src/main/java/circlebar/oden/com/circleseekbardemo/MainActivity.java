package circlebar.oden.com.circleseekbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import circlebar.oden.com.circleseekbar.CircleSeekbar;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    CircleSeekbar circleSeekbar;
    TextView tv_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleSeekbar = (CircleSeekbar) findViewById(R.id.circle_seekbar);
        tv_progress = (TextView) findViewById(R.id.tv_progress);

        circleSeekbar.setOnSeekBarChangeListener(new CircleSeekbar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircleSeekbar circleSeekbar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged progress: " + progress);
                tv_progress.setText(progress + "%");
            }
        });

    }
}
