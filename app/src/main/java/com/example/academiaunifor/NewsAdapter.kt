package com.example.academiaunifor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(
    private val showDelete: Boolean = false,
    private val onItemClick: ((News) -> Unit)? = null,
    private val onDeleteClick: ((News) -> Unit)? = null
) : ListAdapter<News, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.news_title)
        private val author: TextView = itemView.findViewById(R.id.news_author)
        private val date: TextView = itemView.findViewById(R.id.news_date)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btn_delete)

        fun bind(news: News) {
            title.text = news.titulo
            author.text = "Por ${news.autor}"
            date.text = news.timestamp?.let {
                android.text.format.DateFormat.format("dd/MM/yyyy", it)
            } ?: ""

            itemView.setOnClickListener {
                onItemClick?.invoke(news)
            }

            deleteButton.visibility = if (showDelete) View.VISIBLE else View.GONE

            deleteButton.setOnClickListener {
                onDeleteClick?.invoke(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NewsDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem
}