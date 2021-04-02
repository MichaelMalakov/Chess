package com.vadmax.chess

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.game_fragment.*

class GameFragment : Fragment() {

    companion object {
        fun newInstance() = GameFragment()
    }

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        // TODO: Use the ViewModel
        //val parentLayout = cnsl_game as ConstraintLayout

//        var startFromBright = true
//        var img: Int
//        var letter = 'a'
//        while (letter <= 'h') {
//            for (num in 1..8) {
//                img = if (startFromBright && num % 2 != 0) R.drawable.ic_square_bright else R.drawable.ic_square_dark
//                val square = ImageView(requireContext())
//                square.setImageDrawable(ContextCompat.getDrawable(requireContext(), img))
//                square.id = "$letter$num".hashCode()
//                parentLayout.addView(square, 0)
//                //childView.elevation = 1f
//                val set = ConstraintSet()
//                set.clone(parentLayout)
//
//                when (letter) {
//                    'a' -> {
//                        when (num) {
//                            1 -> {
//                                set.connect(square.id, ConstraintSet.TOP, parentLayout.id, ConstraintSet.TOP)
//                                set.connect(square.id, ConstraintSet.START, parentLayout.id, ConstraintSet.START)
//                                set.connect(square.id, ConstraintSet.TOP, parentLayout.id, ConstraintSet.TOP)
//                                set.connect(square.id, ConstraintSet.TOP, parentLayout.id, ConstraintSet.TOP)
//                            }
//
//                            8 -> {
//
//                            }
//
//                            else -> {
//
//                            }
//                        }
//                    }
//
//                    'h' -> {
//
//                    }
//
//                    else -> {
//
//                    }
//                }
//
//            }
//            letter ++
//            startFromBright = !startFromBright
//        }
    }
}