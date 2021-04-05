package com.vadmax.chess.pieces

class Knight(
    override val side: Piece.Companion.Side
) : Piece {

    override val image: String
        get() = "Knight_" + side.side + ".png"

    override val cost: Int
        get() = 3

    override fun moveDirections(): List<MoveDirection> {
        return listOf(
            MoveDirection(1, 2, 1),
            MoveDirection(-1, 2, 1),
            MoveDirection(2, 1, 1),
            MoveDirection(2, -1, 1),
            MoveDirection(1, -2, 1),
            MoveDirection(-1, -2, 1),
            MoveDirection(-2, 1, 1),
            MoveDirection(-2, -1, 1),
        )
    }
}