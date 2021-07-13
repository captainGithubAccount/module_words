package com.lwj_template.moudle_word

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lwj_template.moudle_word.databinding.WordItemContentBinding


class WordAdapter(viewModel: WordViewModel): ListAdapter<WordEntity, WordAdapter.WordViewHolder>(DIFF_CALLBACK) {
        val  wordViewModel: WordViewModel by lazy { viewModel as WordViewModel }




    class WordViewHolder(binding: WordItemContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvId: TextView
        val tvEnglish: TextView
        val tvChinese: TextView
        val switchHideChinese: Switch
        val cartView: ViewGroup
        init{
            tvId = binding.tvWordId
            tvEnglish = binding.tvWordEnglish
            tvChinese = binding.tvWordChinese
            switchHideChinese = binding.switchWordHideChinesemean
            cartView = binding.cvWordItem

        }
    }

    companion object{
        val DIFF_CALLBACK:DiffUtil.ItemCallback<WordEntity> = object : DiffUtil.ItemCallback<WordEntity>() {

//            判断是否是一个两个item的id是否相同
            override fun areItemsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean {
                return (oldItem.id == newItem.id)
            }

//            判断这两个item内容是否完全相同
            override fun areContentsTheSame(oldItem: WordEntity, newItem: WordEntity): Boolean {
                return (oldItem.chineseMean == newItem.chineseMean &&
                        oldItem.englishMean == oldItem.englishMean &&
                        oldItem.isHideChineseMean == oldItem.isHideChineseMean)
            }
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding:WordItemContentBinding  = DataBindingUtil.inflate(inflater, R.layout.word_item_content, parent, false)
        val holder:WordViewHolder = WordViewHolder(binding)
        //**** 注意: 将view的事件监听放入刚刚创建的viewHolder中, 这样就不用每一次点击的时候都要去new一个匿名内部类了
        //整体的卡片式的布局设置监听, 打开百度翻译
        holder.cartView.setOnClickListener(){ v ->

            //当点击事件发生的时候先拿到holder下的这个数据, 拿到的是一个Object类型需要强转
            //val word: WordEntity = holder.cartView.getTag(R.id.id_holder_word_for_view) as WordEntity
            val uri: Uri = Uri.parse("https://fanyi.baidu.com/?aldtype=85#zh/en/${holder.tvEnglish.text}")
            //Log.d("test_english", word.englishMean)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            holder.cartView.context.startActivity(intent)
        }

        //点击开关控件隐藏和显示中文
        holder.switchHideChinese.setOnCheckedChangeListener(){switchView, isChecked->
            //当点击事件发生的时候先拿到holder下的这个数据, 拿到的是一个Object类型需要强转
            val word: WordEntity = holder.cartView.getTag(R.id.id_holder_word_for_view) as WordEntity
            if(isChecked){
                holder.tvChinese.visibility = View.GONE
                word?.isHideChineseMean = true

                word?.run{
                    wordViewModel.updateWords(this)
                }

            }else{
                holder.tvChinese.visibility = View.VISIBLE
                word?.isHideChineseMean = false
                word?.run{
                    wordViewModel.updateWords(this)
                }
            }
        }
        return holder
    }



    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word: WordEntity = getItem(position)

        //存储word
        holder.cartView.setTag(R.id.id_holder_word_for_view, word)

        holder.tvId.text = (position+1).toString()
        holder.tvEnglish.text = word.englishMean
        holder.tvChinese.text = word.chineseMean

        //由于放在了创建viewholder中,所以只创建一次,不需要每次都创建匿名内部类,所以下面的方法也要注释
        //holder.switchHideChinese.setOnCheckedChangeListener(null)//由于可能用到回收的view的监听所以需要每次将监听置为空

        //如果item switch为true, 隐藏中文
        if(word.isHideChineseMean){
            holder.tvChinese.visibility = View.GONE
            holder.switchHideChinese.isChecked = true
        }else{
            holder.tvEnglish.visibility = View.VISIBLE
            holder.switchHideChinese.isChecked = false
        }

        /*
        //点击开关控件隐藏和显示中文
        holder.switchHideChinese.setOnCheckedChangeListener(){
            switchView, isChecked->
            if(isChecked){
                holder.tvChinese.visibility = View.GONE
                word.isHideChineseMean = true
                wordViewModel.updateWords(word)

            }else{
                holder.tvChinese.visibility = View.VISIBLE
                word.isHideChineseMean = false
                wordViewModel.updateWords(word)
            }
        }
*/


    }
    //当viewHolder出现到屏幕上的时候, 将其序号更改
    override fun onViewAttachedToWindow(holder: WordViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.tvId.text = (holder.adapterPosition + 1).toString()
    }

}