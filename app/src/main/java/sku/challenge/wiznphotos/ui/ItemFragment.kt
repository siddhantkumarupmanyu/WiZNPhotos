package sku.challenge.wiznphotos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sku.challenge.wiznphotos.R
import sku.challenge.wiznphotos.databinding.FragmentItemBinding


@AndroidEntryPoint
class ItemFragment : Fragment() {

    // TODO

    private var _binding: FragmentItemBinding? = null

    private val binding: FragmentItemBinding
        get() = _binding!!

    private val itemViewModel by viewModels<ItemViewModel>()

    private val args by navArgs<ItemFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_item,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        // adapter = ViewPagerItemAdapter()
        // binding.pager.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                itemViewModel.currentItem.collect { result ->
                    when (result) {
                        is ItemViewModel.PhotoItemResult.Loading -> {} // no op
                        is ItemViewModel.PhotoItemResult.Success -> updateData(result)
                        is ItemViewModel.PhotoItemResult.NoData -> TODO()
                    }
                }
            }
        }

        setUpArrows()

        itemViewModel.loadItem(args.id)
    }

    private fun setUpArrows() {
        binding.nextArrow.setOnClickListener {
            itemViewModel.loadNextItem()
        }

        binding.previousArrow.setOnClickListener {
            itemViewModel.loadPreviousItem()
        }
    }

    private fun updateData(result: ItemViewModel.PhotoItemResult.Success) {

        binding.photoItem = result.currentItem

        val url = GlideUrl(
            result.currentItem.url, LazyHeaders.Builder()
                .addHeader("User-Agent", WebSettings.getDefaultUserAgent(binding.imageView.context))
                .build()
        )
        Glide.with(binding.imageView.context)
            .load(url)
            // .transition()
            .placeholder(R.drawable.no_thumbnail)
            .into(binding.imageView)

        binding.previousArrow.visibility =
            if (result.prevItemAvailable) View.VISIBLE else View.INVISIBLE
        binding.nextArrow.visibility =
            if (result.nextItemAvailable) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}