package com.example.matechatting.function.cliphead

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.example.matechatting.R
import com.example.matechatting.function.mine.MineFragment
import com.example.matechatting.myview.CropView
import com.example.matechatting.utils.InjectorUtils
import com.example.matechatting.utils.statusbar.StatusBarUtil
import java.io.File
import java.io.IOException
import java.io.OutputStream

class ClipActivity : AppCompatActivity() {
    private lateinit var back: ImageView
    private lateinit var over: TextView
    private lateinit var clipView: CropView
    private lateinit var viewModel: ClipViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip)
        StatusBarUtil.setRootViewFitsSystemWindows(this, true)
        StatusBarUtil.setStatusBarDarkTheme(this, true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setStatusBarColor(this, this.getColor(R.color.bg_ffffff))
        }
        initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            init()
        }
    }

    private fun initView() {
        val factory = InjectorUtils.provideClipViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, factory).get(ClipViewModel::class.java)
        back = findViewById(R.id.clip_back)
        over = findViewById(R.id.clip_text_over)
        clipView = findViewById(R.id.crop_view)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun init() {
        val path = intent.getStringExtra("image_uri")
        if (path.isNullOrEmpty()) {
            return
        }
        val point = Point()
        this.windowManager.defaultDisplay.getSize(point)
        val width = point.x * (7 / 9f)
        clipView.apply {
            setBitmapForHeight(path, 1500)
            radius = width
            maxScale = 3f
            doubleClickScale = 1f
        }

        over.setOnClickListener {
            generateUriAndReturn()
        }
        back.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun generateUriAndReturn() {
        val zoomedCropBitmap = clipView.clip() ?: return
        val file = File(cacheDir, "cropped_" + System.currentTimeMillis() + ".jpg")
        val mSaveUri = Uri.fromFile(file)

        if (mSaveUri != null) {
            var outputStream: OutputStream? = null
            try {
                outputStream = contentResolver.openOutputStream(mSaveUri)
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                viewModel.postImage(file)
            } catch (ex: IOException) {
                Log.e("aaa", "Cannot open file: $mSaveUri", ex)
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
        val intent = Intent(this, MineFragment::class.java)
        intent.data = mSaveUri
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
