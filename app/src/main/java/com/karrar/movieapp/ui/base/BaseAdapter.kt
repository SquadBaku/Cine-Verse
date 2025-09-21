package com.karrar.movieapp.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karrar.movieapp.BR

interface BaseInteractionListener

abstract class BaseAdapter<T>(
    private var items: List<T>,
    private val listener: BaseInteractionListener,
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {

    abstract val layoutID: Int

    class ItemViewHolder(val binding: ViewDataBinding) : BaseViewHolder(binding)
    abstract class BaseViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), layoutID, parent, /* attachToParent = */ false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ItemViewHolder) bind(holder, position)
    }

    open fun bind(holder: ItemViewHolder, position: Int) {
        holder.binding.apply {
            setVariable(BR.item, items[position])
            setVariable(BR.listener, listener)
            executePendingBindings()
        }
    }


    open fun foregroundOf(holder: BaseViewHolder): View? = null

    fun getItems(): List<T> = items
    fun getItemCountSafe(): Int = items.size
    override fun getItemCount(): Int = items.size

    fun getItemOrNull(position: Int): T? = items.getOrNull(position)

    fun getItemAt(position: Int): T = items[position]


    open fun setItems(newItems: List<T>) {
        val diff = DiffUtil.calculateDiff(
            BaseDiffUtil(items, newItems, ::areItemsSame, ::areContentSame)
        )
        items = newItems
        diff.dispatchUpdatesTo(this)
    }


    open fun removeAt(position: Int): T? {
        if (position !in items.indices) return null
        val current = items.toMutableList()
        val removed = current.removeAt(position)
        setItems(current)
        return removed
    }

    open fun removeItem(item: T): Boolean {
        val idx = items.indexOf(item)
        if (idx == -1) return false
        removeAt(idx)
        return true
    }

    open fun areItemsSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
    open fun areContentSame(oldItem: T, newItem: T): Boolean = true
}
