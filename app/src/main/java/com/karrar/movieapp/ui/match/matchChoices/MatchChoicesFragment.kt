package com.karrar.movieapp.ui.match.matchChoices

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.ChipGroup
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentMatchChoicesBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchChoicesFragment : BaseFragment<FragmentMatchChoicesBinding>() {
    override val layoutIdFragment = R.layout.fragment_match_choices
    override val viewModel: MatchChoicesViewModel by viewModels()
    private var counter = 0
    private var time:String = ""
    private var classicOrRecent:String  = ""
    private val listOfSelectGenre = mutableListOf<String>()
    private val listOfGenre = listOf("Action", "Comedy" , "Drama", "Romance", "Science Fiction" , "Thriller",
                                    "Animation" , "Mystery")

    override fun onStart() {
        super.onStart()
        collectLast(viewModel.matchChoicesEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }
    private fun onEvent(event: MatchChoicesUIEvent) {
        when (event) {
            is MatchChoicesUIEvent.DoneLoadingDataEvent -> {
               //handle navigate to screen result
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(false)
        initButton()
        initBackAction()
        initChipsMood()
        initChipsGenre()
        initChipTime()
        initAgeTime()
        init()

    }

    private fun initAgeTime() {
        binding.recent.setOnClickListener {
            binding.recent.isSelected = true
            binding.classic.isSelected = false
            binding.both.isSelected = false
            makeButtonClickable()
            classicOrRecent = "Recent"
        }
        binding.classic.setOnClickListener {
            binding.recent.isSelected = false
            binding.classic.isSelected = true
            binding.both.isSelected = false
            makeButtonClickable()
            classicOrRecent = "Classic"

        }
        binding.both.setOnClickListener {
            binding.recent.isSelected = false
            binding.classic.isSelected = false
            binding.both.isSelected = true
            makeButtonClickable()
            classicOrRecent = "Both"
        }
    }


    private fun initChipTime() {
        for (i in 0 until binding.timeGroup.childCount) {
            val chip = binding.timeGroup.getChildAt(i)
            chip.setOnClickListener {
                makeSelectFromTime(index = i)
                if(i == 0){
                    time = "Short"
                }else if( i == 1 ){
                    time = "Medium"
                }else if (i == 2 ){
                    time = "long"
                }
                makeButtonClickable()
            }
        }
    }


    private fun makeSelectFromTime(index : Int ){
        for (i in 0 until binding.timeGroup.childCount) {
            val chip = binding.timeGroup.getChildAt(i)
            chip.isSelected = i == index
        }
    }

    private fun initChipsGenre() {
        for (i in 0 until binding.genreGroup.childCount) {
            val chip = binding.genreGroup.getChildAt(i)
            chip.setOnClickListener {
                chip.isSelected = !chip.isSelected
                manageButtonClickable(binding.genreGroup)
            }
        }    }

    private fun initChipsMood() {
        for (i in 0 until binding.moodGroup.childCount) {
            val chip = binding.moodGroup.getChildAt(i)
            chip.setOnClickListener {
                chip.isSelected = !chip.isSelected
                manageButtonClickable(binding.moodGroup)
            }
        }
    }

    private fun manageButtonClickable(chipGroup : ChipGroup){
        for(i in 0 until chipGroup.childCount ){
            val child = chipGroup.getChildAt(i)
            if(child.isSelected){
                makeButtonClickable()
                return
            }
        }
        makeButtonNotClickable()
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

    private fun disAppearAllChipsNotSelected(chipGroup : ChipGroup){
        for(i in 0 until chipGroup.childCount ){
            val child = chipGroup.getChildAt(i)
            if(!child.isSelected){
                child.visibility  = View.GONE
            }else{
                child.alpha = 0.30f
            }
        }
    }
    private fun createListOfSelectGenre(genreGroup: ChipGroup) {
        listOfSelectGenre.clear()
        for(i in 0 until genreGroup.childCount ){
            val child = genreGroup.getChildAt(i)
            if(child.isSelected){
                listOfSelectGenre.add(listOfGenre[i])
            }
        }
    }

    private fun appearAllChipsNotSelected(chipGroup : ChipGroup){
        for(i in 0 until chipGroup.childCount ){
            val child = chipGroup.getChildAt(i)
            child.visibility  = View.VISIBLE
            child.alpha = 1f

        }
    }
    private fun makeAllSelectedFalse(chipGroup : ChipGroup){
        for(i in 0 until chipGroup.childCount ){
            val child = chipGroup.getChildAt(i)
            child.isSelected = false

        }
    }
    private fun initNextButtonAction() {
        binding.startMatchingButton.setOnClickListener {
            when (counter) {
                0 -> {
                    counter++
                    makeButtonNotClickable()
                    handleLoadingColorLocation(binding.second.id)

                    disAppearAllChipsNotSelected(binding.moodGroup)
                    binding.moodTitle.alpha = 0.30f
                    binding.genreTitle.visibility = View.VISIBLE
                    binding.genreGroup.visibility = View.VISIBLE

                    makeAllSelectedFalse(binding.genreGroup)

                }

                1 -> {
                    counter++
                    makeButtonNotClickable()
                    handleLoadingColorLocation(binding.third.id)

                    disAppearAllChipsNotSelected(binding.genreGroup)
                    createListOfSelectGenre(binding.genreGroup)
                    binding.genreTitle.alpha = 0.30f
                    binding.timeGroup.visibility = View.VISIBLE
                    binding.timeTitle.visibility = View.VISIBLE

                    makeAllSelectedFalse(binding.timeGroup)

                }

                2 -> {
                    counter++
                    makeButtonNotClickable()
                    handleLoadingColorLocation(binding.loader.id)
                    changeButtonText(getString(R.string.start_matching))

                    disAppearAllChipsNotSelected(binding.timeGroup)
                    binding.timeTitle.alpha = 0.30f
                    binding.ageTitle.visibility = View.VISIBLE
                    binding.ageGroup.visibility = View.VISIBLE


                    binding.recent.isSelected = false
                    binding.classic.isSelected = false
                    binding.both.isSelected = false
                }

                3 -> {
                    counter++
                    binding.ageTitle.alpha = 0.3f
                    if (binding.recent.isSelected){
                        binding.classic.visibility = View.GONE
                        binding.both.visibility = View.GONE
                        binding.recent.alpha = 0.30f

                    }else if ( binding.classic.isSelected){
                        binding.classic.alpha = 0.30f
                        binding.both.visibility = View.GONE
                        binding.recent.visibility = View.GONE
                    }else{
                        binding.classic.visibility = View.GONE
                        binding.both.alpha =0.30f
                        binding.recent.visibility = View.GONE
                    }

                    viewModel.getMatchMovie(
                        genre = listOfSelectGenre,
                        time = time,
                        classicOrRecent = classicOrRecent
                    )
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
                    appearAllChipsNotSelected(binding.moodGroup)
                    binding.moodTitle.alpha = 1f
                    binding.genreGroup.visibility = View.GONE
                    binding.genreTitle.visibility = View.GONE
                }

                2 -> {
                    counter--
                    handleLoadingColorLocation(binding.second.id)
                    makeButtonClickable()

                    appearAllChipsNotSelected(binding.genreGroup)
                    binding.genreTitle.alpha = 1f
                    binding.timeGroup.visibility = View.GONE
                    binding.timeTitle.visibility = View.GONE
                }

                3 -> {
                    counter--
                    handleLoadingColorLocation(binding.third.id)
                    makeButtonClickable()
                    changeButtonText(getString(R.string.next))

                    appearAllChipsNotSelected(binding.timeGroup)
                    binding.timeTitle.alpha = 1f
                    binding.ageTitle.visibility = View.GONE
                    binding.ageGroup.visibility = View.GONE
                }
                4->{
                    counter--
                        binding.ageTitle.alpha = 1f
                        binding.classic.visibility = View.VISIBLE
                        binding.both.visibility = View.VISIBLE
                        binding.recent.visibility = View.VISIBLE

                        binding.recent.alpha = 1f
                        binding.classic.alpha = 1f
                        binding.both.alpha = 1f
                    viewModel.stopLoadingData()
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
}

