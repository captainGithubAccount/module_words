package com.lwj_template.moudle_word

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordViewModel(private val _wordRepository: WordRepository): ViewModel() {
    val allWords:LiveData<List<WordEntity>> by lazy { _wordRepository.allWords }
    val searchWords:LiveData<List<WordEntity>>? = null


    fun getSearchWords(search:String): LiveData<List<WordEntity>>{
        return _wordRepository.getSearchWords(search)
    }

    @WorkerThread
    fun insertWords(vararg wordEntity: WordEntity){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                _wordRepository.insertWords(*wordEntity)
            }
        }

    }

    @WorkerThread
    fun deleteWords(vararg wordEntity: WordEntity){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                _wordRepository.deleteWords(*wordEntity)
            }
        }
    }

    //    清空数据

    fun deleteAllWords(){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                _wordRepository.deleteAllWords()
            }

        }
    }

    @WorkerThread
    fun updateWords(vararg wordEntity: WordEntity){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                _wordRepository.updateWords(*wordEntity)
            }
        }
    }



}