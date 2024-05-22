package com.example.finflow.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finflow.R
import com.example.finflow.databinding.CardItemBinding

class RecyclerViewAdapter(private  val appList: List<EnjoyEntity>,
                          private val clickListener:(EnjoyEntity)-> Unit):RecyclerView.Adapter<MyDebitAppViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDebitAppViewHolder
        {
        var inflator = LayoutInflater.from(parent.context)
        val cardView: CardItemBinding = DataBindingUtil.inflate(inflator,
            R.layout.card_item,parent, false)
        return MyDebitAppViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(holder: MyDebitAppViewHolder, position: Int) {
        holder.bind(appList[position],clickListener)
    }
}

class MyDebitAppViewHolder(val binding: CardItemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(enjoyEntity: EnjoyEntity, clickListener:(EnjoyEntity)-> Unit){
        binding.outputName.text = enjoyEntity.name
        binding.ouputDesc.text = enjoyEntity.desc
        binding.outputRate.text = enjoyEntity.rate.toString()
        binding.printFreq.text = enjoyEntity.freq.toString()
        binding.listDebitApp.setOnClickListener(){
            clickListener(enjoyEntity)
        }
    }
}