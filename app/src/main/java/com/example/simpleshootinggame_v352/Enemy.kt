package com.example.simpleshootinggame_v352

import android.widget.ImageView

public class Enemy(image:ImageView, x:Float, y:Float, width: Int) {
    // x,yは表示する座標の初期値。widthはスクリーンの横幅。

    public val imageView = image;
    private val screenwidth = width;
    private var dir = 1;

    init {
        imageView.x = x;
        imageView.y = y;
    }

    fun move(x:Int): Int{  //　xは移動量
        imageView.x = imageView.x + x * dir  //移動する

        if(imageView.x < 0 ||  screenwidth - imageView.width < imageView.x ){  //移動した後、左端または右端を越えたら
            dir = dir * -1;  //移動の左右の向きを反転する
        }
        return 1;
    }
}