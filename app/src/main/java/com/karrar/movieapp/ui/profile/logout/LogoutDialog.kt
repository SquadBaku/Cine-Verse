package com.karrar.movieapp.ui.profile.logout

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogLogoutBinding
import com.karrar.movieapp.ui.base.BaseDialog
import com.karrar.movieapp.utilities.Constants
import com.karrar.movieapp.utilities.collectLast
import com.karrar.movieapp.utilities.setWidthPercent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutDialog : BaseDialog<DialogLogoutBinding>() {
    override val layoutIdFragment: Int = R.layout.dialog_logout
    override val viewModel: LogoutViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        collectLast(viewModel.logoutUIEvent) {
            it.getContentIfNotHandled()?.let { onEvent(it) }
        }
    }

    private fun onEvent(event: LogoutUIEvent) {
        when (event) {
            LogoutUIEvent.CloseDialogEvent -> dismiss()
            LogoutUIEvent.LogoutEvent -> {
                val action = LogoutDialogDirections.actionLogoutDialogToLoginFragment(Constants.LOGOUT)
                findNavController().navigate(
                    action,
                    navOptions {
                        popUpTo(R.id.profileFragment) {
                            inclusive = true
                        }
                    }
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val params = attributes
            val marginInPx = (16 * resources.displayMetrics.density).toInt()
            val bottomMarginPx = (30 * resources.displayMetrics.density).toInt()

            params.width = resources.displayMetrics.widthPixels - marginInPx * 2
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            params.gravity = android.view.Gravity.CENTER

            attributes = params

            decorView.setPadding(0, 0, 0, bottomMarginPx)
        }
    }


}