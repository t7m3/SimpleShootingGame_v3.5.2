package com.example.simpleshootinggame_v352

import android.widget.ImageView

public class Enemy(image:ImageView, x:Float, y:Float, width: Int) {
    // x,yは表示する座標の初期値。widthはスクリーンの横幅。

    public val imageView = image;
    private val screenwidth = width;

    init {
        imageView.x = x;
        imageView.y = y;
    }

    fun move(x:Int): Int{  //　xは移動量

        return 1;
    }
}