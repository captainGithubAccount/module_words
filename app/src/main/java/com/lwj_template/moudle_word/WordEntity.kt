package com.lwj_template.moudle_word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "english") val englishMean: String,
        @ColumnInfo(name = "chinese") val chineseMean: String,
        @ColumnInfo(name = "isHide") val isHideChineseMean: Boolean)
