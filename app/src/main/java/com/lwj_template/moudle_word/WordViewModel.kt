package com.lwj_template.moudle_word

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WordViewModel(private val _wordRepository: WordRepository): ViewModel() {
    val allWords:LiveData<List<WordEntity>> by lazy { _wordRepository.allWords }
    val searchWords:LiveData<List<WordEntity>>? = null


    fun getSearchWords(search:String): LiveData<List<WordEntity>>{
        return _wordRepository.getSearchWords(search)
    }

    @WorkerThread
    fun insertWords(vararg wordEntity: WordEntity){
        viewModelScope.launch {
            _wordRepository.insertWords(*wordEntity)
        }

    }

    @WorkerThread
    fun deleteWords(vararg wordEntity: WordEntity){
        viewModelScope.launch {
            _wordRepository.deleteWords(*wordEntity)
        }
    }

    //    清空数据
    @WorkerThread
    fun deleteAllWords(){
        viewModelScope.launch {
            _wordRepository.deleteAllWords()
        }
    }



}