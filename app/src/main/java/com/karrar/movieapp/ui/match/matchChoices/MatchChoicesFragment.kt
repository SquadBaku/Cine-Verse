package com.karrar.movieapp.ui.match.matchChoices

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchChoicesBinding
import com.karrar.movieapp.ui.base.BaseFragment

class MatchChoicesFragment : BaseFragment<FragmentMatchChoicesBinding>() {
    override val layoutIdFragment = R.layout.fragment_match_choices
    override val viewModel: MatchChoicesViewModel by viewModels()
    private var counter = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
        initButton()
        initBackAction()
        init()
    }


    private fun init() {
        makeButtonNotClickable()
    }

    private fun initButton() {
        initNextButtonAction()
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun initNextButtonAction() {
        binding.startMatchingButton.setOnClickListener {
            when (counter) {
                0 -> {
                    counter++
                    makeButtonNotClickable()
                    handleLoadingColorLocation(binding.second.id)
                }

                1 -> {
                    counter++
                    makeButtonNotClickable()
                    handleLoadingColorLocation(binding.third.id)
                }

                2 -> {
                    counter++
                    makeButtonNotClickable()
                    handleLoadingColorLocation(binding.loader.id)
                    changeButtonText(getString(R.string.start_matching))
                }

                3 -> {

                }
            }
        }
    }

    private fun initBackAction() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when (counter) {
                0 -> {
                    findNavController().popBackStack()
                }

                1 -> {
                    counter--
                    handleLoadingColorLocation(binding.first.id)
                    makeButtonClickable()
                }

                2 -> {
                    counter--
                    handleLoadingColorLocation(binding.second.id)
                    makeButtonClickable()
                }

                3 -> {
                    counter--
                    handleLoadingColorLocation(binding.third.id)
                    makeButtonClickable()
                    changeButtonText(getString(R.string.next))
                }
            }
        }
    }

    private fun changeButtonText(text : String ) {
        binding.buttonText.text = text
    }

    private fun makeButtonNotClickable() {
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.rectancle_button_disable)
        binding.startMatchingButton.background = drawable
        binding.startMatchingButton.isClickable = false

    }

    private fun makeButtonClickable() {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.rectancle_button)
        binding.startMatchingButton.background = drawable
        binding.startMatchingButton.isClickable = true

    }

    private fun handleLoadingColorLocation(idOfEndWithStop: Int) {
        val params = binding.loadingColor.layoutParams as ConstraintLayout.LayoutParams
        params.endToEnd = idOfEndWithStop
        binding.loadingColor.layoutParams = params
    }

    //    View.VISIBLE
//    View.INVISIBLE
//    View.GONE


}