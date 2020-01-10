package com.example.simpleshootinggame_v352

import android.view.View
import android.widget.ImageView

class Bullet(image: ImageView, x:Float, y:Float, height:Int) {

    public val imageView = image
    private val screenHeight = height
    public var state = "stop"  // 弾の状態を保存する。
                               //"move"：弾は動いている。"stop"：弾は動いていない。
    private var xx = x
    private var yy = y

    init {
        imageView.x = x;
        imageView.y = y;
        imageView.visibility = View.INVISIBLE  // 弾を不可視にする
    }

    public fun move(y:Int){

        imageView.y = imageView.y - y  // yだけ、上に移動する

        if(imageView.y <= 0){  //画面の上端になったら
            state = "stop"
            imageView.x = xx // 位置を初期位置にする
            imageView.y = yy // 位置を初期位置にする
            imageView.visibility = View.INVISIBLE  // 弾を不可視にする
        }

    }
}