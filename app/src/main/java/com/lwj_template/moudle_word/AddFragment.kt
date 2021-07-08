package com.lwj_template.moudle_word

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.play.core.splitinstall.d
import com.lwj_template.moudle_word.databinding.FragmentAddBinding


//添加单词页面
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!
    lateinit var floatActionButtonAdd: FloatingActionButton
    lateinit var edEnglish: EditText

    lateinit var edChinese: EditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false)
        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()


    }

    private fun init() {
        initView()
        initEvent()
        initData()


    }

    private fun initData() {

    }

    private fun initEvent() {
        //        floatActionButtonAdd.setOnClickListener({view -> Toast.makeText(getActivity(), "no", Toast.LENGTH_SHORT)})

        //j进来禁止使用提交按钮, 直到用户输入了英文和中文才可以使用提交按钮
        floatActionButtonAdd?.run{
            isEnabled = false
        }

        //editText控件获取焦点
        edEnglish.requestFocus()
        showSoftKeyboard(edEnglish)

//        TextWatcher监听, 当edEnglish和edChinese内都有内容时候隐藏软键盘
        edEnglish.addTextChangedListener { object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(edEnglish != null && edChinese != null){
                    floatActionButtonAdd.isEnabled = true
                    hideSoftKeyboard(edEnglish)
                }
            }

        } }
    }



    private fun initView() {
        floatActionButtonAdd = binding.floatingActionButton
        edEnglish = binding.edEnglish
        edChinese = binding.edChinese
    }

//    弹出软键盘(弹出软键盘需要控件拿到焦点)
    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

//    隐藏软键盘
    fun hideSoftKeyboard(view: View){
        val imm =activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_IMPLICIT)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}