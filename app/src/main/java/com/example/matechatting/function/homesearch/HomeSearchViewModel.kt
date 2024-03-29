package com.example.matechatting.function.homesearch

import androidx.lifecycle.ViewModel
import com.example.matechatting.bean.SearchBean

class HomeSearchViewModel(private val repository:HomeSearchRepository) :ViewModel(){

    fun getResult(key: String, page: Int, size: Int = 20, callback: (List<SearchBean.Payload.MyArray.Map>) -> Unit){
        repository.getResult(key,page, size, callback)
    }
}