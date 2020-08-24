package com.fanjavaid.github_explorer_android.views

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanjavaid.github_explorer_android.R
import com.fanjavaid.github_explorer_android.data.AccountRepository
import com.fanjavaid.github_explorer_android.data.local.AccountDao
import com.fanjavaid.github_explorer_android.data.local.GithubExplorerDatabase
import com.fanjavaid.github_explorer_android.data.local.AccountLocalRepositoryImpl
import com.fanjavaid.github_explorer_android.data.model.Account
import com.fanjavaid.github_explorer_android.data.network.AccountRemoteRepositoryImpl
import com.fanjavaid.github_explorer_android.usecases.*
import com.fanjavaid.github_explorer_android.viewmodels.GetAccountViewModel
import com.fanjavaid.github_explorer_android.viewmodels.GetAccountViewModelFactory
import com.fanjavaid.github_explorer_android.viewmodels.SearchQuery
import com.fanjavaid.github_explorer_android.views.adapter.AccountAdapter
import com.fanjavaid.github_explorer_android.views.utils.AndroidUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates.observable

/**
 * Created by Fandi Akhmad (fanjavaid) on 18/08/20.
 */
class MainActivity : AppCompatActivity(), LoadableView<List<Account>>, EmptyView {
    // Adapter
    private lateinit var accountAdapter: AccountAdapter

    // ViewModel
    private lateinit var viewModel: GetAccountViewModel

    // Repositories
    private lateinit var accountRepository: AccountRepository
    private lateinit var accountLocalRepository: AccountRepository

    // Use Cases
    private lateinit var getAccountsUseCase: GetAccountsUseCase
    private lateinit var getAccountsByNameUseCase: GetAccountsByNameUseCase
    private lateinit var saveAccountsUseCase: SaveAccountsUseCase
    private lateinit var deleteAccountsUseCase: DeleteAccountsUseCase

    // Local
    private lateinit var accountDao: AccountDao

    private var endlessScrollListener = EndlessScrollListener()
    private var isLoading: Boolean by observable(false) { _, _, newValue ->
        endlessScrollListener.isLoading = newValue
    }
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initListAdapter()
        initDao()
        initRepositories()
        initUseCases()
        initViewModel()
        observerCachedAccounts()
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
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> false
            }
        }
    }

    private fun initDao() {
        accountDao = GithubExplorerDatabase.getDatabase(this).getAccountDao()
    }

    private fun initListAdapter() {
        accountAdapter = AccountAdapter()
        rvResults.apply {
            adapter = accountAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(endlessScrollListener.apply {
                action = ::onLoadMore
            })
        }
    }

    private fun initRepositories() {
        accountLocalRepository = AccountLocalRepositoryImpl(accountDao)
        accountRepository = AccountRemoteRepositoryImpl()
    }

    private fun initUseCases() {
        // local
        saveAccountsUseCase = SaveLocalAccountUseCaseImpl(accountLocalRepository)
        deleteAccountsUseCase = DeleteLocalAccountsUseCaseImpl(accountLocalRepository)
        getAccountsUseCase = GetLocalAccountsUseCaseImpl(accountLocalRepository)

        // network
        getAccountsByNameUseCase = GetAccountsByNameUseCaseImpl(accountRepository)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            GetAccountViewModelFactory(getAccountsUseCase, getAccountsByNameUseCase)
        ).get(GetAccountViewModel::class.java)
    }

    private fun observeAccounts() {
        viewModel.accounts.observe(this@MainActivity, Observer {
            if (!it.isNullOrEmpty()) {
                GlobalScope.launch {
                    saveAccountsUseCase.saveAccounts(it)
                }
            }

            hideLoading()
            render(it)
        })
    }

    private fun observerCachedAccounts() {
        viewModel.cachedAccounts.observe(this, Observer { data ->
            if (!data.isNullOrEmpty()) {
                accountAdapter.accounts = data.toMutableList()
                accountAdapter.notifyDataSetChanged()
            }
        })
    }

    fun search() {
        // Update UI
        AndroidUtils.hideKeyboard(etSearch)
        showLoading()

        // Reset state
        accountAdapter.accounts.clear()
        GlobalScope.launch(Dispatchers.Main) {
            deleteAccountsUseCase.deleteAccounts()
        }

        // Start new search
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
