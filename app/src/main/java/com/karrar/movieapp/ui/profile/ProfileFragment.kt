package com.karrar.movieapp.ui.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentProfileBinding
import com.karrar.movieapp.ui.base.BaseFragment
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.LanguageManager
import com.karrar.movieapp.utilities.PrefsManager
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val layoutIdFragment: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(true, getString(R.string.profile))

        val prefs =
            requireContext().getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
        val darkMode = prefs.getBoolean("dark_mode", false)
        binding.switchDarkMode.isChecked = darkMode

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit { putBoolean("dark_mode", isChecked) }
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.language.setOnClickListener {
            viewModel.onClickLanguage()
        }

        collectLast(viewModel.profileUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: ProfileUIEvent) {
        when (event) {
            ProfileUIEvent.DialogLogoutEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToLogoutDialog()
                findNavController().navigate(action)
            }

            ProfileUIEvent.LoginEvent -> {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToLoginFragment(Constants.PROFILE)
                findNavController().navigate(action)
            }

            ProfileUIEvent.RatedMoviesEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToRatedMoviesFragment()
                findNavController().navigate(action)
            }

            ProfileUIEvent.WatchHistoryEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToWatchHistoryFragment()
                findNavController().navigate(action)
            }

            ProfileUIEvent.MyCollectionsEvent -> {
                val action = ProfileFragmentDirections.actionProfileFragmentToMyListFragment()
                findNavController().navigate(action)
            }

            ProfileUIEvent.EditProfileEvent -> {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToEditProfileBottomSheet()
                findNavController().navigate(action)
            }

            ProfileUIEvent.DialogLanguageEvent -> {
                val bottomSheet = LanguageBottomSheet()
                bottomSheet.setOnLanguageSelectedListener { newLang ->
                    PrefsManager.saveLanguage(requireContext(), newLang)
                    LanguageManager.setLocale(requireContext(), newLang)
                    requireActivity().recreate()
                }
                bottomSheet.show(parentFragmentManager, "LanguageBottomSheet")
            }
        }
    }
}