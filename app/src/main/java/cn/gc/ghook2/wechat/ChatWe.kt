package cn.gc.ghook2.wechat

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * 微信hook类
 * Created by 宫成 on 2019-12-04 16:13.
 */
class ChatWe(private val loadPackageParam: XC_LoadPackage.LoadPackageParam) {
    companion object {
        const val weChatPackageName = "com.tencent.mm"
        const val weWorkPackageName = "com.tencent.wework"
        /**
         * log相关
         */
        private const val weChatLogClassName = "com.tencent.mm.xlog.app.XLogSetup"
        private const val weChatLogMethodName = "keep_setupXLog"
    }

    init {
        val packageName = loadPackageParam.packageName
        val classLoader = loadPackageParam.classLoader
        //打开微信debug日志
        weChatLogOpen(classLoader)
    }

    private fun weChatLogOpen(classLoader: ClassLoader) {

    }
}