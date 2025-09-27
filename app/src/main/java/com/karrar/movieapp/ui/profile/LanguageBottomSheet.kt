package com.karrar.movieapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karrar.movieapp.databinding.DialogLanguageBinding
import com.karrar.movieapp.utilities.PrefsManager

class LanguageBottomSheet : BottomSheetDialogFragment() {

    private var _binding: DialogLanguageBinding? = null
    private val binding get() = _binding!!

    private var onLanguageSelected: ((String) -> Unit)? = null

    fun setOnLanguageSelectedListener(callback: (String) -> Unit) {
        onLanguageSelected = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentLang = PrefsManager.getLanguage(requireContext())
        updateSelection(currentLang)

        binding.btnEnglish.setOnClickListener {
            onLanguageSelected?.invoke("en")
            updateSelection("en")
            dismiss()
        }

        binding.btnArabic.setOnClickListener {
            onLanguageSelected?.invoke("ar")
            updateSelection("ar")
            dismiss()
        }
    }

    private fun updateSelection(lang: String) {
        if (lang == "en") {
            binding.btnEnglish.isSelected = true
            binding.btnArabic.isSelected = false
        } else {
            binding.btnArabic.isSelected = true
            binding.btnEnglish.isSelected = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}