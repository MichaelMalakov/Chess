package com.vadmax.chess

import android.animation.ValueAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.main_menu_fragment.*

class MainMenuFragment : Fragment() {

    private lateinit var viewModel: MainMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_menu_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainMenuViewModel::class.java)

        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = 10000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        animator.start()

        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            if (iv_menu_image != null) {
                iv_menu_image.rotation = animatedValue
            }
        }

        btn_menu_start.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_mainMenuFragment_to_gameFragment)
        }

        btn_menu_exit.setOnClickListener{
            requireActivity().finish()
        }
    }
}
