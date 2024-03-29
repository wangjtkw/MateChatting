package com.example.matechatting

import com.example.matechatting.R

//从相册选择页面返回数据的请求码
const val ALBUM_REQUEST_CODE = 0x111
//从裁剪页面返回给选择页面的请求码
const val CLIP_REQUEST_CODE = 0x112
//登陆界面返回登陆状态的请求码
const val LOGIN_REQUEST_CODE = 0x120
//个性标语编辑页返回给我的信息页的请求码
const val PERSON_SIGN_REQUEST_CODE = 0x130
//方向选择页返回给我的信息页的请求码
const val DIRECT_REQUEST_CODE = 0x131
//从忘记密码页返回登陆页的请求码
const val FORGET_REQUEST_CODE = 0X132

const val BASE_URL = "http://pdf9fk.natappfree.cc"
const val PATH = "/static/"

var PAGE = ArrayList<Int>()

const val JOB_SERVICE_NAME = 0x9999

//方向Map key：大标签 value：小标签
val list = hashMapOf<String, List<String>>()
//大方向的集合，
val listKey = ArrayList<String>()
val mapChecked = hashMapOf<String,Boolean>()
val clicked_names =  ArrayList<String>()

interface MainConstValue {

    val tabSelectedDrawableIdList: Array<Int> get() = arrayOf(
        R.drawable.main_home_selected,
        R.drawable.main_milelist_selected,
        R.drawable.main_mine_selected
    )

    val tabUnselectedDrawableList: Array<Int> get() = arrayOf(
        R.drawable.main_home_unselected,
        R.drawable.main_milelist_unselected,
        R.drawable.main_mine_unselected
    )

    val tabText: Array<String> get() = arrayOf("首页","名片夹","我的")
}