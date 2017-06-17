# 自定义view CircleSeekbar
自定义view练手，效果图如下：
![](http://i.imgur.com/IdzWpfs.png)
## 实现功能
- 可设置圆环颜色和线宽及触摸后的颜色和线宽
- 可设置圆环内圈显示的文本内容及字体大小、颜色
- 可设置触摸点的图片
- 可设置触摸的有效范围

## 使用示例
```java
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="circlebar.oden.com.circleseekbardemo.MainActivity">

    <circlebar.oden.com.circleseekbar.CircleSeekbar
        android:id="@+id/circle_seekbar"
        android:layout_centerInParent="true"
        android:layout_width="300dp"
        android:layout_height="300dp" />

    <TextView
        android:id="@+id/tv_progress"
        android:text="0%"
        android:textColor="#FF383838"
        android:textSize="18sp"
        android:layout_centerInParent="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />


</RelativeLayout>

```
```java
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

```
## 实现过程
[详细过程见blog](http://blog.csdn.net/yaodong379/article/details/73395610)




