package com.example.baseprojectandroid.ui.component.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSearchBinding
import com.example.baseprojectandroid.evenbus.GoneBottomLayoutEvent
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.hideLoadingDialog
import com.example.baseprojectandroid.extension.showLoadingDialog
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.extension.visibleOrGone
import com.example.baseprojectandroid.ui.base.BaseFragment
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class SearchFragment: BaseFragmentBinding<FragmentSearchBinding, SearchViewModel>() {

    private lateinit var adapter: SearchAdapter
    private var searchEditText: SearchView.SearchAutoComplete? = null

    override fun getContentViewId(): Int = R.layout.fragment_search

    override fun initializeViews() {
        super.initializeViews()

        searchEditText =
            dataBinding.searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete

        adapter = SearchAdapter(requireContext())
        dataBinding.rvSearch.adapter = adapter

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


    override fun registerListeners() {
        searchEditText?.setOnClickListener {
            showKeyboard()
        }

        dataBinding.btnBack.setOnClickListener {
            searchEditText?.setText("")
            dataBinding.searchView.clearFocus()
            dataBinding.btnBack.gone()
            dataBinding.rvSearch.gone()
        }

        searchEditText?.setOnFocusChangeListener { _, hasFocus ->
            dataBinding.btnBack.visible()
            dataBinding.rvSearch.visibleOrGone(!hasFocus)
            dataBinding.searchRecently.visibleOrGone(hasFocus)
            EventBus.getDefault().post(GoneBottomLayoutEvent(hasFocus))
        }
    }

    private fun handleSearchFile(query: String) {
        requireActivity().showLoadingDialog()

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            viewModel.handleSearch("a", 1).collect {
                /*adapter.submitList(viewModel.listResultSearch, true)*/
                requireActivity().hideLoadingDialog()
            }
        }
    }
}
