package com.vadmax.chess.pieces

class Knight(
    override val side: Piece.Companion.Side,
    override val cost: Int = 3
) : Piece {

    override val image: String
        get() = "Knight_" + side.side + ".png"

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