package com.sample.roomandpaginlibrary

import androidx.lifecycle.AndroidViewModel
import com.sample.roomandpaginlibrary.model.DbManager

class MainViewModel(app: Application): AndroidViewModel(app) {
    var dbManager: DbManager = app.db

    init {
    }

    fun onFabClicked() {
        dbManager.insert(0)
    }
}