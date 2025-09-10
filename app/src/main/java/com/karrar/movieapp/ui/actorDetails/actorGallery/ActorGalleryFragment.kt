package com.karrar.movieapp.ui.actorDetails.actorGallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.databinding.FragmentActorGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActorGalleryFragment : Fragment() {

    private var _binding: FragmentActorGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActorGalleryViewModel by viewModels()
    private val adapter = ActorGalleryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerSeasons.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                if (state.isLoading) {
                    // show loading
                }
                if (state.actorImages.isNotEmpty()) {
                    adapter.submitList(state.actorImages)
                }
                state.error?.let {
                    // show error message
                }
                state.backEvent?.getContentIfNotHandled()?.let {
                    findNavController().navigateUp()
                }
            }
        }

        binding.backArrow.setOnClickListener {
            viewModel.onClickBack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
