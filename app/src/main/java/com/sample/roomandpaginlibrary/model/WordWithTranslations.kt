package com.sample.roomandpaginlibrary.model

import androidx.room.Embedded
import androidx.room.Relation

data class WordWithTranslations (
    @Embedded val word: Word,
    @Relation(
        entity = Translation::class,
        parentColumn = "rowid",
        entityColumn = "word_id"
    )
    val translations: List<Translation>
)