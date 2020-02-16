package com.sample.roomandpaginlibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.roomandpaginlibrary.net.LingvoItem
import kotlinx.android.synthetic.main.word_cell.view.*

class LingvoPagedAdapter: PagedListAdapter<LingvoItem, LingvoPagedAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val concert: LingvoItem? = getItem(position)

        // Note that "concert" is a placeholder if it's null.
        holder.bindTo(concert)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<LingvoItem>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldConcert: LingvoItem,
                newConcert: LingvoItem
            ) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(
                oldConcert: LingvoItem,
                newConcert: LingvoItem
            ) = oldConcert == newConcert
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.name
        val translation = view.translation

        fun bindTo(data: LingvoItem?) {
            name.text = data?.title
            translation.text = data?.dictionary + " / " + data?.articleId
        }
    }
}