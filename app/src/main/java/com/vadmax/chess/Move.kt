package com.vadmax.chess

import com.vadmax.chess.pieces.Piece

data class Move(
    val from: String,
    val to: String,
    val piece: Piece
)
