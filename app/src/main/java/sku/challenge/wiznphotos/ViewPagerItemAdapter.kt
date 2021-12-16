package sku.challenge.wiznphotos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sku.challenge.wiznphotos.databinding.HorizontalListItemBinding
import sku.challenge.wiznphotos.vo.PhotoItem


private const val PAGE_SIZE = 10

class ViewPagerItemAdapter(
    private val paginationCallback: PaginationCallback
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

        val currentItemPosition = position % currentItems.size
        val item: PhotoItem = currentItems[currentItemPosition]

        if (currentItemPosition == 0) {
            // load Previous
            currentItems = previousItems
            paginationCallback.loadPrevious(PAGE_SIZE)
        } else if (currentItemPosition == 1) {
            // load next
            currentItems = nextItems
            paginationCallback.loadNext(PAGE_SIZE)
        }

        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return totalItems
    }

    fun setPreviousItems() {
        TODO()
    }

    fun setCurrentItems(currentItems: List<PhotoItem>, totalItems: Int) {
        TODO()
        this.totalItems = totalItems
        notifyDataSetChanged()
    }

    fun setNextItems() {
        TODO()
    }

    class ViewPagerItemHolder(
        private val binding: HorizontalListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoItem) {
            binding.photoItem = item

            binding.executePendingBindings()
        }

    }

}