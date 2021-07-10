package com.lwj_template.moudle_word

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.lwj_template.moudle_word.databinding.WordItemContentBinding


class WordAdapter: ListAdapter<WordEntity, WordAdapter.WordViewHolder>(DIFF_CALLBACK) {

    class WordViewHolder(binding: WordItemContentBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvId: TextView
        val tvEnglish: TextView
        val tvChinese: TextView
        val switchHideChinese: Switch

        init{
            tvId = binding.tvWordId
            tvEnglish = binding.tvWordEnglish
            tvChinese = binding.tvWordChinese
            switchHideChinese =binding.switchWordHideChinesemean
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
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word: WordEntity = getItem(position)
        holder.tvId.text = (word.id + 1).toString()
        holder.tvEnglish.text = word.englishMean
        holder.tvChinese.text = word.chineseMean

    }

}