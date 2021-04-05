package com.vadmax.chess

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.vadmax.chess.pieces.Piece


class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private val boardId = "square"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

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

        viewModel.board.observe(viewLifecycleOwner, { board ->
            board.forEach {
                updateCell(it.key, it.value)
            }
        })

        viewModel.clearSelection.observe(viewLifecycleOwner, {
            val cell = getCellById(it)
            cell.setImageDrawable(ContextCompat.getDrawable(requireContext(), android.R.color.transparent))
            cell.background = ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        })

        viewModel.cancelSelection.observe(viewLifecycleOwner, {
            val cell = getCellById(it)
            cell.background = ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        })

        viewModel.setSelection.observe(viewLifecycleOwner, {
            val cell = getCellById(it)
            cell.background = ContextCompat.getDrawable(requireContext(), R.color.cell_selection)
        })

        viewModel.showAvailableMoves.observe(viewLifecycleOwner, {
            it.forEach { id ->
                val cell = getCellById(id)
                cell.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_available_move_round)
            }
        })

        viewModel.clearAvailableMoves.observe(viewLifecycleOwner, {
             it.forEach { id ->
                val cell = getCellById(id)
                cell.background = ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
            }
        })

        viewModel.move.observe(viewLifecycleOwner, {
            updateCell(it.from)
            updateCell(it.to, it.piece)
        })

        viewModel.initBoard()
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
        }
    }

    private fun getCellById(id: String) = requireActivity().findViewById<ImageView>(
        resources.getIdentifier(
            "${boardId}_$id",
            "id",
            requireContext().packageName
        )
    )
}
