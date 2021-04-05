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
import kotlinx.android.synthetic.main.end_game_dialog_fragment.*
import kotlinx.android.synthetic.main.select_piece_dialog_fragment.*

class EndGameDialog(private val text: String): DialogFragment() {

    private var listener: DismissListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_round_corner)
        return inflater.inflate(R.layout.end_game_dialog_fragment, container, false)
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

        end_game_tv.text = text

        end_game_btn.setOnClickListener {
            listener!!.dismiss()
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as DismissListener
    }

    interface DismissListener {
        fun dismiss()
    }
}
