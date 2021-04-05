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
        piece.moveDirections().forEach {
            checkingPos[0] = currentPosition[0] + it.x
            checkingPos[1] = currentPosition[1] + it.y
            var limit = 1
            var pawnLimit = 0
            if (piece is Pawn) {
                pawnLimit = if (piece.side == Piece.Companion.Side.WHITE && Character.getNumericValue(currentPosition[1]) == 2) 1
                else if (piece.side == Piece.Companion.Side.BLACK && Character.getNumericValue(currentPosition[1]) == 7) 1
                else 0
            }
            while (
                checkingPos[0] in 'a'..'h' &&
                Character.getNumericValue(checkingPos[1]) in 1..8 &&
                limit <= it.limit + pawnLimit
            ) {
                val id = checkingPos[0].toString() + Character.getNumericValue(checkingPos[1])
                if (_board.value!![id] != null) {
                    break
                } else {
                    availableMoves.add(id)
                }
                checkingPos[0] = checkingPos[0] + it.x
                checkingPos[1] = checkingPos[1] + it.y
                limit ++
            }
        }

        return availableMoves
    }
}