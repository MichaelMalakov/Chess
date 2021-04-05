package com.vadmax.chess

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.vadmax.chess.pieces.*
import kotlinx.android.synthetic.main.select_piece_dialog_fragment.*

class SelectPieceDialog(
    val side: Piece.Companion.Side
): DialogFragment() {

    private var listener: SelectPieceDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_round_corner)
        return inflater.inflate(R.layout.select_piece_dialog_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        //val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.setCancelable(false)
        dialog!!.window?.setWindowAnimations(R.style.pieces_dialog_animation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (side == Piece.Companion.Side.BLACK) {
            setDrawableToImageView(Queen(Piece.Companion.Side.BLACK), dialog_queen)
            setDrawableToImageView(Rook(Piece.Companion.Side.BLACK), dialog_rook)
            setDrawableToImageView(Bishop(Piece.Companion.Side.BLACK), dialog_bishop)
            setDrawableToImageView(Knight(Piece.Companion.Side.BLACK), dialog_knight)
        } else {
            setDrawableToImageView(Queen(Piece.Companion.Side.WHITE), dialog_queen)
            setDrawableToImageView(Rook(Piece.Companion.Side.WHITE), dialog_rook)
            setDrawableToImageView(Bishop(Piece.Companion.Side.WHITE), dialog_bishop)
            setDrawableToImageView(Knight(Piece.Companion.Side.WHITE), dialog_knight)
        }
    }

    private fun setDrawableToImageView(piece: Piece, view: ImageView) {
        val queenStream = requireActivity().assets.open("pieces/" + App.APP_PIECES_THEME + "/" + piece.image)
        val queenDrawable = Drawable.createFromStream(queenStream, null)
        view.setImageDrawable(queenDrawable)
        view.setOnClickListener {
            listener!!.selectPiece(piece)
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as SelectPieceDialogListener
    }

    interface SelectPieceDialogListener {
        fun selectPiece(piece: Piece)
    }
}
