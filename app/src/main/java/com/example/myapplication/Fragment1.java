package com.example.myapplication;

/**
 * Created by 강지희 on 2017-12-04.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Fragment1 extends Fragment {
    public Fragment1()
    {
        // required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RelativeLayout layout = (RelativeLayout)inflater.inflate(R.layout.fragment_fragment1,
                container, false);
        return layout;
    }



    public class MainActivity extends Activity implements View.OnClickListener {
        /** Called when the activity is first created. */
        TextView tv1;
        MyThread1 tr1=new MyThread1();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_fragment1);
            Button bt1 = (Button) findViewById(R.id.bt_Start);
            Button bt2 = (Button) findViewById(R.id.bt_Clear);
            bt1.setOnClickListener(this);
            bt2.setOnClickListener(this);
            tv1 = (TextView) findViewById(R.id.tv);
        }

        @Override
        public void onClick(View arg0) {//버튼이 클릭 되었을때

            if (arg0.getId() == R.id.bt_Start) {//만약 bt_Start버튼이 클릭 되면
                tr1.start();                     //스레드가 시작 된다.
            } else if (arg0.getId() == R.id.bt_Clear) {//만약 bt_Clear이 클릭되면
                tv1.setText(" ");                                 //tv1의 문자열을 공백으로 채운다.
            }
        }


        Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                tv1.setText(""+msg.what);
            };
        };
        class MyThread1 extends Thread{//스래드 클래스의 정의
            @Override
            public void run() {//run메소드의 재정의
                super.run();
                for(int i=0;i<100;i++){
                    handler.sendEmptyMessage(i);//핸들러에 메시지 보낸다.
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

    }

}
