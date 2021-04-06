package com.vadmax.chess.pieces

class Bishop(
    override val side: Piece.Companion.Side,
    override val cost: Int = 3
) : Piece {

    override val image: String
        get() = "Bishop_" + side.side + ".png"

    override fun moveDirections(): List<MoveDirection> {
        return listOf(
            MoveDirection(1, 1, 8),
            MoveDirection(1, -1, 8),
            MoveDirection(-1, 1, 8),
            MoveDirection(-1, -1, 8)
        )
    }
}