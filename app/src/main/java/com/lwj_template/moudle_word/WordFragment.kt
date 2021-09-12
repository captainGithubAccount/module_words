package com.lwj_template.moudle_word

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
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
//        val bind = FragmentWordBinding.inflate(inflater)
        // Inflate the layout for this fragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }


    //Fragment执行onResume生命周期方法时候隐藏软键盘
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

        //支持导航到AddFragment碎片时, 回退键头出现
        val navController = findNavController()
        NavigationUI.setupActionBarWithNavController(activity as AppCompatActivity, navController)

        floatingActionButton.setOnClickListener { view ->
            findNavController().navigate(R.id.addFragment)
        }

    }

    private fun initData() {
        initRecyclerView()
        words = wordViewModel.allWords

        words?.observe(viewLifecycleOwner, Observer { wordList ->
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

            //searchView文本监听
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                //当搜索框内文本发生改变时后台进行数据获取
                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.run{
                        words?.removeObservers(viewLifecycleOwner)
                        words = wordViewModel.getSearchWords(newText)

                        //因为requireActivity作为owner的时候,之前的activity也没有删除掉,所以每次都会叠加新的activity作为owner,但是实际要求应该是只有一个owner
                        words?.observe(viewLifecycleOwner) { list ->
                            wordAdapter.submitList(list)
                        }
                    }
                    return true
                }
            })

            //点击软键盘右上角收起(软键盘)键, 同时需要将搜索框变为搜索图标
            /*setOnKeyListener { v, keyCode, event ->
                if(keyCode == KeyEvent.){}
            }*/
        }

    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var alertDialog: AlertDialog? = null
        when(item.itemId) {
            R.id.clear_word_menu ->
                alertDialog = activity?.let {
                    val builder = AlertDialog.Builder(it)
                    builder.apply {
                        setTitle("您确定清空所有单词吗?")
                        setMessage("清空了就没有了喔, 谨慎操作!!!")
                        setPositiveButton("确定",
                                DialogInterface.OnClickListener { dialog, id ->
                                    // User clicked OK button
                                    wordViewModel.deleteAllWords()
                                })
                        setNegativeButton("取消",
                                DialogInterface.OnClickListener { dialog, id ->
                                    // User cancelled the dialog
                                })
                    }
                    // Set other dialog properties


                    // Create the AlertDialog
                    builder.create()
                    builder.show()
                }
        }

        return super.onOptionsItemSelected(item)
    }




    //初始化RecyclerView
    private fun initRecyclerView() {
        wordAdapter = WordAdapter(wordViewModel)

        rv?.run{
            adapter = wordAdapter
            //layoutManagerr(linearLayout(requireActivity()
            layoutManager = LinearLayoutManager(requireActivity())
            //layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            itemAnimator = object : DefaultItemAnimator() {
                override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                    super.onAnimationFinished(viewHolder)

                    //插入一个item动画结束后, 更新数据(序号)
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


        //rv的item滑动删除某一项处理
        object: ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,( ItemTouchHelper.START or ItemTouchHelper.END)) {
            /*
            * @param dragDirs: 允许拖动的方向, 0表示不支持拖动
            * @param swipeDirs: 允许滑动的方向, ItemTouchHelper.START: 从左向右开始滑动 ItemTouchHelper.END: 从右向左开始滑动
            * */
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //拿到要删除的item对应的WordEntity
                val wordItemDelete: WordEntity? = words?.value?.get(viewHolder.adapterPosition)//words.value调用get方法很可能不能实时返回结果, 尤其是当正在异步查询的时候
                wordItemDelete?.run{
                    wordViewModel.deleteWords(this)
                }

                //误删除数据的撤销处理
                Snackbar.make(binding.rvWordfragmentContent, "您删除了一条数据", Snackbar.LENGTH_SHORT)
                        .setAction("撤销") { v ->
                            wordViewModel.insertWords(wordItemDelete!!)
                        }.show()

            }
        }){}.attachToRecyclerView(rv)

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