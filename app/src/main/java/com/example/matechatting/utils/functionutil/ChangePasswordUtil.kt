package com.example.matechatting.utils.functionutil

import android.util.Log
import com.example.matechatting.MyApplication
import com.example.matechatting.utils.NetworkState
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.AGAIN_ERROR
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.AGAIN_NULL
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.NEW_ERROR
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.NEW_NO6
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.NEW_NULL
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.NEW_OK
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.NEW_TOO_LONG
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.NO_NET
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.OLD_NULL
import com.example.matechatting.utils.functionutil.ChangePasswordState.Companion.regex
import com.example.matechatting.utils.isNetworkConnected


/**
 * 网络检查密码是否正确
 */
object ChangePasswordUtil {

    fun checkNewPassword(new: String, callback: (state: Int) -> Unit) {
        if (new.length < 6) {
            callback(NEW_NO6)
        } else if (new.length > 16) {
            callback(NEW_TOO_LONG)
        } else {
            val result = regex.find(new)
            Log.d("aaa", result.toString())
            if (result == null) {
                callback(NEW_ERROR)
            } else {
                callback(NEW_OK)
            }
        }
    }

    fun checkChangePassword(account: String, old: String, new: String, again: String, callback: (state: Int) -> Unit) {
        val state = isNetworkConnected(MyApplication.getContext())
        if (state == NetworkState.NONE){
            callback(NO_NET)
        }
        if (old.isEmpty()){
            callback(OLD_NULL)
        }
        if (new.isEmpty()){
            callback(NEW_NULL)
        }
        if (again.isEmpty()){
            callback(AGAIN_NULL)
        }
        val result = regex.find(new)
        if (result == null) {
            callback(NEW_ERROR)
        }
        if (old != again){
            callback(AGAIN_ERROR)
        }
        /**
         * 验证旧密码是否正确
         */
    }

}

class ChangePasswordState {
    companion object {
        val regex = Regex("(?!^[0-9]+\$)(?!^[A-z]+\$)(?!^[^A-z0-9]+\$)^.{6,16}\$")
        //验证成功
        const val OK = 0
        //旧密码为空
        const val OLD_NULL = 1
        //旧密码输入错误
        const val OLD_ERROR = 4
        //新密码为空
        const val NEW_NULL = 10
        //新密码不符合规范
        const val NEW_ERROR = 11
        //新密码不到六位
        const val NEW_NO6 = 12
        //符号规范
        const val NEW_OK = 13
        //密码太长
        const val NEW_TOO_LONG = 14
        //再次密码为空
        const val AGAIN_NULL = 20
        //再次密码不相同
        const val AGAIN_ERROR = 21
        //
        const val NO_NET = 30
    }
}