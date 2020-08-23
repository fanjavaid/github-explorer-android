package com.fanjavaid.github_explorer_android.views

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanjavaid.github_explorer_android.R
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.repository.AccountRemoteRepositoryImpl
import com.fanjavaid.github_explorer_android.data.repository.AccountRepository
import com.fanjavaid.github_explorer_android.usecases.GetAccountsByNameUseCase
import com.fanjavaid.github_explorer_android.usecases.GetAccountsByNameUseCaseImpl
import com.fanjavaid.github_explorer_android.viewmodels.GetAccountViewModel
import com.fanjavaid.github_explorer_android.viewmodels.GetAccountViewModelFactory
import com.fanjavaid.github_explorer_android.viewmodels.SearchQuery
import com.fanjavaid.github_explorer_android.views.adapter.AccountAdapter
import com.fanjavaid.github_explorer_android.views.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates.observable

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
class MainActivity : AppCompatActivity(), LoadableView<List<Account>>, EmptyView {
    // Adapter
    private lateinit var accountAdapter: AccountAdapter

    // ViewModel
    private lateinit var viewModel: GetAccountViewModel
    private lateinit var accountRepository: AccountRepository
    private lateinit var getAccountsByNameUseCase: GetAccountsByNameUseCase

    private var endlessScrollListener = EndlessScrollListener()
    private var isLoading: Boolean by observable(false) { _, _, newValue ->
        endlessScrollListener.isLoading = newValue
    }
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListAdapter()
        initViewModel()
        observeAccounts()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_LOADING, isLoading)
        outState.putInt("page", currentPage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(STATE_LOADING)) {
            showLoading()
        }

        if (savedInstanceState.containsKey("page")) {
            currentPage = savedInstanceState.getInt("page")
        }
    }

    override fun onResume() {
        super.onResume()

        btnSearch.setOnClickListener {
            search()
        }
        etSearch.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> false
            }
        }
    }

    private fun initListAdapter() {
        accountAdapter = AccountAdapter()
        rvResults.apply {
            setHasFixedSize(true)
            adapter = accountAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(endlessScrollListener.apply {
                action = ::onLoadMore
            })
        }
    }

    private fun initViewModel() {
        accountRepository = AccountRemoteRepositoryImpl()
        getAccountsByNameUseCase = GetAccountsByNameUseCaseImpl(accountRepository)
        viewModel = ViewModelProvider(
            this,
            GetAccountViewModelFactory(getAccountsByNameUseCase)
        ).get(GetAccountViewModel::class.java)
    }

    private fun observeAccounts() {
        viewModel.accounts.observe(this@MainActivity, Observer {
            Toast.makeText(
                this@MainActivity,
                "Data exists? ${!it.isNullOrEmpty()}",
                Toast.LENGTH_SHORT
            ).show()
            hideLoading()
            render(it)
        })
    }

    fun search() {
        AndroidUtils.hideKeyboard(etSearch)
        showLoading()
        accountAdapter.accounts = mutableListOf()
        viewModel.searchGithubAccount(SearchQuery(etSearch.text.toString(), 1))
    }

    override fun render(data: List<Account>?) {
        // data empty when reload
        if (!data.isNullOrEmpty()) {
            showResults(data)
            return
        }

        if (accountAdapter.accounts.size == 0) showEmpty()
    }

    override fun showLoading() {
        pbLoading.visibility = VISIBLE
        rvResults.visibility = GONE
        groupEmptyErrorView.visibility = GONE
        isLoading = true
    }

    override fun hideLoading() {
        pbLoading.visibility = GONE
        isLoading = false
    }

    override fun showEmpty() {
        groupEmptyErrorView.visibility = VISIBLE
    }

    override fun onLoadMore() {
        currentPage++
        isLoading = true
        accountAdapter.accounts.add(null)
        rvResults.post {
            accountAdapter.notifyItemInserted(accountAdapter.accounts.size - 1)
        }
        viewModel.searchGithubAccount(SearchQuery(etSearch.text.toString(), currentPage))
    }

    private fun showResults(data: List<Account>) {
        rvResults.visibility = VISIBLE
        accountAdapter.run {
            if (accounts.isNotEmpty()) {
                accounts.remove(null)
                notifyItemRemoved(accounts.size)
            }

            if (data.isNotEmpty()) {
                accounts.addAll(data)
                notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val STATE_LOADING = "is_loading"
    }
}
