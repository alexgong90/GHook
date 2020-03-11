package cn.gc.ghook2

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.DexClassLoader
import dalvik.system.PathClassLoader
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val context = App.mApp.createPackageContext("cn.gc.ghook",
            Context.CONTEXT_INCLUDE_CODE or Context.CONTEXT_IGNORE_SECURITY)
        val apkPath = context.packageCodePath
        val file = File(apkPath)
        val loader = PathClassLoader(file.absolutePath, ClassLoader.getSystemClassLoader())
        Log.i("ExampleUnitTest", "2: $apkPath")
        Test().doTest(loader)
        Log.i("ExampleUnitTest", "3")*/

        val externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val file = externalFilesDir!!.listFiles()[0]
//        println(externalFilesDir)
//        println(file.path)
//        println(file.absoluteFile)

    }
}
