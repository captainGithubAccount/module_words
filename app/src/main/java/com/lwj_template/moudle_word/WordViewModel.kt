package com.lwj_template.moudle_word

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WordViewModel(private val _wordRepository: WordRepository): ViewModel() {
    val allWords:LiveData<List<WordEntity>> by lazy { _wordRepository.allWords }
    val searchWords:LiveData<List<WordEntity>>? = null


    fun getSearchWords(search:String): LiveData<List<WordEntity>>{
        return _wordRepository.getSearchWords(search)
    }

    @WorkerThread
    suspend fun insertWords(vararg wordEntity: WordEntity){
        _wordRepository.insertWords()
    }

    @WorkerThread
    suspend fun deleteWords(vararg wordEntity: WordEntity){
        _wordRepository.deleteWords(*wordEntity)
    }

    //    清空数据
    @WorkerThread
    suspend fun deleteAllWords(){
        _wordRepository.deleteAllWords()
    }



}