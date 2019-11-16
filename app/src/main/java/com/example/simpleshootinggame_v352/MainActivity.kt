package com.example.simpleshootinggame_v352

import android.os.Bundle
import android.os.CountDownTimer
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var screenWidth = 0  //スクリーンの幅を格納する変数の宣言
    private var screenHeight = 0   //スクリーンの高さ格納する変数の宣言

    private lateinit var enemy01 :Enemy  //Enemyクラスの変数を宣言しておく
    private lateinit var bullet01 :Bullet  //Bulletクラスの変数を宣言しておく

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // スクリーンの幅と高さを取得する
        val dMetrics = DisplayMetrics()  //DisplayMetrics のインスタンスを生成する
        windowManager.defaultDisplay.getMetrics(dMetrics)  //スクリーンサイズを取得しているらしい
        screenWidth = dMetrics.widthPixels  //スクリーンの幅を取得
        screenHeight = dMetrics.heightPixels  //スクリーンの高さを取得

        // Enemyクラスのインスタンスを生成する
        var x = 50F;
        var y = screenHeight.toFloat() * 0.2F
        enemy01 = Enemy(imageViewEnemy, x, y, screenWidth);

        // imageViewPlayer の初期位置の設定
        imageViewPlayer.x = 50F
        imageViewPlayer.y = screenHeight.toFloat() * 0.6F

        // タイマのインスタンスの生成
        val timer = MyCountDownTimer(150 * 60 * 1000, 10)
        timerText.text = "150:00"  // ←↑今は150分

        // タイマのスタート
        timer.start()  // <- これで十分かな。
        //timer.apply { start() }  // <- これでもできる。教科書はこれ。
    }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {

            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000 % 60
            timerText.text = "%1d:%2$02d".format(minute, second)

            enemy01.move(3);  // 敵が左右に移動する

            if (bullet01.state == "move"){
                bullet01.move(5)  // 弾が上に移動する
            }
        }

        override fun onFinish() {
            //timerText.text = "0:00"
            timerText.text = "--:--"                                                         //デバッグ用

        }
    }

    //画面タッチのメソッドの定義
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val ex = event.x  //タッチした場所のＸ座標
        val ey = event.y  //タッチした場所のＹ座標

        textViewtouch.text = "X座標：$ex　Y座標：$ey"

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                textViewtouch.append("　ACTION_DOWN")
                imageViewPlayer.x = ex
            }

            MotionEvent.ACTION_UP -> {
                textViewtouch.append("　ACTION_UP")
                bullet01.state = "move"
            }

            MotionEvent.ACTION_MOVE -> {
                textViewtouch.append("　ACTION_MOVE")
                imageViewPlayer.x = ex
            }

            MotionEvent.ACTION_CANCEL -> {
                textViewtouch.append("　ACTION_CANCEL")
            }
        }

        return true

    }
}
