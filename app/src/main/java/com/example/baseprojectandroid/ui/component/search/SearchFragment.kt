package com.example.baseprojectandroid.ui.component.search

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseprojectandroid.R
import com.example.baseprojectandroid.databinding.FragmentSearchBinding
import com.example.baseprojectandroid.evenbus.GoneBottomLayoutEvent
import com.example.baseprojectandroid.extension.gone
import com.example.baseprojectandroid.extension.hideLoadingDialog
import com.example.baseprojectandroid.extension.showLoadingDialog
import com.example.baseprojectandroid.extension.visible
import com.example.baseprojectandroid.extension.visibleOrGone
import com.example.baseprojectandroid.ui.base.BaseFragmentBinding
import com.example.baseprojectandroid.ui.common.HeaderItem
import com.example.baseprojectandroid.ui.common.HorizontallyGroup
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class SearchFragment: BaseFragmentBinding<FragmentSearchBinding, SearchViewModel>() {

    private val recentGroup = HorizontallyGroup()
    private val searchSection = Section(HeaderItem("Result Song"))
    private val searchAdapter = GroupieAdapter()
    private lateinit var adapter: SearchAdapter
    private var searchEditText: SearchView.SearchAutoComplete? = null

    override fun getContentViewId(): Int = R.layout.fragment_search

    override fun initializeViews() {
        super.initializeViews()

        searchEditText =
            dataBinding.searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete

        adapter = SearchAdapter(requireContext())
        dataBinding.rvSearch.apply {
            adapter = searchAdapter
            dataBinding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }

        initSearchAdapter()

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

    private fun initSearchAdapter() {
        searchAdapter.add(searchSection)
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
                adapter.submitList(viewModel.listResultSearch, true)
                requireActivity().hideLoadingDialog()
            }
        }
    }

    override fun registerObservers() {
        super.registerObservers()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.
                }
            }
        }
    }
}
