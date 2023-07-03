package com.example.baseprojectandroid.ui.component.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.viewModelScope
import com.example.baseprojectandroid.App
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSearchBinding
import com.example.baseprojectandroid.evenbus.GoneBottomLayoutEvent
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.hideLoadingDialog
import com.example.baseprojectandroid.extension.showLoadingDialog
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.extension.visibleOrGone
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class SearchFragment : BaseFragmentBinding<FragmentSearchBinding, SearchViewModel>() {

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
            EventBus.getDefault().post(GoneBottomLayoutEvent(hasFocus))
        }

        adapter.setOnItemClickListener {

        }
    }

    private fun handleSearchFile(query: String) {
        requireActivity().showLoadingDialog()

        viewModel.viewModelScope.launch(Dispatchers.IO) {
            App.instance.localStorage.currentUser?.id?.let {
                viewModel.handleSearch(query, it).collect {
                    when (it) {
                        is DataState.Success -> {
                            it.data?.let { it1 ->
                                withContext(Dispatchers.Main) {
                                    adapter.submitList(it1, true)
                                }
                            }
                        }

                        else -> Unit
                    }
                    requireActivity().hideLoadingDialog()
                }
            }
        }
    }
}
