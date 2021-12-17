package sku.challenge.wiznphotos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sku.challenge.wiznphotos.databinding.ListItemBinding
import sku.challenge.wiznphotos.vo.PhotoItem

class ListViewAdapter(
    private val listener: (item: PhotoItem) -> Unit
) : ListAdapter<PhotoItem, RecyclerView.ViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListItemViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            listener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ListItemViewHolder
        holder.bind(getItem(position))
    }

    class ListItemViewHolder(
        private val binding: ListItemBinding,
        private val listener: (item: PhotoItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener(binding.item!!)
            }
        }

        fun bind(item: PhotoItem) {
            binding.item = item

            binding.executePendingBindings()
        }

    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<PhotoItem>() {

            override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem) =
                oldItem == newItem

        }
    }
}