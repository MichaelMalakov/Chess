package com.vadmax.chess.pieces

class Rook(
    override val side: Piece.Companion.Side
) : Piece {

    override val image: String
        get() = "Rook_" + side.side + ".png"

    override val cost: Int
        get() = 5

    override fun moveDirections(): List<MoveDirection> {
        return listOf(
            MoveDirection(1, 0, 8),
            MoveDirection(0, 1, 8),
            MoveDirection(-1, 0, 8),
            MoveDirection(0, -1, 8)
        )
    }
}