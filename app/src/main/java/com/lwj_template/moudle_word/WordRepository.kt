package com.lwj_template.moudle_word

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

//对数据进行管理的仓库类, 获取数据, 删除数据, 更新数据, 插入数据等
class WordRepository(private var _wordDao: WordDao) {

//    注意LiveData数据已经实现了后台加载的功能所以可以直接获取, 但是直接插入和删除等耗时操作应该放入子线程中执行


    private lateinit var _searchWords: LiveData<List<WordEntity>>

//    拿到全部的数据,用来观察数据库的变化
    val allWords: LiveData<List<WordEntity>> by lazy{ _wordDao.getAll() }

//    拿到经过搜索输入模糊查询的符合条件的数据
    fun getSearchWords(search:String): LiveData<List<WordEntity>>{
        _searchWords = _wordDao.getWordsBySearch("%$search%")
        return _searchWords
    }

    @WorkerThread
    suspend fun insertWords(vararg wordEntity: WordEntity){
        _wordDao.insertWord(*wordEntity)
    }

    @WorkerThread
    suspend fun deleteWords(vararg wordEntity: WordEntity){
        _wordDao.deleteWord(*wordEntity)
    }

//    清空数据
    @WorkerThread
    suspend fun deleteAllWords(){
        _wordDao.deleteAllWords()
    }

    @WorkerThread
    suspend fun updateWords(vararg wordEntity: WordEntity){
        _wordDao.updateWord(*wordEntity)
    }
}