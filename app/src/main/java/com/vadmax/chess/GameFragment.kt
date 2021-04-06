package com.vadmax.chess

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.vadmax.chess.pieces.Piece
import kotlinx.android.synthetic.main.game_fragment.*

class GameFragment : Fragment(), SelectPieceDialog.SelectPieceDialogListener, EndGameDialog.DismissListener {

    private lateinit var viewModel: GameViewModel
    private val boardId = "square"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        var letter = 'a'
        while (letter <= 'h') {
            for (num in 1..8) {
                val currentLetter = letter
                getCellById("$letter$num").setOnClickListener {
                    val id = "$currentLetter$num"
                    viewModel.onClickEvent(id)
                }
            }
            letter++
        }

        viewModel.takenPieces.observe(viewLifecycleOwner, {
            it.sortBy { piece -> piece.cost }
            white_win_pieces.removeAllViews()
            var sumWhite = 0
            it.filter { piece -> piece.side == Piece.Companion.Side.WHITE }.forEach { piece ->
                sumWhite += piece.cost
                addTakenPiece(piece, white_win_pieces)
            }

            black_win_pieces.removeAllViews()
            var sumBlack = 0
            it.filter { piece -> piece.side == Piece.Companion.Side.BLACK }.forEach { piece ->
                sumBlack += piece.cost
                addTakenPiece(piece, black_win_pieces)
            }

            val textView = TextView(ContextThemeWrapper(requireContext(), R.style.score_label))
            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) // value is in pixels
            textView.setPadding(10, 0, 0, 0)
            val difference = sumWhite - sumBlack
            if (difference > 0) {
                textView.text = "+$difference"
                white_win_pieces.addView(textView)
            } else if (difference < 0) {
                textView.text = "+" + kotlin.math.abs(difference)
                black_win_pieces.addView(textView)
            }
        })

        viewModel.board.observe(viewLifecycleOwner, { board ->
            board.forEach {
                updateCell(it.key, it.value)
            }
        })

        viewModel.clearSelection.observe(viewLifecycleOwner, {
            val cell = getCellById(it)
            cell.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    android.R.color.transparent
                )
            )
            cell.background = ContextCompat.getDrawable(
                requireContext(),
                android.R.color.transparent
            )
        })

        viewModel.cancelSelection.observe(viewLifecycleOwner, {
            val cell = getCellById(it)
            cell.background = ContextCompat.getDrawable(
                requireContext(),
                android.R.color.transparent
            )
        })

        viewModel.setSelection.observe(viewLifecycleOwner, {
            val cell = getCellById(it)
            cell.background = ContextCompat.getDrawable(requireContext(), R.color.cell_selection)
        })

        viewModel.showAvailableMoves.observe(viewLifecycleOwner, {
            it.forEach { id ->
                val cell = getCellById(id)
                cell.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_available_move_round
                )
            }
        })

        viewModel.clearAvailableMoves.observe(viewLifecycleOwner, {
            it.forEach { id ->
                val cell = getCellById(id)
                cell.background = ContextCompat.getDrawable(
                    requireContext(),
                    android.R.color.transparent
                )
            }
        })

        viewModel.move.observe(viewLifecycleOwner, {
            updateCell(it.from)
            updateCell(it.to, it.piece)
            viewModel.checkForCheckmateWin(it.piece.side)
        })

        viewModel.showPieceDialog.observe(viewLifecycleOwner, {
            val dialog = SelectPieceDialog(it)
            val manager: FragmentManager = parentFragmentManager
            dialog.setTargetFragment(this, 0)
            dialog.show(manager, "Piece dialog")
        })

        viewModel.gameOverText.observe(viewLifecycleOwner, {
            val dialog = EndGameDialog(it)
            val manager: FragmentManager = parentFragmentManager
            dialog.setTargetFragment(this, 0)
            dialog.show(manager, "End game dialog")
        })

        viewModel.initBoard()
    }

    private fun addTakenPiece(piece: Piece, view: LinearLayout) {
        val imageView = ImageView(requireContext())
        imageView.layoutParams = LinearLayout.LayoutParams(50, 50) // value is in pixels
        val stream =
                requireActivity().assets.open("pieces/" + App.APP_PIECES_THEME + "/" + piece.image)
        val drawable = Drawable.createFromStream(stream, null)
        imageView.setImageDrawable(drawable)
        view.addView(imageView)
    }

    private fun updateCell(id: String, piece: Piece? = null) {
        val cell = getCellById(id)
        if (piece != null) {
//            Glide.with(requireContext())
//                .asBitmap()
//                .load(Uri.parse("file:///android_asset/" + "pieces/" + App.APP_PIECES_THEME + "/" + piece.image))
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(
//                        resource: Bitmap,
//                        transition: Transition<in Bitmap>?
//                    ) {
//                        val background = if (select) {
//                            ContextCompat.getDrawable(requireContext(), R.color.cell_selection)
//                        } else {
//                            ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
//                        }
//
//                        val layers = arrayOf(background, resource.toDrawable(requireContext().resources))
//                        cell.setImageDrawable(LayerDrawable(layers))
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {}
//                })

            Glide.with(requireContext())
                .asBitmap()
                .load(Uri.parse("file:///android_asset/" + "pieces/" + App.APP_PIECES_THEME + "/" + piece.image))
                .into(cell)
        } else {
            cell.setImageResource(android.R.color.transparent)
        }
    }

    private fun getCellById(id: String) = requireActivity().findViewById<ImageView>(
        resources.getIdentifier(
            "${boardId}_$id",
            "id",
            requireContext().packageName
        )
    )

    override fun selectPiece(piece: Piece) {
        viewModel.selectPiece(piece)
    }

    override fun dismiss() {
        viewModel.initBoard()
    }
}
