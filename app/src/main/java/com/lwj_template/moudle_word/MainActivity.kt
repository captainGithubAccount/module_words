package com.lwj_template.moudle_word

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.lwj_template.moudle_word.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    //监听点击action bar回退按钮操作
    override fun onSupportNavigateUp(): Boolean {
        /*
        * @ param R.id.nav_word: 指放nav_graph的viewGroup的id
        * */
        return Navigation.findNavController(findViewById(R.id.nav_word)).navigateUp() or super.onSupportNavigateUp()
    }

    //当按下手机自带导航栏的回退键时候, 需要出栈处理
    override fun onBackPressed() {
        super.onBackPressed()
        Navigation.findNavController(findViewById(R.id.nav_word)).navigateUp()

    }
}