package com.sample.roomandpaginlibrary.model

import androidx.paging.DataSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DBDao {
    @Query("SELECT *, rowid, lid FROM words")
    fun getAll(): Flow<List<Word>>

    @Query("SELECT *, rowid, lid FROM translations")
    fun getAllTranslations(): Flow<List<Translation>>

    @Query("SELECT *, rowid, lid FROM words WHERE rowid IN (:wordIds)")
    fun getAllByIds(wordIds: IntArray): Flow<List<Word>>

    @Query("SELECT *, rowid, lid FROM words WHERE name LIKE :firstLike LIMIT 1")
    fun getByName(firstLike: String): Flow<Word>

    @Transaction @Query("SELECT *, rowid, lid FROM words")
    fun getWordsWithTranslations(): Flow<List<WordWithTranslations>>

    @Transaction @Query("SELECT *, rowid, lid FROM words")
    fun getWordsWithTranslationsDataSource(): DataSource.Factory<Int, WordWithTranslations>

    @Transaction @Query("SELECT *, rowid, lid FROM words WHERE rowid IN (:wordIds)")
    fun getWordsWithTranslationsById(wordIds: IntArray): Flow<List<WordWithTranslations>>

    @Insert
    suspend fun insert(word: Word): Long

    @Insert
    suspend fun insert(translation: Translation): Long

    @Transaction
    suspend fun insert(word: Word, translation: Translation): Long {
        val id = insert(word)
        insert(translation.copy(wordId = id.toInt()))
        return id
    }

    @Update
    suspend fun update(word: Word): Int

    @Update
    suspend fun update(word: Translation): Int

    @Delete
    suspend fun delete(word: Word): Int
}