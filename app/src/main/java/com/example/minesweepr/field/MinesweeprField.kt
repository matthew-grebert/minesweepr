package com.example.minesweepr.field

data class MinesweeprField(var r: Int = 0, var c: Int = 0, var bomb: Boolean = false) {

    public var hasBomb = bomb
    public var row = r
    public var col = c
    public var isRevealed = false
    public var bombsNear = 0


}