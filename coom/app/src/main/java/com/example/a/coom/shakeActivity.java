package com.example.a.coom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.example.a.coom.shake;

public class shakeActivity extends Activity {
    public static final String ACTIVITY_TAG="shakeActivity";
    public static shakeActivity instance=null;
    public   String[] s=null;
    public String name=null;
    public String state=null;
    private SensorManager manager;
    private shake listener;





    @BindView(R.id.NoiseboardView)
    BoardView dashboardView;
    @BindView(R.id.cpb_countdown)
    CountDownProgressBar countDownProgressBar;



    private int    degree = 0;  //记录指针旋转

    public TextView jia;
    public TextView yi;
    public TextView jia1;
    public TextView yi1;
    Unbinder um;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance=this;


       // um=ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();

       ButterKnife.bind(this);
        init();                       //动画

         jia=(TextView)findViewById(R.id.jia);
         yi=(TextView)findViewById(R.id.yi);
        jia1=(TextView)findViewById(R.id.jia);
        yi1=(TextView)findViewById(R.id.yi);
         jia.setText("0%");//AB组比例
        yi.setText("0%");

        if(Intent.ACTION_VIEW.equals(action)){
            Uri uri = i_getvalue.getData();
            if(uri != null){
                 name = uri.getQueryParameter("name");
                 state=uri.getQueryParameter("state");
                System.out.println("名字："+name);
                System.out.println("组别："+state);

            }
        }



        manager = (SensorManager) this.getSystemService(Service.SENSOR_SERVICE);
        listener = new shake(this,handler);

        countDownProgressBar.setDuration(50000, new CountDownProgressBar.OnFinishListener() {
            @Override
            public void onFinish() {
                //um.unbind();
                float alpha = Float.parseFloat(shakeActivity.instance.s[0]);
                float beta = Float.parseFloat(shakeActivity.instance.s[1]);
                if (alpha< beta) {
                    Toast.makeText(shakeActivity.this, "Game Over! B组赢了！" +
                            "A组再接再厉！", Toast.LENGTH_SHORT).show();
                }
                if (alpha>beta) {
                    Toast.makeText(shakeActivity.this, "Game Over! A组赢了！" +
                            "B组再接再厉！", Toast.LENGTH_SHORT).show();
                }
                if (alpha== beta) {
                    Toast.makeText(shakeActivity.this, "Game Over！势均力敌！！", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void init() {

        View rootView = findViewById(R.id.rl_root);

        RotateAnimation rotateAnima = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnima.setDuration(1000);// 设置动画持续时间
        rotateAnima.setFillAfter(true);// 设置动画执行完毕时, 停留在完毕的状态下.

        ScaleAnimation scaleAnima = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnima.setDuration(1000);
        scaleAnima.setFillAfter(true);

        AlphaAnimation alphaAnima = new AlphaAnimation(0, 1);
        alphaAnima.setDuration(2000);
        alphaAnima.setFillAfter(true);


        // 把三个动画合在一起, 组成一个集合动画
        AnimationSet setAnima = new AnimationSet(false);
        setAnima.addAnimation(rotateAnima);
        setAnima.addAnimation(scaleAnima);
        setAnima.addAnimation(alphaAnima);

        rootView.startAnimation(setAnima);
    }


    Handler handler=new Handler(){

        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0X00:
                    if ("-Infinity".equals( msg.obj.toString())) {
                        Log.e("shakeActivity","ratio3");
                    }else {
                        try {
                            String s= msg.obj.toString();
                            Log.e("shakeActivity","ratio4:"+s);
                            double a=Double.parseDouble(s)*100;
                            degree=(new Double(a)).intValue();
                            Log.e("shakeActivity","ratio5:"+degree);

                        }catch (NumberFormatException e){
                            e.printStackTrace();//获取到的值
                        }
                    }



                    if (dashboardView != null)
                        dashboardView.setRealTimeValue(degree);
                    Log.e("shakeActivity","ratio2:"+degree);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume(){
        super.onResume();
        manager.registerListener(listener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }
    public String name(){
        return name;
    }



}
