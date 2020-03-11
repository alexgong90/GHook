package cn.gc.ghook2;

import android.app.Notification;
import android.util.Log;
import cn.gc.ghook2.wechat.ChatWe;
import cn.gc.ghook2.wechat.ChatWeAction;
import cn.gc.ghook2.wework.WeWorkAction;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 代理hook类
 *
 * @author 宫成
 * @date 2019-12-04 16:04
 */
public class GHook implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packageName = loadPackageParam.packageName;
        ClassLoader classLoader = loadPackageParam.classLoader;
        Log.i("G_C", "handleLoadPackage: " + packageName);
        //hook微信
        hookWeChat(packageName, classLoader,loadPackageParam);
    }

    private void hookWeChat(String packageName, ClassLoader classLoader, XC_LoadPackage.LoadPackageParam loadPackageParam) {
        /*if (ChatWe.weChatPackageName.equals(packageName)) {
            XposedBridge.log("=========Loaded app: " + packageName);
            ChatWeAction action = new ChatWeAction(loadPackageParam);
        }*/
        if (WeWorkAction.wework_package.equals(packageName)) {
            XposedBridge.log("=========Loaded app: " + packageName);
            WeWorkAction action = new WeWorkAction(loadPackageParam);
        }

        Class clazz =  XposedHelpers.findClass(
                "android.app.NotificationManager", loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "notify", String.class, int.class, Notification.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Notification notification = (Notification) param.args[2];
                        String content = (String) notification.extras.get(Notification.EXTRA_TEXT);
                        XposedBridge.log("hsdhfsdhfshdfhsdfhsdfh " + content);
                    }
                }
        );
        XposedHelpers.findAndHookMethod(clazz, "notify", int.class, Notification.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        Notification notification = (Notification) param.args[1];
                        String content = (String) notification.extras.get(Notification.EXTRA_TEXT);
                        XposedBridge.log("qweqwrqwrqweqwerqw " + content);
                    }
                }
        );



    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
//        this.sharedPreferences =new XSharedPreferences(modulePackageName, "default");
    }
}
