package com.vadmax.chess.pieces

class Queen(
    override val side: Piece.Companion.Side
) : Piece {

    override val image: String
        get() = "Queen_" + side.side + ".png"

    override fun moveDirections(): List<MoveDirection> {
        return listOf(
            MoveDirection(1, 1, 8),
            MoveDirection(1, -1, 8),
            MoveDirection(-1, 1, 8),
            MoveDirection(-1, -1, 8),
            MoveDirection(1, 0, 8),
            MoveDirection(0, 1, 8),
            MoveDirection(-1, 0, 8),
            MoveDirection(0, -1, 8)
        )
    }
}