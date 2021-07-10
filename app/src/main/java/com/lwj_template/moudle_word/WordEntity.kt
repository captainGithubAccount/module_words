package com.lwj_template.moudle_word

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
        @PrimaryKey val id: Int,

        @ColumnInfo(name = "english") @NonNull val englishMean: String,
        @ColumnInfo(name = "chinese") @NonNull val chineseMean: String,
        @ColumnInfo(name = "isHide") @NonNull val isHideChineseMean: Boolean = false,
        )



