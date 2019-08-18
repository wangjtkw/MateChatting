package com.example.matechatting.network

import androidx.paging.DataSource
import com.example.matechatting.bean.HomeItemBean

class PagingHomeItemDataSourceFactory : DataSource.Factory<Int, HomeItemBean>() {
    override fun create(): DataSource<Int, HomeItemBean> {
        return PagingHomeItemDataSource()
    }
}