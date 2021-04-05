package com.vadmax.chess

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vadmax.chess.pieces.*
import com.vadmax.chess.util.SingleLiveEvent

class GameViewModel : ViewModel() {

    private val _board = MutableLiveData<MutableMap<String, Piece?>>()
    val board: LiveData<MutableMap<String, Piece?>>
        get() = _board

    private var _takenPiece = SingleLiveEvent<Piece>()
    val takenPiece: LiveData<Piece>
        get() = _takenPiece

    private var _showPieceDialog = SingleLiveEvent<Piece.Companion.Side>()
    val showPieceDialog: LiveData<Piece.Companion.Side>
        get() = _showPieceDialog

    private var _setSelection = SingleLiveEvent<String>()
    val setSelection: LiveData<String>
        get() = _setSelection

    private var _clearSelection = SingleLiveEvent<String>()
    val clearSelection: LiveData<String>
        get() = _clearSelection

    private var _cancelSelection = SingleLiveEvent<String>()
    val cancelSelection: LiveData<String>
        get() = _cancelSelection

    private var _showAvailableMoves = SingleLiveEvent<List<String>>()
    val showAvailableMoves: LiveData<List<String>>
        get() = _showAvailableMoves

    private var _clearAvailableMoves = SingleLiveEvent<List<String>>()
    val clearAvailableMoves: LiveData<List<String>>
        get() = _clearAvailableMoves

    private val _move = MutableLiveData<Move>()
    val move: LiveData<Move>
        get() = _move
    
    private var selection: String? = null

    fun initBoard() {
        _board.value = mutableMapOf()
        var letter = 'a'
        while (letter <= 'h') {
            for (num in 1..8) {
                _board.value!!["$letter$num"] = null
            }
            letter++
        }

        _board.value!!["d4"] = Rook(Piece.Companion.Side.WHITE)
        _board.value!!["a1"] = Bishop(Piece.Companion.Side.WHITE)
        _board.value!!["d7"] = King(Piece.Companion.Side.WHITE)
        _board.value!!["e7"] = Queen(Piece.Companion.Side.WHITE)
        _board.value!!["a2"] = Pawn(Piece.Companion.Side.WHITE)
        _board.value!!["b7"] = Pawn(Piece.Companion.Side.BLACK)
        _board.value!!["a7"] = Pawn(Piece.Companion.Side.BLACK)
        _board.value!!["c7"] = Pawn(Piece.Companion.Side.BLACK)
    }

    fun onClickEvent(id: String) {
        if (selection == null) {
            if (_board.value!![id] != null) {
                selection = id
                _setSelection.value = id
                _showAvailableMoves.value = getAvailableMoves()
            }
        } else {
            val piece = _board.value!![selection]
            if (piece != null && getAvailableMoves().contains(id)) {
                if (_board.value!![id] != null) {
                    _takenPiece.value = _board.value!![id]!!
                } else if (move.value != null && isPassant(piece, id)) {
                    _takenPiece.value = _board.value!![move.value!!.to]
                    _board.value!![move.value!!.to] = null
                    _clearSelection.value = move.value!!.to
                }

                if (piece is Pawn) {
                    val line = Character.getNumericValue(id.toCharArray()[1])
                    if (piece.side == Piece.Companion.Side.WHITE && line == 8) {
                        _showPieceDialog.value = Piece.Companion.Side.WHITE
                    } else if (piece.side == Piece.Companion.Side.BLACK && line == 1) {
                        _showPieceDialog.value = Piece.Companion.Side.BLACK
                    }
                }

                _move.value = Move(selection!!, id, piece)
                _board.value!![id] = piece
                _board.value!![selection!!] = null
                _clearSelection.value = selection!!
            } else if (piece != null && !getAvailableMoves().contains(id)) {
                _cancelSelection.value = selection!!
            }

            _clearAvailableMoves.value = _showAvailableMoves.value
            selection = null
        }
    }

    private fun getAvailableMoves(): List<String> {
        val availableMoves = mutableListOf<String>()
        val currentPosition = selection!!.toCharArray()
        val checkingPos = CharArray(2)
        val piece = _board.value!![selection]!!
        piece.moveDirections().forEach loop@{
            checkingPos[0] = currentPosition[0] + it.x
            checkingPos[1] = currentPosition[1] + it.y

            if (piece is Pawn) {
                if (it.limit == 2) {
                    if (piece.side == Piece.Companion.Side.WHITE && Character.getNumericValue(
                            currentPosition[1]
                        ) != 2
                    ) {
                        return@loop
                    } else if (piece.side == Piece.Companion.Side.BLACK && Character.getNumericValue(
                            currentPosition[1]
                        ) != 7
                    ) {
                        return@loop
                    }
                }
            }

            var limit = 1
            while (
                checkingPos[0] in 'a'..'h' &&
                Character.getNumericValue(checkingPos[1]) in 1..8 &&
                limit <= it.limit
            ) {
                val id = checkingPos[0].toString() + Character.getNumericValue(checkingPos[1])
                if (_board.value!![id] != null) {
                    if (piece.side != _board.value!![id]!!.side) {
                        if (piece is Pawn && it.x != 0 || piece !is Pawn) {
                            availableMoves.add(id)
                        }
                    }
                    break
                } else if (move.value != null && isPassant(piece, id)) {
                    if (piece.side == Piece.Companion.Side.WHITE && currentPosition[1] == '6') {
                        availableMoves.add(id)
                    } else if (piece.side == Piece.Companion.Side.BLACK && currentPosition[1] == '3') {
                        availableMoves.add(id)
                    }
                } else {
                    if (piece is Pawn && it.x == 0 || piece !is Pawn) {
                        availableMoves.add(id)
                    }
                }
                checkingPos[0] = checkingPos[0] + it.x
                checkingPos[1] = checkingPos[1] + it.y
                limit ++
            }
        }

        return availableMoves
    }

    private fun isPassant(currentPiece: Piece, currentId: String): Boolean {
        @Suppress("TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
        if (currentPiece is Pawn && _board.value!![currentId] == null && move.value!!.piece.side != currentPiece.side &&
            move.value!!.piece is Pawn && move.value!!.from[0] == currentId[0]) {
            val length = Character.getNumericValue(move.value!!.from[1]) - Character.getNumericValue(move.value!!.to[1])
            if (kotlin.math.abs(length) == 2) {
                return true
            }
        }
        return false
    }

    fun selectPiece(piece: Piece) {
        val id = _move.value!!.to
        _board.value!![id] = piece
        _move.value = Move(id, id, piece)
    }
}
