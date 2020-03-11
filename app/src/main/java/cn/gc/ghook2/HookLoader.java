package cn.gc.ghook2;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.io.File;
import java.util.Objects;

/**
 *
 * @author 宫成
 * @date 2019-12-04 14:45
 * xposed的真正入口, hook application的attach方法, 启动代理apk来执行hook, 避免修改代码重启手机, 只需要杀死进程,重新读入修改代码后的apk即可.
 */
public class HookLoader implements IXposedHookLoadPackage, IXposedHookZygoteInit {
    private final String apkPackageName = Objects.requireNonNull(HookLoader.class.getPackage()).getName();
    private final String apkHookClassName = GHook.class.getName();

    private StartupParam startupParam;
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        // 排除系统应用
        if (loadPackageParam.appInfo == null ||
                (loadPackageParam.appInfo.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) == 1) {
            return;
        }
        //hook application 获取他的 ClassLoader实例
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context) param.args[0];
                loadPackageParam.classLoader = context.getClassLoader();
                Class<?> cls = getClassFromDelegateApk(context, apkPackageName, apkHookClassName);
                Object obj = cls.newInstance();
                //调用代理类的初始化方法 todo 有用吗?
                cls.getDeclaredMethod("initZygote", startupParam.getClass()).invoke(obj, startupParam);
                //调用代理apk的代理hook方法.
                cls.getDeclaredMethod("handleLoadPackage", loadPackageParam.getClass()).invoke(obj, loadPackageParam);
            }
        });
    }

    private Class<?> getClassFromDelegateApk(Context context, String packageName, String hookClassName) throws ClassNotFoundException {
        File apkFile = findApkFile(context, packageName);
        if (apkFile == null) {
            throw new RuntimeException("Hook代理apk找不到");
        }
        PathClassLoader classLoader = new PathClassLoader(apkFile.getAbsolutePath(), ClassLoader.getSystemClassLoader());
        return Class.forName(hookClassName, true, classLoader);
    }

    /**
     * 根据包名构建目标Context,并调用getPackageCodePath()来定位apk
     *
     * @param context           context参数
     * @param modulePackageName 当前模块包名
     * @return apk file
     */
    private File findApkFile(Context context, String modulePackageName) {
        if (context == null) {
            return null;
        }
        try {
            Context moudleContext = context.createPackageContext(modulePackageName, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
            String apkPath = moudleContext.getPackageCodePath();
            return new File(apkPath);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        this.startupParam = startupParam;
    }
}
