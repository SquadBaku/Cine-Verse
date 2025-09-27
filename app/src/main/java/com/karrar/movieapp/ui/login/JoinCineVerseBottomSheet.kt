package com.karrar.movieapp.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.karrar.movieapp.BuildConfig
import com.karrar.movieapp.R
import androidx.core.net.toUri

class JoinCineVerseBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_join_cineverse, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.btnCancel).setOnClickListener {
            dismiss()
        }

        view.findViewById<MaterialButton>(R.id.btnWebsite).setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, BuildConfig.TMDB_SIGNUP_URL.toUri())
            startActivity(browserIntent)
        }
    }
}
