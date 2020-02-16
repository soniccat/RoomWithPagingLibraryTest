package com.sample.roomandpaginlibrary.model

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.sample.roomandpaginlibrary.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import java.net.URL
import java.util.concurrent.Executors

class DbManager(private val context: Context) {
    private val scope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher() + SupervisorJob())
    private lateinit var db: AppDatabase

    init {
        //scope.launch {
            buildDB()
//            val list = db.userDao().getAll().first()
//            if (list.isEmpty()) {
//                db.userDao().insert(Word(1, "first", "https://www.yandex.ru", 1))
//            }
        //}

        // observe db changes
        scope.launch {
            db.userDao().getAll().collect {
                Log.d("DB", "words " + it.size)
            }
        }

        // observe db changes
        scope.launch {
            db.userDao().getWordsWithTranslations().collect {
                Log.d("DB", "words with translations " + it.size)
            }
        }

        // observe db changes
        scope.launch {
            db.userDao().getAllTranslations().collect {
                Log.d("DB", "translations " + it.size)
            }
        }

//        for (i in 0..500) {
//            insert(i)
//        }
    }

    fun getWordsWithTranslations() = db.userDao().getWordsWithTranslationsDataSource()

    fun insert(number: Int) {
        scope.launch {
            val id = db.userDao().insert(Word(0, "sth $number", "https://www.yandex.ru", 1), Translation(0, "a", 1, 0))
            val word = db.userDao().getWordsWithTranslationsById(intArrayOf(id.toInt())).first()
            db.userDao().update(word.first().translations.first().copy(name = "zzzz"))
        }
    }

    private fun buildDB() {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "WordsDB"
        ).build()
    }


}