package com.lwj_template.moudle_word

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

//word实体类对应的Dao层
@Dao
interface WordDao {


    @Insert
    suspend fun insertWord(vararg words: WordEntity)

    @Delete
    suspend fun deleteWord(vararg word: WordEntity)

//    加载到recyclerview时候显示的数据,同时也被用来观察数据库的变化, LieveData数据已经实现了异步,所以不需要重复开辟线程进行获取操作
    @Query("SELECT * FROM words ORDER BY english ASC")
    fun getAll(): LiveData<List<WordEntity>>

//    搜索查询返回的数据
    @Query("SELECT * FROM words WHERE english LIKE :search ORDER BY english ASC")
    fun getWordsBySearch(search: String): LiveData<List<WordEntity>>




//    清空数据
    @Query("DELETE FROM words")
    fun deleteAllWords()



}