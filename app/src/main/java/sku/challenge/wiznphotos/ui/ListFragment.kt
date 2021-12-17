package sku.challenge.wiznphotos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sku.challenge.wiznphotos.R
import sku.challenge.wiznphotos.databinding.FragmentListBinding
import sku.challenge.wiznphotos.vo.PhotoItem

@AndroidEntryPoint
class ListFragment : Fragment() {


    private var _binding: FragmentListBinding? = null

    private val binding: FragmentListBinding
        get() = _binding!!

    private val listViewModel by viewModels<ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.listView.adapter = ListViewAdapter {
            findNavController().navigate(ListFragmentDirections.actionToItemFragment(it.id))
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                listViewModel.photosItems.collect { result ->
                    when (result) {
                        is ListViewModel.PhotosResult.Loading -> showProgressBar()
                        is ListViewModel.PhotosResult.Success -> updateList(result.data)
                    }
                }
            }
        }
    }

    private fun updateList(data: List<PhotoItem>) {
        (binding.listView.adapter as ListViewAdapter)
            .submitList(data)
        hideProgressBar()
    }


    private fun showProgressBar() {
        binding.progressIndicator.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}