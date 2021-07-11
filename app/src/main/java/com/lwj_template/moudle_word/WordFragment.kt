package com.lwj_template.moudle_word

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lwj_template.moudle_word.databinding.FragmentWordBinding


//单词显示页面
class WordFragment : Fragment() {

    //Fragment默认不显示工具条
    init{
        setHasOptionsMenu(true)
    }

    var words: LiveData<List<WordEntity>>? = null

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


    override fun onResume() {
        super.onResume()

        hideSoftKeyboard()

    }

    //隐藏软键盘
    fun hideSoftKeyboard(){
        val imm =requireActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun init(){
        initView()
        initEvent()
        initData()
    }

    private fun initView() {
        floatingActionButton = binding.fabWordfragmentJumb
        rv = binding.rvWordfragmentContent

    }
    private fun initEvent() {
        floatingActionButton.setOnClickListener({ view ->
            findNavController().navigate(R.id.addFragment)
        })

    }

    private fun initData() {
        initRecyclerView()
        words = wordViewModel.allWords

        words?.observe(requireActivity(), Observer { wordList ->
            var temp = wordAdapter.itemCount
            wordList?.run{

                if(temp != wordList.size){//当只是隐藏了中文, 虽然使用了dao改变了数据, 但是没有必要全部更新一次页面

                    rv?.smoothScrollBy(0, 200)
                    //让插入数据后产生视觉反馈

                    wordAdapter.submitList(this)

                    //wordAdapter.notifyDataSetChanged()
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val viewItemWord = inflater.inflate(R.menu.menu_word, menu)


        // Get the SearchView and set the searchable configuration
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search_word_menu).actionView as SearchView).apply {
            maxWidth = 800

            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }


                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.run{
                        words?.removeObservers(requireActivity())
                        words = wordViewModel.getSearchWords(newText)

                        words?.observe(requireActivity()) { list ->
                            wordAdapter.submitList(list)
                        }
                    }
                    return true
                }

            })
        }





    }




    //初始化RecyclerView
    private fun initRecyclerView() {
        wordAdapter = WordAdapter(wordViewModel)

        rv?.run{
            adapter = wordAdapter
            //layoutManagerr(linearLayout(requireActivity()
            layoutManager = LinearLayoutManager(requireActivity())
//            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                    super.onAnimationFinished(viewHolder)

                    //插入一个item动画结束后, 更新数据
                    val linnerLayoutManager: LinearLayoutManager = rv?.layoutManager as LinearLayoutManager
                    val first: Int = linnerLayoutManager.findFirstVisibleItemPosition()
                    val last: Int = linnerLayoutManager.findLastVisibleItemPosition()

                    for(i in first..last){
                        val holder = rv?.findViewHolderForAdapterPosition(i) as? WordAdapter.WordViewHolder
                        holder?.let{ it.tvId.text = (i+1).toString()}
                    }
                }
            }

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