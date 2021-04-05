package com.vadmax.chess.pieces

class Bishop(
    override val side: Piece.Companion.Side
) : Piece {

    override val image: String
        get() = "Bishop_" + side.side + ".png"

    override val cost: Int
        get() = 3

    override fun moveDirections(): List<MoveDirection> {
        return listOf(
            MoveDirection(1, 1, 8),
            MoveDirection(1, -1, 8),
            MoveDirection(-1, 1, 8),
            MoveDirection(-1, -1, 8)
        )
    }
}