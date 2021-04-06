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

    private var _gameOverText = SingleLiveEvent<String>()
    val gameOverText: LiveData<String>
        get() = _gameOverText
    
    private var selection: String? = null
    private var castleOptions: Map<Piece.Companion.Side, MutableMap<String, Boolean?>> = getDefaultCastleParams()
    private var currentMove = Piece.Companion.Side.WHITE

    fun initBoard() {
        val map: MutableMap<String, Piece?> = mutableMapOf()
        var letter = 'a'
        while (letter <= 'h') {
            for (num in 1..8) {
                map["$letter$num"] = null
            }
            letter++
        }

        map["a1"] = Rook(Piece.Companion.Side.WHITE)
        map["b1"] = Knight(Piece.Companion.Side.WHITE)
        map["c1"] = Bishop(Piece.Companion.Side.WHITE)
        map["d1"] = Queen(Piece.Companion.Side.WHITE)
        map["e1"] = King(Piece.Companion.Side.WHITE)
        map["f1"] = Bishop(Piece.Companion.Side.WHITE)
        map["g1"] = Knight(Piece.Companion.Side.WHITE)
        map["h1"] = Rook(Piece.Companion.Side.WHITE)
        map["a2"] = Pawn(Piece.Companion.Side.WHITE)
        map["b2"] = Pawn(Piece.Companion.Side.WHITE)
        map["c2"] = Pawn(Piece.Companion.Side.WHITE)
        map["d2"] = Pawn(Piece.Companion.Side.WHITE)
        map["e2"] = Pawn(Piece.Companion.Side.WHITE)
        map["f2"] = Pawn(Piece.Companion.Side.WHITE)
        map["g2"] = Pawn(Piece.Companion.Side.WHITE)
        map["h2"] = Pawn(Piece.Companion.Side.WHITE)

        map["a8"] = Rook(Piece.Companion.Side.BLACK)
        map["b8"] = Knight(Piece.Companion.Side.BLACK)
        map["c8"] = Bishop(Piece.Companion.Side.BLACK)
        map["d8"] = Queen(Piece.Companion.Side.BLACK)
        map["e8"] = King(Piece.Companion.Side.BLACK)
        map["f8"] = Bishop(Piece.Companion.Side.BLACK)
        map["g8"] = Knight(Piece.Companion.Side.BLACK)
        map["h8"] = Rook(Piece.Companion.Side.BLACK)
        map["a7"] = Pawn(Piece.Companion.Side.BLACK)
        map["b7"] = Pawn(Piece.Companion.Side.BLACK)
        map["c7"] = Pawn(Piece.Companion.Side.BLACK)
        map["d7"] = Pawn(Piece.Companion.Side.BLACK)
        map["e7"] = Pawn(Piece.Companion.Side.BLACK)
        map["f7"] = Pawn(Piece.Companion.Side.BLACK)
        map["g7"] = Pawn(Piece.Companion.Side.BLACK)
        map["h7"] = Pawn(Piece.Companion.Side.BLACK)

        _board.value = map.toMutableMap()
        currentMove = Piece.Companion.Side.WHITE
        castleOptions = getDefaultCastleParams()
    }

    fun onClickEvent(id: String) {
        if (selection == null) {
            if (_board.value!![id] != null && currentMove == _board.value!![id]!!.side) {
                selection = id
                _setSelection.value = id
                _showAvailableMoves.value = getAvailableMoves()
            }
        } else {
            val piece = _board.value!![selection]
            if (piece != null && getAvailableMoves().contains(id)) {
                if (_board.value!![id] != null) {
                    _takenPiece.value = _board.value!![id]!!
                } else if (move.value != null && isPassant(piece, selection!![1], id)) {
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
                _board.value!![id] = piece
                _board.value!![selection!!] = null
                _move.value = Move(selection!!, id, piece)

                val opponentSide = if (piece.side == Piece.Companion.Side.WHITE) Piece.Companion.Side.BLACK
                else Piece.Companion.Side.WHITE
                currentMove = opponentSide

                if (piece is King || piece is Rook) {
                    if (piece is King) {
                        castleOptions[piece.side]!![SHORT] = null
                        castleOptions[piece.side]!![LONG] = null
                        if (piece.side == Piece.Companion.Side.WHITE) {
                            if (selection!! == "e1" && id == "g1") {
                                makeRookCastle("h1", "f1", Piece.Companion.Side.WHITE)
                            } else if (selection!! == "e1" && id == "c1") {
                                makeRookCastle("a1", "d1", Piece.Companion.Side.WHITE)
                            }
                        } else {
                            if (selection!! == "e8" && id == "g8") {
                                makeRookCastle("h8", "f8", Piece.Companion.Side.BLACK)
                            } else if (selection!! == "e8" && id == "c8") {
                                makeRookCastle("a8", "d8", Piece.Companion.Side.BLACK)
                            }
                        }
                    } else if (piece is Rook) {
                        if (move.value!!.from == "a1" || move.value!!.from == "a8") {
                            castleOptions[piece.side]!![LONG] = null
                        } else if (move.value!!.from == "h1" || move.value!!.from == "h8") {
                            castleOptions[piece.side]!![SHORT] = null
                        }
                    }
                }

                // Check for opponent castling settings
                if (castleOptions[opponentSide]!![SHORT] != null) {
                    if (opponentSide == Piece.Companion.Side.WHITE) {
                        val shah = isShahForSide(null, opponentSide) ||
                            isShahForSide(Move("e1", "f1", King(opponentSide)), opponentSide) ||
                            isShahForSide(Move("e1", "g1", King(opponentSide)), opponentSide)

                        castleOptions[opponentSide]!![SHORT] = !shah
                        if (_board.value!!["h1"] == null || _board.value!!["h1"] !is Rook || _board.value!!["h1"]!!.side != opponentSide) {
                            castleOptions[opponentSide]!![LONG] = null
                        }
                    } else {
                        val shah = isShahForSide(null, opponentSide) ||
                            isShahForSide(Move("e8", "f8", King(opponentSide)), opponentSide) ||
                            isShahForSide(Move("e8", "g8", King(opponentSide)), opponentSide)

                        castleOptions[opponentSide]!![SHORT] = !shah
                        if (_board.value!!["h8"] == null || _board.value!!["h8"] !is Rook || _board.value!!["h8"]!!.side != opponentSide) {
                            castleOptions[opponentSide]!![SHORT] = null
                        }
                    }
                }

                if (castleOptions[opponentSide]!![LONG] != null) {
                    if (opponentSide == Piece.Companion.Side.WHITE) {
                        val shah = isShahForSide(null, opponentSide) ||
                            isShahForSide(Move("e1", "d1", King(opponentSide)), opponentSide) ||
                            isShahForSide(Move("e1", "c1", King(opponentSide)), opponentSide)

                        castleOptions[opponentSide]!![LONG] = !shah
                        if (_board.value!!["a1"] == null || _board.value!!["a1"] !is Rook || _board.value!!["a1"]!!.side != opponentSide) {
                            castleOptions[opponentSide]!![LONG] = null
                        }
                    } else {
                        val shah = isShahForSide(null, opponentSide) ||
                            isShahForSide(Move("e8", "d8", King(opponentSide)), opponentSide) ||
                            isShahForSide(Move("e8", "c8", King(opponentSide)), opponentSide)

                        castleOptions[opponentSide]!![LONG] = !shah
                        if (_board.value!!["a8"] == null || _board.value!!["a8"] !is Rook || _board.value!!["a8"]!!.side != opponentSide) {
                            castleOptions[opponentSide]!![LONG] = null
                        }
                    }
                }

                _clearSelection.value = selection!!
            } else if (piece != null && !getAvailableMoves().contains(id)) {
                _cancelSelection.value = selection!!
            }

            _clearAvailableMoves.value = _showAvailableMoves.value
            selection = null
        }
    }

    fun selectPiece(piece: Piece) {
        val id = _move.value!!.to
        _board.value!![id] = piece
        _move.value = Move(id, id, piece)
    }

    private fun makeRookCastle(from: String, to: String, side: Piece.Companion.Side) {
        _move.value = Move(from, to, Rook(side))
        _board.value!![from] = null
        _board.value!![to] = Rook(side)
    }

    private fun getAvailableMoves(
        checkShah: Boolean = false,
        currentPosition: String = selection!!,
        board: MutableMap<String, Piece?> = _board.value!!
    ): List<String> {
        val availableMoves = mutableListOf<String>()
        val checkingPos = CharArray(2)
        val piece = board[currentPosition]!!
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

                val canMove = if (!checkShah) !isShahForSide(Move(currentPosition, id, piece), piece.side) else true
                if (canMove) {
                    if (board[id] != null) {
                        if (piece.side != board[id]!!.side) {
                            if (piece is Pawn && it.x != 0 || piece !is Pawn) {
                                if (checkShah || board[id]!! !is King) {
                                    availableMoves.add(id)
                                }
                            }
                        }
                        break
                    } else if (move.value != null && isPassant(piece, currentPosition[1], id)) {
                        availableMoves.add(id)
                    } else if (piece is King && it.limit == 2) {
                        if (it.x == -1 && castleOptions[piece.side]!![LONG] == true) {
                            availableMoves.add(id)
                        } else if (it.x == 1 && castleOptions[piece.side]!![SHORT] == true) {
                            availableMoves.add(id)
                        }
                    } else {
                        if (piece is Pawn && it.x == 0 || piece !is Pawn) {
                            availableMoves.add(id)
                        }
                    }
                }
                checkingPos[0] = checkingPos[0] + it.x
                checkingPos[1] = checkingPos[1] + it.y
                limit ++
            }
        }

        return availableMoves
    }

    private fun isPassant(currentPiece: Piece, line: Char, currentId: String): Boolean {
        if (currentPiece is Pawn && _board.value!![currentId] == null && move.value!!.piece.side != currentPiece.side &&
            move.value!!.piece is Pawn && move.value!!.from[0] == currentId[0]) {
            val length = Character.getNumericValue(move.value!!.from[1]) - Character.getNumericValue(move.value!!.to[1])
            if (kotlin.math.abs(length) == 2) {
                if (currentPiece.side == Piece.Companion.Side.WHITE && line == '5') {
                    return true
                } else if (currentPiece.side == Piece.Companion.Side.BLACK && line == '4') {
                    return true
                }
            }
        }
        return false
    }

    private fun isShahForSide(move: Move?, side: Piece.Companion.Side): Boolean {
        val checkBoard = _board.value!!.toMutableMap()
        if (move != null && checkBoard[move.to] !is King) {
            checkBoard[move.from] = null
            checkBoard[move.to] = move.piece
        }
        var shah = false
        val kingPos = checkBoard.filterValues { it != null && it.side == side && it is King }.keys.elementAt(0)
        checkBoard.filterValues { it != null && it.side != side }.forEach {
            if (getAvailableMoves(true, it.key, checkBoard).contains(kingPos)) {
                shah = true
                return@forEach
            }
        }

        return shah
    }

    private fun getDefaultCastleParams(): Map<Piece.Companion.Side, MutableMap<String, Boolean?>> = mapOf(
        Pair(Piece.Companion.Side.WHITE, mutableMapOf(Pair(SHORT, true), Pair(LONG, true))),
        Pair(Piece.Companion.Side.BLACK, mutableMapOf(Pair(SHORT, true), Pair(LONG, true))),
    )

    fun checkForCheckmateWin(side: Piece.Companion.Side) {
        val opponentSide = if (side == Piece.Companion.Side.WHITE) Piece.Companion.Side.BLACK
        else Piece.Companion.Side.WHITE

        val shah = isShahForSide(null, opponentSide)

        var canMove = false
        board.value!!.filterValues { it != null && it.side == opponentSide }.forEach {
            if (!canMove && getAvailableMoves(false, it.key).count() > 0) {
                canMove = true
            }
        }

        if (!canMove) {
            if (shah) {
                _gameOverText.value = side.side + " WON"
            } else {
                _gameOverText.value = "Draw"
            }
        }
    }

    companion object {
        const val SHORT = "short"
        const val LONG = "long"
    }
}
