package com.sample.roomandpaginlibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.roomandpaginlibrary.model.WordWithTranslations
import kotlinx.android.synthetic.main.word_cell.view.*

class WordPagedAdapter: PagedListAdapter<WordWithTranslations, WordPagedAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val concert: WordWithTranslations? = getItem(position)

        // Note that "concert" is a placeholder if it's null.
        holder.bindTo(concert)
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<WordWithTranslations>() {
            // Concert details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldConcert: WordWithTranslations,
                newConcert: WordWithTranslations
            ) = oldConcert.word.id == newConcert.word.id

            override fun areContentsTheSame(
                oldConcert: WordWithTranslations,
                newConcert: WordWithTranslations
            ) = oldConcert == newConcert
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.name
        val translation = view.translation

        fun bindTo(data: WordWithTranslations?) {
            name.text = data?.word?.name
            translation.text = data?.translations?.firstOrNull()?.name
        }
    }
}