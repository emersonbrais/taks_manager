package com.example.task_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.task_app.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData

class TaskPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("task_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTaskList(taskList: List<Task>) {
        val json = gson.toJson(taskList)
        sharedPreferences.edit() { putString("task_list", json) }
    }

    fun getTaskList(): List<Task> {
        val json = sharedPreferences.getString("task_list", null) ?: return emptyList()
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getTaskMutableList(): MutableLiveData<List<Task>>  {
        val json = sharedPreferences.getString("task_list", null) ?: return MutableLiveData()
        val type = object : TypeToken<List<Task>>() {}.type
        val taskList: List<Task> =  gson.fromJson(json, type)

        return MutableLiveData(taskList)
    }
}