package cn.gc.ghook2.wechat;

import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 宫成 on 2019-11-06 15:23.
 */
public class Tutorial implements IXposedHookLoadPackage {
    private static final String TAG = Tutorial.class.getSimpleName();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String packageName = lpparam.packageName;
        ClassLoader classLoader = lpparam.classLoader;
        hookWechat(packageName, classLoader);
    }

    private void hookWechat(String packageName, ClassLoader classLoader) {
        if ("com.tencent.mm".equals(packageName)) {
            XposedBridge.log("=========Loaded app: " + packageName);
            //打开微信debug日志
            weChatLogOpen(classLoader);
            jump(classLoader);
            jump2(classLoader);
        }
    }

    private void jump2(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.tencent.mm.sdk.platformtools.j",
                classLoader,
                "b", String.class,String.class,String.class,String.class,String.class,boolean.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG, "m13901b str = "+(String) param.args[0]);
                        Log.i(TAG, "m13901b str1 = "+(String) param.args[1]);
                        Log.i(TAG, "m13901b str2 = "+(String) param.args[2]);
                        Log.i(TAG, "m13901b str3 = "+(String) param.args[3]);
                        Log.i(TAG, "m13901b str4 = "+(String) param.args[4]);
                        Log.i(TAG, "m13901b z = "+(boolean) param.args[5]);
                        /*Intent intent = (Intent) param.args[0];
                        if (intent != null) {
                            Bundle extras = intent.getExtras();
                            if (extras != null) {
                                Log.i("G_C", "beforeHookedMethod: activity_log---->Activity.startActivity =>" +
                                        param.thisObject.getClass() + "intent =>" + extras.toString());
                            }
                        }*/

                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        super.afterHookedMethod(param);
                        Log.i(TAG, "m13901b-result: " + param.getResult());
                    }
                });
    }
    private void jump3(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.tencent.mm.sdk.platformtools.j",
                classLoader,
                "a", String.class,String.class,String.class,String.class,int.class,boolean.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Log.i(TAG, "m13900a str = "+(String) param.args[0]);
                        Log.i(TAG, "m13900a str1 = "+(String) param.args[1]);
                        Log.i(TAG, "m13900a str2 = "+(String) param.args[2]);
                        Log.i(TAG, "m13900a str3 = "+(String) param.args[3]);
                        Log.i(TAG, "m13900a i = "+(int) param.args[4]);
                        Log.i(TAG, "m13900a z = "+(boolean) param.args[5]);
                        /*Intent intent = (Intent) param.args[0];
                        if (intent != null) {
                            Bundle extras = intent.getExtras();
                            if (extras != null) {
                                Log.i("G_C", "beforeHookedMethod: activity_log---->Activity.startActivity =>" +
                                        param.thisObject.getClass() + "intent =>" + extras.toString());
                            }
                        }*/

                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        super.afterHookedMethod(param);
                        Log.i(TAG, "m13900a-result: " + param.getResult());
                    }
                });
    }

    /*
    findAndHookMethod(
        "android.app.Activity", pkg.loader,
        "startActivity", C.Intent, object : XC_MethodHook() {
    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        val intent = param.args[0] as Intent?
        if (isActivityTest) {
            LogTool.e("activity_log---->Activity.startActivity => " +
                    "${param.thisObject.javaClass}, " +
                    "intent => ${bundleToString(intent?.extras)}")
        }
    }
})
     */
    private void jump(final ClassLoader classLoader) {

        XposedHelpers.findAndHookMethod("com.tencent.mm.au.g",
                classLoader,
                "a", String.class,String.class,String.class,boolean.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        String str = (String) param.args[0];
                        Log.i("G_C", "mo33371a str = "+str);
                        /*Intent intent = (Intent) param.args[0];
                        if (intent != null) {
                            Bundle extras = intent.getExtras();
                            if (extras != null) {
                                Log.i("G_C", "beforeHookedMethod: activity_log---->Activity.startActivity =>" +
                                        param.thisObject.getClass() + "intent =>" + extras.toString());
                            }
                        }*/

                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                        super.afterHookedMethod(param);
                        Log.i(TAG, "keep_setupXLog参数isLogcatOpen: " + param.getResult());
                    }
                });
    }

    private void weChatLogOpen(final ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.tencent.mm.xlog.app.XLogSetup",
                classLoader,
                "keep_setupXLog", boolean.class, String.class, String.class, Integer.class, Boolean.class, Boolean.class, String.class,
                new XC_MethodHook() {

                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.args[5] = true;
                        super.afterHookedMethod(param);
                        Log.i(TAG, "keep_setupXLog参数isLogcatOpen: " + param.args[5]);
                    }
                });
    }
}
