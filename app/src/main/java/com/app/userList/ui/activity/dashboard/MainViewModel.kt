package com.app.userList.ui.activity.dashboard

import android.app.Application
import com.app.userList.dbclasses.AppDatabase
import com.app.userList.dbclasses.enitity.UserEntity
import com.app.userList.di.GetUserApi
import com.app.userList.ui.base.BaseViewModel
import com.app.userList.util.ConnectionCheckServiceImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : BaseViewModel<MainActivityNavigator>(application) {
    var arrayList = ArrayList<UserEntity>()

    fun featchUserData(getUserDetails: GetUserApi) {
        if (ConnectionCheckServiceImpl(getApplication()).hasNetworkConnection()) {
            setIsLoading(true)
            getUserDetails.getUserDetails().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    run {
                        AppDatabase.getInstance(getApplication())!!.clearAllTables()
                        var filterData =
                            response.results!!

                        filterData.forEach {
                            val userEntity = UserEntity()
                            userEntity.userName =
                                it!!.name!!.title + " " + it.name!!.first + " " + it.name.last
                            userEntity.age = it.dob!!.age!!
                            userEntity.location = it.location!!.city + "/" + it.location.state
                            userEntity.gender = if (it.gender.equals("Female", true)) 0 else 1
                            userEntity.serverUserId = it.login!!.username
                            userEntity.image = it.picture!!.large
                            AppDatabase.getInstance(getApplication())!!.daoUser()
                                .insertAll(userEntity)
                        }
                        arrayList.clear()
                        var allUserData =
                            AppDatabase.getInstance(getApplication())!!.daoUser()
                                .getAllUserData() //Fetch all data

                        arrayList.addAll(allUserData)
                        setIsLoading(false)
                        getNavigator()?.onDataLoad(allUserData as ArrayList<UserEntity>)
                    }

                }, { error ->
                    error.printStackTrace()
                    setIsLoading(false)
                }
                )
        } else {
            //Check db data
            val allUserData = AppDatabase.getInstance(getApplication())!!.daoUser()
                .getAllUserData() //Fetch all data
            if (allUserData.size > 0) {
                getNavigator()?.onDataLoad(allUserData as ArrayList<UserEntity>)
            } else {
                getNavigator()?.showNoInternetMessage()
            }
        }

    }


}