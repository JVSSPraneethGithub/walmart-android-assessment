package com.walmart.assessment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walmart.assessment.databinding.FragmentMainBinding
import com.walmart.assessment.ui.recyclerview.CountryListAdapter
import com.walmart.assessment.ui.viewmodel.CountryListViewModel
import com.walmart.assessment.ui.viewmodel.Failure
import com.walmart.assessment.ui.viewmodel.Loading
import com.walmart.assessment.ui.viewmodel.Success
import com.walmart.assessment.ui.viewmodel.factory.CountryListViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CountryListFragment(
    private val viewModelFactory: ViewModelProvider.Factory? = null
) : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel by viewModels<CountryListViewModel> {
        viewModelFactory ?: CountryListViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMainBinding.inflate(inflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collectLatest { value ->
                        with(binding) {
                            when (value) {
                                is Loading -> {
                                    countriesList.visibility = GONE
                                    errorText.visibility = GONE
                                    loadingIndicator.visibility = VISIBLE
                                    viewModel.fetchCountryList()
                                }

                                else -> {
                                    loadingIndicator.visibility = GONE
                                    when {
                                        value is Success -> {
                                            countriesList.run {
                                                visibility = VISIBLE
                                                layoutManager = LinearLayoutManager(
                                                    requireContext(),
                                                    RecyclerView.VERTICAL,
                                                    false
                                                )
                                                adapter = CountryListAdapter()
                                                    .apply {
                                                        submitList(value.data)
                                                    }
                                            }
                                        }

                                        value is Failure -> {
                                            errorText.visibility = VISIBLE
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}