package com.sample.roomandpaginlibrary.model

import androidx.room.*
import java.net.URL

//@Fts4(languageId = "lid")
@Entity(tableName = "translations",
        indices = [Index("word_id")],
        foreignKeys = [ForeignKey(entity = Word::class,
            parentColumns = ["rowid"],
            childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE)])
data class Translation (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "lid") val languageId: Int,
    @ColumnInfo(name = "word_id") val wordId: Int
)