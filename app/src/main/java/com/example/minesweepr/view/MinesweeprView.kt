package com.example.minesweepr.view

import android.content.Context
import android.graphics.*
import android.graphics.BitmapFactory.decodeResource
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.minesweepr.R
import com.example.minesweepr.model.MinesweeprModel
import com.google.android.material.snackbar.Snackbar


class MinesweeprView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var paintBackground: Paint = Paint()
    var paintLine: Paint = Paint()
    var gameOver = false



    //Initialize bitmaps
    var bitmap1: Bitmap = decodeResource(
        context?.resources, R.drawable.num1
    )
    var bitmap2: Bitmap = decodeResource(
        context?.resources, R.drawable.num2
    )
    var bitmap3: Bitmap = decodeResource(
        context?.resources, R.drawable.num3
    )
    var bitmapBomb: Bitmap = decodeResource(
        context?.resources, R.drawable.bomb
    )
    var bitmapEmpty: Bitmap = decodeResource(
        context?.resources, R.drawable.none
    )


    init {
        paintBackground.setColor(Color.GRAY)
        paintBackground.style = Paint.Style.FILL
        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // paintText.textSize = height/3f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //Todo edit this into a for loop
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackground)
        for (i in 1..5) {
            canvas?.drawLine(
                0f, (i * height / 5).toFloat(), width.toFloat(), (i * height / 5).toFloat(),
                paintLine
            )
            canvas?.drawLine(
                (i * width / 5).toFloat(), 0f, (i * width / 5).toFloat(), height.toFloat(),
                paintLine
            )
        }
        drawPlayers(canvas)
    }

    //@RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun drawPlayers(canvas: Canvas?) {
        scaleBitmpas()
        for (i in 0..4) {
            for (j in 0..4) {
                checkNum(i, j)
                if (MinesweeprModel.checkBomb(i, j) == false && MinesweeprModel.isVisible(i, j)){
                    drawNumber(i, j, canvas);
                } else if (MinesweeprModel.checkBomb(i, j) == true && MinesweeprModel.isVisible(i, j) ){
                    canvas?.drawBitmap(
                        bitmapBomb,
                        (i * width / 5).toFloat(),
                        (j * height / 5).toFloat(),
                        paintLine
                    )
                    revealAll()
                    if(!gameOver) {
                        gameOver = true
                        onSNACK(this@MinesweeprView, "YOU LOSE")
                    }
                }
            }
        }
    }

    private fun scaleBitmpas() {
        bitmap1 = Bitmap.createScaledBitmap(bitmap1, width / 5, height / 5, true)
        bitmap2 = Bitmap.createScaledBitmap(bitmap2, width / 5, height / 5, true)
        bitmap3 = Bitmap.createScaledBitmap(bitmap3, width / 5, height / 5, true)
        bitmapBomb = Bitmap.createScaledBitmap(bitmapBomb, width / 5, height / 5, true)
        bitmapEmpty = Bitmap.createScaledBitmap(bitmapEmpty, width / 5, height / 5, true)

    }

    private fun drawNumber(col: Int, row: Int, canvas: Canvas?) {
        if (checkNum(col, row) == 1) {
            canvas?.drawBitmap(
                bitmap1,
                (col * width / 5).toFloat(),
                (row * height / 5).toFloat(),
                paintLine
            )
        } else if (checkNum(col, row) == 2) {
            canvas?.drawBitmap(
                bitmap2,
                (col * width / 5).toFloat(),
                (row * height / 5).toFloat(),
                paintLine
            )
        } else if (checkNum(col, row) == 3) {
            canvas?.drawBitmap(
                bitmap3,
                (col * width / 5).toFloat(),
                (row * height / 5).toFloat(),
                paintLine
            )
        }else{
            canvas?.drawBitmap(
                bitmapEmpty,
                (col * width / 5).toFloat(),
                (row * height / 5).toFloat(),
                paintLine
            )
        }
    }

    private fun checkNum(col: Int, row: Int): Int {
        var nearbyBombs = 0;
        for (i in col - 1..col + 1) {
            if (i >= 0 && i <= 4) {
                for (j in row - 1..row + 1) {
                    if (j >= 0 && j <= 4) {
                        if (MinesweeprModel.checkBomb(i, j) == true) {
                            nearbyBombs++
                        }
                    }
                }
            }
        }
        MinesweeprModel.setBombCount(col, row, nearbyBombs)
        return nearbyBombs
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / 5)
            val tY = event.y.toInt() / (height / 5)
            if (tX < 5 && tY < 5 ) {
                    MinesweeprModel.makeVisable(tX, tY)
                    if(MinesweeprModel.getBombCount(tX, tY) == 0){
                        creepReveal(tX, tY)
                    }
                    invalidate()
                    checkWin(tX, tY)
                }
            }
        return true
    }

    fun creepReveal(col :Int,row :Int) {
            for (i in col - 1..col + 1) {
                if (i >= 0 && i <= 4) {
                    for (j in row - 1..row + 1) {
                        if (j >= 0 && j <= 4) {
                            if (!MinesweeprModel.isVisible(i, j)){
                                MinesweeprModel.makeVisable(i, j)
                                if(MinesweeprModel.getBombCount(i, j) == 0) {
                                    creepReveal(i, j)
                                }
                            }

                        }
                    }
                }
            }
        invalidate()
    }

    fun checkWin(col :Int, row :Int){
        if(MinesweeprModel.checkWin(col, row)) {
            onSNACK(this@MinesweeprView, "YOU WIN")
            println("YOU WIN")
            gameOver = true
            revealAll()
        }
    }

    fun revealAll(){
        for(i in 0..4){
            for(j in 0..4){
                MinesweeprModel.makeVisable(i, j)
            }
        }
       invalidate()
    }

    fun onSNACK(view: View, text : String){
        //Snackbar(view)
        val snackbar = Snackbar.make(view, text,
            Snackbar.LENGTH_LONG).setAction("Action", null)
        snackbar.show()
    }
}
