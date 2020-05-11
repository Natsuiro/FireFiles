package com.szmy.fireflies.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.szmy.fireflies.R
import kotlinx.android.synthetic.main.activity_push_msg.*
import kotlinx.android.synthetic.main.push_msg_header.*


class PushMsgActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_push_msg
    }

    override fun init() {
        super.init()

        cancel.setOnClickListener {
            finish()
        }

        push.setOnClickListener {
            val intent = Intent()
            intent.putExtra("msgContent",msgContent.text.toString())
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

        msgContent.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (push.visibility!=View.VISIBLE&&!TextUtils.isEmpty(s)){
                    push.visibility = View.VISIBLE
                }else if (push.visibility != View.GONE&&TextUtils.isEmpty(s)){
                    push.visibility = View.GONE
                }
            }

        })

    }

}
