package com.vadmax.chess.pieces

class Pawn(
    override val side: Piece.Companion.Side
) : Piece {

    override val image: String
        get() = "Pawn_" + side.side + ".png"

    override fun moveDirections(): List<MoveDirection> {
        return if (side == Piece.Companion.Side.WHITE) listOf(
            MoveDirection(0, 1, 1),
        )
        else listOf(
            MoveDirection(0, -1, 1)
        )
    }
}