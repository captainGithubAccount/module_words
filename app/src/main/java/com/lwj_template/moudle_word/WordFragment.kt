package com.lwj_template.moudle_word

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lwj_template.moudle_word.databinding.FragmentWordBinding

//单词显示页面
class WordFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentWordBinding? = null
    var rv: RecyclerView? = null
    lateinit var floatingActionButton: FloatingActionButton

    lateinit var wordAdapter: WordAdapter
    val wordViewModel: WordViewModel by viewModels<WordViewModel> {
        WordViewModelFactory((activity?.application as WordsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    fun init(){
        initView()
        initEvent()
        initData()
    }

    private fun initData() {
        initRecyclerView()

        wordViewModel.allWords?.observe(requireActivity(), Observer {

            wordList -> wordList?.run{wordAdapter.submitList(this)}
        })


    }

    private fun initEvent() {
        floatingActionButton.setOnClickListener({ view ->
            findNavController().navigate(R.id.addFragment)
        })

    }

    private fun initView() {
        floatingActionButton = binding.fabWordfragmentJumb
        rv = binding.rvWordfragmentContent

    }



    //初始化RecyclerView
    private fun initRecyclerView() {
        wordAdapter = WordAdapter()

        rv?.run{
            adapter = wordAdapter
            //layoutManagerr(linearLayout(requireActivity()
            layoutManager = LinearLayoutManager(requireActivity())

        }
        /*val list:ArrayList<WordEntity>? = arrayListOf()
        for(item in 0..20){
            list?.add(WordEntity(item, "hello","你好", false ))
        }
        wordAdapter.submitList(list)

         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}