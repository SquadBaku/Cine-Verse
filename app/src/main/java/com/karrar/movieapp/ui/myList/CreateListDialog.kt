package com.karrar.movieapp.ui.myList

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.FragmentCreateListDialogBinding
import com.karrar.movieapp.ui.base.BaseDialog
import com.karrar.movieapp.ui.myList.myListUIState.MyListUIEvent
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateListDialog : BaseDialog<FragmentCreateListDialogBinding>() {

    override val layoutIdFragment = R.layout.fragment_create_list_dialog
    override val viewModel: MyListsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        collectLast(viewModel.myListUIEvent) {
            it.peekContent()?.let {
                if (it is MyListUIEvent.CLickAddEvent) {
                    dismissDialog()
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            dismissDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setGravity(android.view.Gravity.BOTTOM)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            setBackgroundDrawableResource(android.R.color.transparent)

            val params = attributes
            val marginInPx = (16 * context.resources.displayMetrics.density).toInt()
            params.width = context.resources.displayMetrics.widthPixels - (marginInPx * 2)
            params.horizontalMargin = 0f
            attributes = params
        }
    }

    private fun dismissDialog() {
        this.dismiss()
    }

}