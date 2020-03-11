package cn.gc.ghook2.wework

import android.app.Notification
import android.app.NotificationManager
import android.util.Log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class WeWorkAction(val llpp: XC_LoadPackage.LoadPackageParam) {
    companion object {
        const val wework_package = "weWorkPackageName"
    }

    init {
        openLog()
        hookContactService()
        hookConversationService()
        hookNotification()
    }

    private fun hookNotification() {
        val TAG = "hookNotification"
        XposedHelpers.findAndHookMethod(NotificationManager::class.java,"notify", String::class.java,Int::class.java,Notification::class.java,
            object :XC_MethodHook(){
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    XposedBridge.log("hookNotification before")
                    val notification = param!!.args[2] as Notification
                    val extras = notification.extras
                    val contentText = extras.getCharSequence(Notification.EXTRA_TEXT,"")

                    Log.i(TAG,"contentText: ${contentText.toString()}")
                }
            }
        )
        val clazz =  XposedHelpers.findClass(
            "android.app.NotificationManager", llpp.classLoader)
        XposedHelpers.findAndHookMethod(clazz,"notify", String::class.java,Int::class.java,Notification::class.java,
            object :XC_MethodHook(){
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    XposedBridge.log("hookNotification before")
                    val notification = param!!.args[2] as Notification
                    val extras = notification.extras
                    val contentText = extras.getCharSequence(Notification.EXTRA_TEXT,"")

                    Log.i(TAG,"contentText: ${contentText.toString()}")
                }
            }
        )
    }

    private fun hookConversationService() {
        val TAG = "ConversationService"
        val ContactService = XposedHelpers.findClass(
            "com.tencent.wework.foundation.logic.ConversationService",
            llpp.classLoader
        )
        val Conversation = XposedHelpers.findClass("com.tencent.wework.foundation.model.Conversation",llpp.classLoader)
        val Message = XposedHelpers.findClass("com.tencent.wework.foundation.model.Message",llpp.classLoader)
        val ISendMessageCallback = XposedHelpers.findClass("com.tencent.wework.foundation.callback.ISendMessageCallback",llpp.classLoader)
        XposedHelpers.findAndHookMethod(ContactService,"SendMessage",Conversation,Message,ISendMessageCallback ,object :XC_MethodHook(){
            override fun afterHookedMethod(param: MethodHookParam?) {

            }

            override fun beforeHookedMethod(param: MethodHookParam?) {
                Log.i(TAG,"SendMessage: arg0: ${param!!.args[0]}   arg1: ${param!!.args[1]}")
            }
        })


        XposedHelpers.findAndHookMethod(ContactService,"convertToProtocolMessage",Message ,object :XC_MethodHook(){
            override fun afterHookedMethod(param: MethodHookParam?) {
                Log.i(TAG,"convertToProtocolMessage: arg0: ${param!!.args[0]}   arg result: ${param.result}")
            }

            override fun beforeHookedMethod(param: MethodHookParam?) {

            }
        })


    }


    private fun openLog() {
        val TAG = "openLog"
        Log.d("WeWorkLogPlugin", "======= p000.Log start =======")
        val logClassName: String = "euj$" + "a"
        val classLog = XposedHelpers.findClass(logClassName, llpp.classLoader)
        XposedHelpers.setStaticBooleanField(classLog, "IsOpenLog", true)
        XposedHelpers.setStaticBooleanField(classLog, "gpK", true)
        XposedHelpers.setStaticBooleanField(classLog, "gpL", true)
        XposedHelpers.setStaticBooleanField(classLog, "gpM", true)
        XposedHelpers.setStaticBooleanField(classLog, "gpN", true)

        XposedHelpers.findAndHookMethod("euj",llpp.classLoader,"v", String::class.java,
            Array<Any>::class.java,object : XC_MethodHook(){
                override fun afterHookedMethod(param: MethodHookParam?) {
//                    param.args[1] as
                    Log.i(TAG,"eju .v : arg0: ${param!!.args[0] as String}, arg1: ${param.args[1]}")
                }
            }
        )
    }


    private fun hookContactService() {
        val TAG = "hookContactService"

        val ContactService = XposedHelpers.findClass(
            "com.tencent.wework.foundation.logic.ContactService",
            llpp.classLoader
        )
        XposedHelpers.findAndHookMethod(ContactService,"SyncContactList",Int::class.java,object :XC_MethodHook(){
            override fun afterHookedMethod(param: MethodHookParam?) {
                Log.i(TAG,"SyncContactList: arg0: ${param!!.args[0] as String}")

            }
        })


        XposedHelpers.findClass("com.tencent.wework.foundation.model.User",llpp.classLoader)

        XposedHelpers.findAndHookMethod(ContactService,"GetCachedContactList",object :XC_MethodHook(){
            override fun afterHookedMethod(param: MethodHookParam?) {
                Log.i(TAG,"SyncContactList: result: ${param?.result}")

            }

            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
            }
        })


        val IContactServiceObserver = XposedHelpers.findClass(
            "com.tencent.wework.foundation.observer.IContactServiceObserver",
            llpp.classLoader
        )

        XposedHelpers.findAndHookMethod(ContactService,"addContactServiceObserver",IContactServiceObserver, object :XC_MethodHook(){
            override fun afterHookedMethod(param: MethodHookParam?) {
                Log.i(TAG,"SyncContactList: result: ${param?.result}")

            }

            override fun beforeHookedMethod(param: MethodHookParam?) {
                Log.i(TAG,"addContactServiceObserver: arg0: ${param!!.args[0] as String}}")

            }
        })



    }
}