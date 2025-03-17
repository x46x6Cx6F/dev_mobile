package com.example.devmobile.library.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.devmobile.library.data.Trail
import com.example.devmobile.utils.loadTrailsFromJson

class TrailViewModel(application: Application) : AndroidViewModel(application) {
    private val _trails = MutableLiveData<List<Trail>>()
    val trails: LiveData<List<Trail>> = _trails

    init {
        loadTrails()
    }

    private fun loadTrails() {
        val context: Context = getApplication()
        val trailList = loadTrailsFromJson(context)
        _trails.value = trailList
    }
}
