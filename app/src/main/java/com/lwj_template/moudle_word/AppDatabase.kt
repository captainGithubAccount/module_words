package com.lwj_template.moudle_word

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//迁移数据库(即修改数据库结构)
/*val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE `words` RENAME COLUMN `id` int auto_increment primary key;")
    }
}*/

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
                        "database_word"
                ).addCallback(WordDatabaseCallback(scope))
                        .fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }



    abstract fun wordDao(): WordDao


    //填充数据库
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




            var word1 = WordEntity( "hello", "你好")

            var word2 = WordEntity("populate", "填充")

            var word3 = WordEntity("english", "英语")

            var word4 = WordEntity("dababase", "数据库")

            var word5 = WordEntity("adapter", "适配器")

            var word6 = WordEntity("word", "单词")

            val words = arrayOf(word1, word2, word3, word4, word5, word6)

            for(i in 0..5){
                wordDao.insertWord(words[i])
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