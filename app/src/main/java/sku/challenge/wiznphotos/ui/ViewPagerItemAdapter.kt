package sku.challenge.wiznphotos.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sku.challenge.wiznphotos.databinding.HorizontalListItemBinding
import sku.challenge.wiznphotos.vo.PhotoItem

class ViewPagerItemAdapter(
    private val items: List<PhotoItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // should I replace List with array, since can
    private var previousItems = listOf<PhotoItem>()
    private var currentItems = listOf<PhotoItem>()
    private var nextItems = listOf<PhotoItem>()

    private var totalItems: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewPagerItemHolder(
            HorizontalListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewPagerItemHolder


        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return totalItems
    }

    fun deleteItem() {
        TODO()
    }


    class ViewPagerItemHolder(
        private val binding: HorizontalListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoItem) {
            // binding.photoItem = item

            binding.executePendingBindings()
        }

    }

}