package com.candroid.pazaramafinalproject.presentation.view.ui

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.candroid.pazaramafinalproject.R
import com.candroid.pazaramafinalproject.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var counter = 0
        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationEnd(p0: Animator) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationCancel(p0: Animator) {
                //TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator) {
                counter++
                if (counter == 2){
                    binding.animationView.pauseAnimation()
                    Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_homeFragment)
                }
            }

        })
    }
}