package com.haieros.viewlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    String text = "测试消息展示是吃撒接口返回卡刷卡粉红色卡号发和OID就撒俩开发都是理科就复合的数据库浩丰科技到时候看而我馊味的看来广进克劳福德寄过来肯酱豆腐两个接口发动机盖来看待就看了发动机了国际法带了几根来得及弗兰克jkljlkjlkjlkjlkgjfgdlgklfdjglkfddgjdkjglkrejljewljtwejws而无法is留短发了删的记录看东方航空就复合的看就是复健科厚大司考解放后肯定会ewgdgfdgddfgfdkgjkldjkgljdfl加了快递费金刚骷髅岛就发了感觉点击更多积分感觉到了分开";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JustTextView kang = (JustTextView) findViewById(R.id.kang);
        kang.setText1(text);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
