package com.lwj_template.moudle_word

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//构建数据库对象, 并用来获取所有的Dao层类, 因为viewModel是管理数据的,所以数据的来源要单独抽出到一个类中,尽可能实现类的单一原则
@Database(entities = arrayOf(WordEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
//    连接数据库是耗费资源的需要设计单例

    //官文实现单例
    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "word_database"
                ).addCallback(WordDatabaseCallback(scope))
                 .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAllWords()



            for(i in 0..10){
                var word = WordEntity( i,"hello", "你好")
                wordDao.insertWord(word)
            }
        }
    }




/*
// 自己的实现单例
    companion object{
        private lateinit var _applicationContext: Context

        private val appDatabase:AppDatabase? = null
            get() {
                if(field == null){
                    return Room.databaseBuilder(
                        _applicationContext,
                        AppDatabase::class.java, "database-word"
                    ).build()
                }else{
                    return appDatabase!!
                }
            }

//        线程安全注解
        @Synchronized
        fun getAppDatabase(context: Context): AppDatabase? {
            this._applicationContext = context.applicationContext
            return appDatabase
        }
    }
*/

}