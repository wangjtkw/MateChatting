package com.example.matechatting.function.forgetpassword

import com.example.matechatting.network.ChangeResetPassService
import com.example.matechatting.network.IdeaApi
import com.example.matechatting.base.BaseRepository
import com.example.matechatting.network.ResetPassInterceptor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ResetPassRepository : BaseRepository {

    fun changePass(password: String, interceptor: ResetPassInterceptor, callback: (Boolean) -> Unit) {
        IdeaApi.getApiService(ChangeResetPassService::class.java,needToken = false,tokenInterceptor = interceptor).changePassword(password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                callback(it.success)
            }
            .subscribe()
    }
}