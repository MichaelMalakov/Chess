package com.vadmax.chess.pieces

interface Piece {

    val image: String
    val side: Side
    fun moveDirections(): List<MoveDirection>

    companion object {
        enum class Side(val side: String) {
            WHITE("white"),
            BLACK("black")
        }
    }
}