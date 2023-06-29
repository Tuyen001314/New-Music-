package com.example.baseprojectandroid.ui.component.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSearchBinding
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.hideLoadingDialog
import com.example.baseprojectandroid.extension.showLoadingDialog
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment: BaseFragmentBinding<FragmentSearchBinding, SearchViewModel>() {

    private lateinit var adapter: SearchAdapter
    private var searchEditText: SearchView.SearchAutoComplete? = null

    override fun getContentViewId(): Int = R.layout.fragment_search

    override fun initializeViews() {
        super.initializeViews()

        searchEditText =
            dataBinding.searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete

        dataBinding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                /*dataBinding.rvSuggestion.gone()
                dataBinding.searchRecently.gone()
                dataBinding.rvSearch.visible()*/
                handleSearchFile(query)
                hiddenKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun handleSearchFile(query: String) {
        requireActivity().showLoadingDialog()

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.handleSearch("a", 1).collect {
                requireActivity().hideLoadingDialog()
            }
        }
    }
}
