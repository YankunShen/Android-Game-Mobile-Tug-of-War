package com.example.a.coom;


import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.a.coom.shakeActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import com.example.a.coom.BoardView;
public class shake implements SensorEventListener {

    private String str = null;
    private int count = 0;
    //private Vibrator vb;
    private Context mContext;
    BoardView boardView;
    //??????
    private SensorManager mSensorManager;
    //???
    private Sensor mSensor;
    //????
    private int mSpeed = 5000;
    //????
    private int mInterval = 50;
    //????????
    private long LastTime;
    //????x?y?z??
    private float LastX, LastY, LastZ;

    public float ratio=0;
    private Handler handler=new Handler();
    public shake(Context mContext,Handler handler) {
        this.mContext = mContext;

            this.handler=handler;

        conn();

        Start();

    }

    private final String TAG = "Game";

    public static final int MAX_LENGTH = 1000 * 60 * 2;// 比赛时长2分钟

    private String filePath;
    private long startTime;
    private long endTime;

    private int BASE = 1;
    private int SPACE = 500;// 间隔取样时间






//    private Runnable mUpdateMicStatusTimer = new Runnable() {
//        public void run() {
//            updateStatus();
//        }
//    };


//    LayoutInflater factorys = LayoutInflater.from(mContext);//获取MainActivity中LayoutInflater （上下文参数）
//    View view= factorys.inflate(R.layout.main, null);//获取View 对象
//    TextView Text1 = (TextView) view.findViewById(R.id.jia);



    public int getCount() {
        return this.count;
    }

    public void Start() {

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        //vb = (Vibrator)mContext.getSystemService(Service.VIBRATOR_SERVICE);
        if (mSensorManager != null) {
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        if (mSensor != null) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    public void Stop() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }

    @Override
    public void onSensorChanged(SensorEvent Event) {
        long NowTime = System.currentTimeMillis();
        if ((NowTime - LastTime) < mInterval)
            return;
        //?NowTime??LastTime
        LastTime = NowTime;
        //??x,y,z
        float NowX = Event.values[0];
        float NowY = Event.values[1];
        float NowZ = Event.values[2];
        //??x,y,z???
        float DeltaX = NowX - LastX;
        float DeltaY = NowY - LastY;
        float DeltaZ = NowZ - LastZ;
        //??
        LastX = NowX;
        LastY = NowY;
        LastZ = NowZ;
        //??
        double NowSpeed = Math.sqrt(DeltaX * DeltaX + DeltaY * DeltaY + DeltaZ * DeltaZ) / mInterval * 10000;
        //

        if (NowSpeed >= mSpeed) {
            count++;
            str = String.valueOf(count);
            long[] pattern = {100, 400, 100, 400};
            // vb.vibrate(pattern,-1);
            send();


            //Toast.makeText(mContext, "Motion Detected"+str, Toast.LENGTH_SHORT).show();
        }

    }

    public Socket socket;

    /**
     * ???????
     */
    public void conn() {               //建立连接

        new Thread() {

            @Override
            public void run() {

                try {
                    socket = new Socket("192.168.137.1", 9999);
                    Log.e("JAVA", "address:" + socket);
                    handler.postDelayed(runnable,1000);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }


public  void send(){                //与服务器通信
    new Thread(){
        @Override
        public void run(){
            try {


                    //向服务器发送数据
                    PrintWriter send = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8")));
                    send.println(shakeActivity.instance.name()+" "+str);
                    send.flush();

                    //接受服务端数据
                    BufferedReader recv = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String recvMsg = recv.readLine();
                     shakeActivity.instance.s=recvMsg.split(" ");
                    if (recvMsg != null) {
                        Log.e("shakeActivity", "返回的内容是: A组："+shakeActivity.instance.s[0]+"B组："+shakeActivity.instance.s[1]);
                        float alpha = Float.parseFloat(shakeActivity.instance.s[0]);
                        float beta = Float.parseFloat(shakeActivity.instance.s[1]);
                        Log.e("shakeActivity","alpha:"+alpha);
                        Log.e("shakeActivity","beta:"+beta);
                        //Log.e("shakeActivity",shakeActivity.instance.state);
                        if(shakeActivity.instance.state.equals("A")) {
                            ratio = alpha / (alpha + beta);
                        }else{
                            ratio=(1-alpha/(alpha+beta));
                       }
                        //Log.e("shakeActivity","ratio:"+ratio);


                    }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }.start();
}

         //发送数据
        Runnable runnable=new Runnable() {

             @Override
             public void run() {
                 Message message = Message.obtain();
                 message.what = 0X00;
                 message.obj = ratio;
                 Log.e("shakeActivity", "ratio1:" + ratio);
                 handler.sendMessage(message);

                 show();
//                 Log.e("shakeActivity", "返回的内容是: A组："+shakeActivity.instance.s[0]+"B组："+shakeActivity.instance.s[1]);
                 handler.postDelayed(this, 1000);

             }

         };

public void show(){            //显示甲乙组比例



            try {
                float a = ratio * 100;
                int b = (new Double(a)).intValue();
                //shakeActivity.instance.jia1.setText("hello");
                shakeActivity.instance.jia.setText( b+"%");
                //shakeActivity.instance.yi1.setText(shakeActivity.instance.s[1]);
                shakeActivity.instance.yi.setText((100 - b) + "%");
            }catch (NullPointerException e){
                e.printStackTrace();
            }

    }


}



