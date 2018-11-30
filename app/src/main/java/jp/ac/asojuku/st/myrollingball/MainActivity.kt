package jp.ac.asojuku.st.myrollingball

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),SensorEventListener,SurfaceHolder.Callback {


    private var surfaceWidth:Int=0;//サーフェイスの幅
    private var surfaceHeight:Int=0;//サーフェイスの高さ
    private val radius=50.0f;//ボールの半径
    private val coef=1000.0f;//ボールの移動量を計算するための係数

    val ball3=0f;
    val ball4=0f;

    private var ballX:Float=0f;//x座標
    private var ballY:Float=0f;//y座標
    private var vx:Float=0f;
    private var vy:Float=0f;
    private var time:Long=0L;


    private var ballX1:Float=1000f;//x座標
    private var ballY1:Float=1000f;//y座標    private var ballX:Float=0f;//x座標
    private var ballX2:Float=500f;//y座標    private var ballX:Float=0f;//x座標
    private var ballY2:Float=1500f;//y座標


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setContentView(R.layout.activity_main)
        val holder=surfaceView.holder;
        holder.addCallback(this);//サーフェスホルダーにコールバックに自クラスを追加
        btn_reset.setOnClickListener { ontapp() }


    }
    fun ontapp(){
        txt1.setText("頑張れ");
        ballX=surfaceWidth/2.toFloat()
        ballY=surfaceHeight/2.toFloat()
        img1.setImageResource(R.mipmap.ic_launcher_round);

    }

    override fun onResume() {
        super.onResume()
        val sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event ==null){return;}

        if (time==0L){
            time=System.currentTimeMillis();//最初のタイミングは現在時刻を保時
        }

        //イベントのセンサー識別情報がアクセラレータ（加速度センサー）の時だけ以下の処理を実行
        if (event.sensor.type==Sensor.TYPE_ACCELEROMETER){
            //センサーのx、yの値を取得
            val x=-event.values[0];
            val y=event.values[1];

            var t=(System.currentTimeMillis() - time).toFloat();
            time=System.currentTimeMillis();
            t/=5000.0f;

            val dx=(vx+t)+(x*t*t)/2.0f;
            val dy=(vy+t)+(y*t*t)/2.0f;

            ballX+=(dx*coef);
            ballY+=(dy*coef);

            vx+=(x*t);
            vy+=(y*t);

            if(ballX-radius < 0 && vx < 0){
                vx = -vx/10.0f;
                ballX=radius;
            }else if(ballX+radius > surfaceWidth && vx > 0){
                vx = -vx/10.0f;
                ballX=surfaceWidth-radius;
            }

            if(ballY-radius < 0 && vy<0){
                vy = -vy/10.0f;
                ballY=radius;
            }else if(ballY+radius > surfaceHeight && vy>0){
                vy = -vy/10.0f;
                ballY=surfaceHeight-radius;
            }

            if(txt1.text=="頑張れ") {

                if (ballX - radius < 200 && ballX + radius > 100 && ballY - radius < 300 && ballY + radius > 100) {
                    txt1.setText("ゴール");
                    img1.setImageResource(R.drawable.kuge);
                }
                if (ballX - radius < ballX1 + radius && ballX + radius > ballX1 - radius && ballY - radius < ballY1 + radius && ballY + radius > ballY1 - radius) {
                    txt1.setText("失敗");
                    img1.setImageResource(R.drawable.kinoshita);
                }
                if (ballX - radius < ballX2 + radius && ballX + radius > ballX2 - radius && ballY - radius < ballY2 + radius && ballY + radius > ballY2 - radius) {
                    txt1.setText("失敗");
                    img1.setImageResource(R.drawable.kinoshita);
                }
            }

            drawCanvas();
        }

    }
    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceWidth=width;
        surfaceHeight=height;

        ballX=(width/2).toFloat();
        ballY=(height/2).toFloat();

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        val sensorManager=this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this);
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        val sensorManager=this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accSensor=
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(
                this,//リスナー
                accSensor,//加速度センサー
                SensorManager.SENSOR_DELAY_GAME//センシングの頻度
        )
    }

    private fun drawCanvas(){
        val canvas=surfaceView.holder.lockCanvas();

        canvas.drawColor(Color.YELLOW);

        canvas.drawCircle(
                ballX,
                ballY,
                radius,
                Paint().apply { color=Color.MAGENTA }
        );
        canvas.drawCircle(
                ballX1,
                ballY1,
                radius,
                Paint().apply { color=Color.GRAY }
        );

        canvas.drawCircle(
                ballX2,
                ballY2,
                radius,
                Paint().apply { color=Color.GRAY }
        );
        val rect = Rect(100, 300, 200, 100)
        canvas.drawRect(
                rect,
                Paint().apply { color=Color.BLUE }
        );
        surfaceView.holder.unlockCanvasAndPost(canvas)
    }

}


