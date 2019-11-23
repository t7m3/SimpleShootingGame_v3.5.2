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

        // Bulletクラスのインスタンスを生成する（まずImageViewのインスタンスを生成し、それを元にして）
        var image : ImageView
        image = ImageView(this)  //　ImageViewのインスタンスを生成する
        image.setImageResource(R.drawable.arw02up)  //画像を設定する
        x = 50F
        y = screenHeight.toFloat() * 0.7F
        bullet01 = Bullet(image, x, y, screenHeight)  // ここで実際にBulletクラスのインスタンスを生成している
        layout.addView(bullet01.imageView)  // 画面（layout）に追加する

        // Bulletクラス　インスタンス配列化の実験
        var bulletArray = arrayOfNulls<Bullet?>(5)  // Bulletクラスの配列を宣言
        //var image : ImageView
        for (i in bulletArray.indices){
            image = ImageView(this)  // ImageViewのインスタンスを生成
            image.setImageResource(R.drawable.arw02up)  //画像を設定する
            x = i * 50F
            y = screenHeight.toFloat() * 0.75F
            bulletArray[i] = Bullet(image, x, y, screenHeight)  // Bulletクラスのインスタンス生成
            layout.addView(bulletArray[i]!!.imageView)  // 画面（layout）に追加する
        }

        // タイマのインスタンスの生成
        val timer = MyCountDownTimer(150 * 60 * 1000, 10)
        timerText.text = "150:00"  // ←↑今は150分

        // タイマのスタート
        timer.start()  // <- これで十分かな。
        //timer.apply { start() }  // <- これでもできる。教科書はこれ。
    }

    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {

        private var explosion_millisUntilFinished = 0L // 爆発したときの時刻（のようなもの）を保存する変数

        override fun onTick(millisUntilFinished: Long) {

            val minute = millisUntilFinished / 1000L / 60L
            val second = millisUntilFinished / 1000 % 60
            timerText.text = "%1d:%2$02d".format(minute, second)

            if (enemy01.state == "move"){  // 敵が動いているときは
                enemy01.move(3);  // 敵が左右に移動する
            }

            if (bullet01.state == "move"){

                bullet01.move(5)  // 弾が上に移動する

                if (hit(enemy01.imageView,  bullet01.imageView)==true){ // 弾が敵に当たったか？

                    enemy01.state = "stop"  // 敵の移動を止める
                    enemy01.imageView.setImageResource(R.drawable.s5z8k0g6)  //画像を爆発の画像に変える
                    explosion_millisUntilFinished = millisUntilFinished// 爆発したときの時刻（のようなもの）を保存しておく

                    //弾を初期位置に戻す
                    bullet01.imageView.y = -100F  // y座標を表示範囲外にすると、Bulletクラスのmoveメソッドで、初期位置に戻される
                }
            }

            if(enemy01.state == "stop"){  //敵が止まっているときは（爆発中のときは）
                if (explosion_millisUntilFinished - millisUntilFinished >= 3000) {  // 爆発の時間が経過したら

                    // imageViewEnemyの画像をロケットの画像に変える
                    enemy01.imageView.setImageResource(R.drawable.rocket)
                    enemy01.state = "move"  // imageViewEnemy が移動する
                }
            }
        }

        override fun onFinish() {
            //timerText.text = "0:00"
            timerText.text = "--:--"                                                         //デバッグ用

        }
    }

    //当たり判定のメソッド　当たったら、trueを返す、当たっていなければFalseを返す
    fun hit(enemy: ImageView, bullet: ImageView): Boolean {
        if (enemy.y <= bullet.y && bullet.y <= enemy.y + enemy.height) {
            if (enemy.x <= bullet.x + bullet.width / 2 && bullet.x + bullet.width / 2 <= enemy.x + enemy.width) {
                return true
            }
        }
        return false
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
                bullet01.imageView.x = imageViewPlayer.x + imageViewPlayer.width/2 - bullet01.imageView.width/2
                bullet01.imageView.y = imageViewPlayer.y
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
