package com.example.matechatting.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import com.example.matechatting.R
import com.example.matechatting.constvalue.FORGET_REQUEST_CODE
import com.example.matechatting.databinding.ActivityLoginBinding
import com.example.matechatting.listener.EditTextTextChangeListener
import com.example.matechatting.repository.LoginState
import com.example.matechatting.utils.InjectorUtils
import com.example.matechatting.utils.NetworkState
import com.example.matechatting.utils.ToastUtil
import com.example.matechatting.utils.isNetworkConnected
import com.example.matechatting.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private lateinit var accountEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var accountError: TextView
    private lateinit var passwordError: TextView
    private lateinit var forgetText: TextView
    private lateinit var loginButton: Button
    private lateinit var accountClear: ImageView
    private lateinit var passwordClear: ImageView
    private lateinit var back: ImageView
    private var accountNotNull = false
    private var passwordNotNull = false

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        canSlideFinish(true)
        initBinding()
        init()
        initEditText()
        initClear()
        initButton()
        initBack()
        initForget()
    }

    private fun init() {
        val factory = InjectorUtils.provideLoginViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        accountEdit = binding.loginEdittextAccount
        passwordEdit = binding.loginEdittextPassword
        forgetText = binding.loginTextForgetPassword
        loginButton = binding.loginButtonLogin
        accountError = binding.loginTextErrorAccount
        passwordError = binding.loginTextErrorPassword
        accountClear = binding.loginAccountClear
        passwordClear = binding.loginPasswordClear
        back = binding.loginBack
    }

    private fun initForget() {
        forgetText.setOnClickListener {
            transferForgetActivity()
        }
    }

    private fun transferForgetActivity() {
        val intent = Intent(this, ForgetPasswordActivity::class.java)
        startActivityForResult(intent, FORGET_REQUEST_CODE)
    }

    private fun initBack() {
        back.setOnClickListener {
            this.finish()
        }
    }


    /**
     * 设置EditText的监听
     * 设置@link [accountClear] 及 @link [passwordClear] 是否显示
     * 设置@link [loginButton] 是否可点击
     * 输入变化时 设置@link [accountError] [passwordError] 的内容
     */
    private fun initEditText() {
        accountEdit.addTextChangedListener(
            EditTextTextChangeListener({
                if (it.isNotEmpty()) {
                    accountClear.visibility = View.VISIBLE
                    accountNotNull = true
                } else {
                    accountClear.visibility = View.GONE
                    accountNotNull = false
                }
                canClick()
            }, { s: CharSequence, i: Int, i1: Int, i2: Int ->
                accountError.text = ""
            })
        )
        passwordEdit.addTextChangedListener(EditTextTextChangeListener({
            if (it.isNotEmpty()) {
                passwordClear.visibility = View.VISIBLE
                passwordNotNull = true
            } else {
                passwordClear.visibility = View.GONE
                passwordNotNull = false
            }
            canClick()
        }, { s: CharSequence, i: Int, i1: Int, i2: Int ->
            passwordError.text = ""
        }))
    }

    /**
     *判断@link [loginButton] 是否可用
     * 如可用 则设置背景为 @link [R.drawable.shape_bt_enable]
     * 不可用 则设置背景为 @link [R.drawable.shape_bt_disable]
     */
    private fun canClick() {
        if (accountNotNull && passwordNotNull) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                loginButton.background = this.resources.getDrawable(R.drawable.shape_bt_enable, null)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                loginButton.background = this.resources.getDrawable(R.drawable.shape_bt_enable)
            }
            loginButton.isEnabled = true
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                loginButton.background = this.resources.getDrawable(R.drawable.shape_bt_disable, null)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                loginButton.background = this.resources.getDrawable(R.drawable.shape_bt_disable)
            }
            loginButton.isEnabled = false
        }
    }

    /**
     * 设置@link [accountClear] [passwordClear] 的点击事件
     * 当点击时，删除@link [accountEdit] [passwordEdit]中的内容
     */
    private fun initClear() {
        val emptyString = SpannableStringBuilder("")
        accountClear.setOnClickListener {
            accountEdit.text = emptyString
        }
        passwordClear.setOnClickListener {
            passwordEdit.text = emptyString
        }
    }

    /**
     * 设置@link[loginButton] 的点击事件
     * 点击时 设置@link[loginButton] 的背景 （未实现）
     * 点击时 获取@link[accountEdit] [passwordEdit] 的内容
     * 并调用@link [checkAccount] 方法
     */
    private fun initButton() {
        loginButton.setOnClickListener {
            onLoginButtonClick()
        }
    }

    private fun onLoginButtonClick(){
        val state = isNetworkConnected(this)
        if (state == NetworkState.NONE){
            ToastUtil().setToast(this, "当前网络未连接")
        }else{
            val account: String = accountEdit.text.toString()
            val password: String = passwordEdit.text.toString()
            checkAccount(account, password)
        }
    }

    /**
     * 核对输入的账号密码是否正确
     * @param[account]: @link [accountEdit] 输入的账号
     * @param[password]: @link [passwordEdit] 输入的密码
     * 会调用@link[LoginUtil.checkAccount] 方法
     * 当回调结果为@link[LoginState.OK]时 调用@link[onResultOk]
     *
     */
    private fun checkAccount(account: String?, password: String?) {
        viewModel.checkAccount(account, password) { state: Int ->
            when (state) {
                LoginState.ACCOUNT_NULL -> accountError.text = "请输入账号"
                LoginState.NO_NETWORK -> accountError.text = "网络未连接，请联网后重试"
                LoginState.PASSWORD_NULL -> passwordError.text = "请输入密码"
                LoginState.ERROR -> passwordError.text = "账号或密码错误"
                LoginState.OK -> {
                    Log.d("aaa","OK")
                    val intent = Intent(this, MainActivity::class.java)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == FORGET_REQUEST_CODE && data != null){
            val account = data.getStringExtra("account")?:""
            accountEdit.text = SpannableStringBuilder(account)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }
}
