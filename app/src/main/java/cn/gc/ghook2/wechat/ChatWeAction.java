package cn.gc.ghook2.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.gc.ghook2.wechat.zhuanfa.MsgRetransmitEntity;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.util.List;

import static cn.gc.ghook2.utils.CommonKt.log;

/**
 * Created by 宫成 on 2019-12-04 16:25.
 */
public class ChatWeAction {
    private static final String weChatLogClassName = "com.tencent.mm.xlog.app.XLogSetup";
    private static final String weChatLogMethodName = "keep_setupXLog";
    private static final String TAG = "Xposed";

    private XC_LoadPackage.LoadPackageParam loadPackageParam;

    public ChatWeAction(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
        weChatLogOpen();
        //转发 页面
        msgRetransmitUI();
    }

    private void msgRetransmitUI() {

        XposedHelpers.findAndHookMethod(
                "com.tencent.mm.ui.transmit.MsgRetransmitUI",
                loadPackageParam.classLoader,
                "onCreate",
                Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        log("beforeHookedMethod onCreate");

                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("afterHookedMethod onCreate");
                    }
                });
        XposedHelpers.findAndHookMethod(
                Activity.class,
                "startActivityForResult",
                Intent.class, int.class, Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("beforeHookedMethod");

                        //转发页面
                        parseMsgRetransmitUI(param);


                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("afterHookedMethod");

                    }
                });
    }

    private void parseMsgRetransmitUI(XC_MethodHook.MethodHookParam param) {
        String activityName = param.thisObject.getClass().getName();
        Log.d("Xposed", "Started activity: " + activityName);
        Intent intent = (Intent) param.args[0];
        Log.d("Xposed", "原始Intent数据: " + intent.toString());
        int msgType = intent.getIntExtra("Retr_Msg_Type", -1);
        Log.i("Xposed", "beforeHookedMethod: msgRetransmitUI msgType = " + msgType);


        MsgRetransmitEntity msgRetransmitEntity = new MsgRetransmitEntity();
        msgRetransmitEntity.msgType = intent.getIntExtra("Retr_Msg_Type", -1);
        msgRetransmitEntity.diZ = intent.getStringExtra("Retr_Msg_content");
        msgRetransmitEntity.cUH = intent.getLongExtra("Retr_Msg_Id", -1);
        msgRetransmitEntity.fileName = intent.getStringExtra("Retr_File_Name");
        msgRetransmitEntity.CXv = intent.getStringArrayListExtra("Retr_File_Path_List");
        msgRetransmitEntity.CXy = msgRetransmitEntity.CXv != null && msgRetransmitEntity.CXv.size() > 0;
        msgRetransmitEntity.gxm = intent.getIntExtra("Retr_Compress_Type", 0);
        msgRetransmitEntity.CXs = intent.getIntExtra("Retr_Scene", 0);
        msgRetransmitEntity.length = intent.getIntExtra("Retr_length", 0);
        msgRetransmitEntity.CXr = intent.getIntExtra("Retr_video_isexport", 0);
        msgRetransmitEntity.CXn = intent.getStringExtra("Retr_Msg_thumb_path");
        msgRetransmitEntity.CXo = intent.getBooleanExtra("Retr_go_to_chattingUI", true);
        msgRetransmitEntity.CXC = intent.getBooleanExtra("Retr_start_where_you_are", true);
        boolean z = false;
        if (msgRetransmitEntity.CXs == 0) {
            z = true;
        } else {
            z = false;
        }
        msgRetransmitEntity.CXD = intent.getBooleanExtra("Multi_Retr", z);
        if (msgRetransmitEntity.CXD) {
            msgRetransmitEntity.CXC = true;
        }

        msgRetransmitEntity.CXp = intent.getBooleanExtra("Retr_show_success_tips", msgRetransmitEntity.CXC);
        msgRetransmitEntity.CXz = intent.getBooleanExtra("Edit_Mode_Sigle_Msg", false);
        msgRetransmitEntity.BRX = intent.getBooleanExtra("is_group_chat", false);
        msgRetransmitEntity.CrQ = intent.getIntExtra("Retr_Biz_Msg_Selected_Msg_Index", -1);
        msgRetransmitEntity.dag = intent.getStringExtra("Retr_NewYear_Thumb_Path");
        msgRetransmitEntity.CXA = intent.getIntExtra("Retr_MsgImgScene", 0);
        msgRetransmitEntity.gzk = intent.getFloatExtra("Retr_Longtitude", -1000.0f);
        msgRetransmitEntity.ded = intent.getFloatExtra("Retr_Latitude", -1000.0f);
        msgRetransmitEntity.gzl = intent.getStringExtra("Retr_AttachedContent");
        msgRetransmitEntity.CXB = "gallery".equals(intent.getStringExtra("Retr_From"));
        msgRetransmitEntity.mSessionId = intent.getStringExtra("reportSessionId");
        msgRetransmitEntity.CXF = intent.getIntExtra("Retr_MsgFromScene", 0);
        msgRetransmitEntity.CXG = intent.getStringExtra("Retr_MsgFromUserName");
        msgRetransmitEntity.CXH = intent.getStringExtra("Retr_MsgTalker");
        msgRetransmitEntity.CXI = intent.getIntExtra("Retr_MsgAppBrandFromScene", 1);
        msgRetransmitEntity.CXJ = intent.getIntExtra("Retr_MsgAppBrandServiceType", 0);

        Log.i("Xposed", "msgRetransmitEntity: " + msgRetransmitEntity);
        Log.i("Xposed", "msgRetransmitEntity.msgType: " + msgRetransmitEntity.msgType);
    }

    private void weChatLogOpen() {
        XposedHelpers.findAndHookMethod(
                weChatLogClassName,
                loadPackageParam.classLoader,
                weChatLogMethodName,
                boolean.class, String.class, String.class, Integer.class, Boolean.class, Boolean.class, String.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG, "is weChatLogOpen: " + param.args[5]);
                    }
                });
    }
}
