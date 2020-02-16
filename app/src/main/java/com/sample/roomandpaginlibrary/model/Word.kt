package com.sample.roomandpaginlibrary.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import java.net.URL

//@Fts4(languageId = "lid")
@Entity(tableName = "words")
data class Word (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") val id: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "lid") val languageId: Int
    // add translations
)