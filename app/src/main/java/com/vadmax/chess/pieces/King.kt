package com.vadmax.chess.pieces

class King(
    override val side: Piece.Companion.Side
) : Piece {

    override val image: String
        get() = "King_" + side.side + ".png"

    override val cost: Int
        get() = 0

    override fun moveDirections(): List<MoveDirection> {
        return listOf(
            MoveDirection(1, 1, 1),
            MoveDirection(1, -1, 1),
            MoveDirection(-1, 1, 1),
            MoveDirection(-1, -1, 1),
            MoveDirection(1, 0, 1),
            MoveDirection(0, 1, 1),
            MoveDirection(-1, 0, 1),
            MoveDirection(0, -1, 1)
        )
    }
}