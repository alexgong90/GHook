package cn.gc.ghook2;

import android.app.Application;
import android.content.Context;
import me.weishu.reflection.Reflection;

/**
 * Created by 宫成 on 2019-12-04 16:46.
 */
public class App extends Application {
    public static App mApp;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Reflection.unseal(base);
    }
}
