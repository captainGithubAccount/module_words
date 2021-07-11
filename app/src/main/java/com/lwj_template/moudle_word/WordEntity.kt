package com.lwj_template.moudle_word

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
        @ColumnInfo(name = "english")
        @NonNull
        val englishMean: String,
        @ColumnInfo(name = "chinese")
        @NonNull
        val chineseMean: String,
) {
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int = 0

        @ColumnInfo(name = "isHide")
        @NonNull
        var isHideChineseMean: Boolean = false
}



