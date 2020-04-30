package com.szmy.fireflies.ui

import android.graphics.BitmapFactory
import android.util.Log
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider
import com.alibaba.sdk.android.oss.model.GetObjectRequest
import com.alibaba.sdk.android.oss.model.GetObjectResult
import com.szmy.fireflies.R
import com.szmy.fireflies.constant.GlobalUtils
import com.szmy.fireflies.constant.WebConstant
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : BaseActivity() {

    private val oss:OSSClient
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    init {
        val credentialProvider = OSSAuthCredentialsProvider(WebConstant.StsServer)
        oss = OSSClient(GlobalUtils.getContext(), WebConstant.endPoint, credentialProvider)
    }

    override fun init() {
        super.init()
        getIcon.setOnClickListener {
            getIconFromOss("52A944F7FB75D2B52D612E0B11DC05F4")
        }
    }

    private fun getIconFromOss(nameMd5:String) {
        // 构造下载文件请求。
        val get = GetObjectRequest(WebConstant.Bucket, "head_img/$nameMd5.jpg")
//设置下载进度回调

        //设置下载进度回调

        //设置下载进度回调
        get.setProgressListener { request, currentSize, totalSize ->
            OSSLog.logDebug(
                "getobj_progress: $currentSize  total_size: $totalSize",
                false
            )
        }

        oss.getObject(get)

//        oss.asyncGetObject(get, object : OSSCompletedCallback<GetObjectRequest?, GetObjectResult> {
//            override fun onSuccess(request: GetObjectRequest?, result: GetObjectResult) {
//                // 请求成功。
//                Log.d("asyncGetObject", "DownloadSuccess")
//                Log.d("Content-Length", "" + result.contentLength)
//                val inputStream = result.objectContent
//
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                icon.setImageBitmap(bitmap)
//
//            }
//
//            // GetObject请求成功，将返回GetObjectResult，其持有一个输入流的实例。返回的输入流，请自行处理。
//            override fun onFailure(
//                request: GetObjectRequest?,
//                clientExcepion: ClientException,
//                serviceException: ServiceException
//            ) {
//                // 请求异常。
//                clientExcepion.printStackTrace()
//                // 服务异常。
//                Log.e("ErrorCode", serviceException.errorCode)
//                Log.e("RequestId", serviceException.requestId)
//                Log.e("HostId", serviceException.hostId)
//                Log.e("RawMessage", serviceException.rawMessage)
//            }
//        })

    }
}
