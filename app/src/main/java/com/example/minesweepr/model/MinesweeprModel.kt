package com.example.minesweepr.model

import com.example.minesweepr.field.MinesweeprField
import kotlin.math.max

object MinesweeprModel {

    private var maxReveal = 0
    private var currentRevealed = 0
    private val model = arrayOf(
        arrayOf(
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField()
        ),
        arrayOf(
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField()
        ),
        arrayOf(
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField()
        ),
        arrayOf(
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField()
        ),
        arrayOf(
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField(),
            MinesweeprField()
        )
    )

    init {
        for (i in 0..4) {
            for (j in 0..4) {
                model[i][j] = MinesweeprField(i, j, false)
                maxReveal++
            }
        }
        randomizeBombs()
    }

    private fun randomizeBombs() {
        var bombs = 0
        while (bombs < 3) {
            val rand1 = Math.random() * 5
            val rand2 = Math.random() * 5
            if (model[rand1.toInt()][rand2.toInt()].bomb != true) {
                model[rand1.toInt()][rand2.toInt()].bomb = true
                maxReveal--
                bombs++
            }
        }
    }


    fun checkBomb(x: Int, y: Int) = model[x][y].bomb

    fun setFieldContent(x: Int, y: Int) {
        model[x][y].bomb = true
    }

    fun isVisible(x :Int, y :Int) = model[x][y].isRevealed

    fun makeVisable(x: Int, y :Int){
        model[x][y].isRevealed = true;
        currentRevealed++

    }

    fun setBombCount(x :Int, y :Int, bombs :Int){

        model[x][y].bombsNear = bombs
    }

    fun getBombCount(x: Int, y: Int) :Int{
        return model[x][y].bombsNear
    }

    fun resetModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                model[i][j] = MinesweeprField()
            }
        }
    }

    fun checkWin(col :Int, row :Int) = (currentRevealed == maxReveal && checkBomb(col, row) == false)


}