package com.jackstraw.market

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import com.jackstraw.market.util.SpFormName
import com.jackstraw.market.util.SpUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val addressValue = SpUtil.getFromSP(SpFormName.CONTENT_FORM, "addressValue", "")
        if(TextUtils.isEmpty(addressValue)){
            setContentView(R.layout.activity_main)
            btnSubmit.setOnClickListener {
                if(!TextUtils.isEmpty(editText.text)){
                    try {
                        val address = this.editText.text.toString().trim()
                        val uri = Uri.parse(address)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                        SpUtil.saveToSP(SpFormName.CONTENT_FORM, "addressValue", address)
                        finish()
                    } catch (e: Exception) {
                        //打印输出异常
                        e.printStackTrace()
                        Toast.makeText(this@MainActivity, "URL地址格式有误", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(this@MainActivity, "请输入URL地址", Toast.LENGTH_SHORT).show()
                }
            }
        }else {
            val uri = Uri.parse(addressValue)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
            finish()
        }
    }
}
