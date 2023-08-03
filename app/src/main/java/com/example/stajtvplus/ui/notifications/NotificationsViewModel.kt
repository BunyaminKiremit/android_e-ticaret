package com.example.stajtvplus.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stajtvplus.models.NotificationMessage

class NotificationsViewModel : ViewModel() {

    companion object{
        val notifications: MutableList<NotificationMessage> = mutableListOf()
    }

    private val _notificationList = MutableLiveData<List<NotificationMessage>>()
    val notificationList: LiveData<List<NotificationMessage>> get() = _notificationList

    fun getNotifications(){
        _notificationList.postValue(notifications)
    }
}