package sku.challenge.wiznphotos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sku.challenge.wiznphotos.databinding.HorizontalListItemBinding
import sku.challenge.wiznphotos.vo.PhotoItem

// TODO: rename to ViewPagerPagedAdapter
//  actually I should extract that paged functionality to differentClass
class ViewPagerItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = (0 until (100)).map { id: Int ->
        PhotoItem(
            id,
            "title: $id",
            "https://example.com/dummy/$id.jpg"
        )
    }

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
        return items.size
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