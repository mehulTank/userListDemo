package com.app.userList.ui.activity.dashboard

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.assignment.utils.showToast
import com.app.userList.R
import com.app.userList.databinding.ActivityMainBinding
import com.app.userList.dbclasses.enitity.UserEntity
import com.app.userList.di.GetUserApi
import com.app.userList.ui.activity.adapter.UserAdapter
import com.app.userList.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), MainActivityNavigator {
    var userAdapter: UserAdapter? = null
    val userApi: GetUserApi by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            rvUser.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
        userAdapter.apply { userAdapter = UserAdapter(ArrayList<UserEntity>(), getViewModel()) }

        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = true
            getViewModel().featchUserData(userApi)
        }

        initObservers()

    }

    private fun initObservers() {
        mViewModel.setNavigator(this)
        mViewModel.featchUserData(userApi)
    }

    override fun getBindingObject(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    override fun getViewModel(): MainViewModel =
        ViewModelProvider(this).get(MainViewModel::class.java)


    override fun onDataLoad(filterData: ArrayList<UserEntity>) {
        userAdapter = UserAdapter(filterData, getViewModel())
        rvUser.adapter = userAdapter
        swipeLayout.isRefreshing = false
    }

    override fun onUpdateUserData(userData: UserEntity) {
        userAdapter?.changeData(userData)
    }

    override fun showErrorMessage(errorMessage: String) {

    }

    override fun showNoInternetMessage() {
        showToast(getString(R.string.no_internet))
        finish()
    }


}