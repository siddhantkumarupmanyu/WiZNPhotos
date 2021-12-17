package sku.challenge.wiznphotos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sku.challenge.wiznphotos.R
import sku.challenge.wiznphotos.databinding.FragmentItemBinding
import sku.challenge.wiznphotos.vo.PhotoItem


@AndroidEntryPoint
class ItemFragment : Fragment() {

    // TODO

    private var _binding: FragmentItemBinding? = null

    private val binding: FragmentItemBinding
        get() = _binding!!

    private lateinit var adapter: ViewPagerItemAdapter

    private var items = (0 until (100)).map { id: Int ->
        PhotoItem(
            id,
            "title: $id",
            "https://example.com/dummy/$id.jpg"
        )
    }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}