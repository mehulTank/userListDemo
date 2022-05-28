package com.app.userList.ui.activity.dashboard

import com.app.userList.dbclasses.enitity.UserEntity
import com.app.userList.ui.base.BaseNavigator

interface MainActivityNavigator : BaseNavigator {


    fun onDataLoad(filterData: ArrayList<UserEntity>)

    fun onUpdateUserData(userData: UserEntity)


}