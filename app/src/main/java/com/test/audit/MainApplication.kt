package com.test.audit

import android.app.AppOpsManager
import android.app.Application
import android.app.AsyncNotedAppOp
import android.app.SyncNotedAppOp
import android.util.Log

private const val MY_APP_TAG = "I'M A TAG"

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val appOpsCallback = object : AppOpsManager.OnOpNotedCallback() {
            override fun onNoted(syncNotedAppOp: SyncNotedAppOp) {
                logPrivateDataAccess(
                    syncNotedAppOp.op,
                    syncNotedAppOp.attributionTag,
                    Throwable().stackTrace.toString()
                )
            }

            override fun onSelfNoted(syncNotedAppOp: SyncNotedAppOp) {
                logPrivateDataAccess(
                    syncNotedAppOp.op,
                    syncNotedAppOp.attributionTag,
                    Throwable().stackTrace.toString()
                )
            }

            override fun onAsyncNoted(asyncNotedAppOp: AsyncNotedAppOp) {
                logPrivateDataAccess(
                    asyncNotedAppOp.op,
                    asyncNotedAppOp.attributionTag,
                    asyncNotedAppOp.message
                )
            }
        }

        val appOpsManager = getSystemService(AppOpsManager::class.java) as AppOpsManager
        appOpsManager.setOnOpNotedCallback(mainExecutor, appOpsCallback)
    }

    private fun logPrivateDataAccess(opCode: String, attributionTag: String?, trace: String) {
        Log.e(
            MY_APP_TAG, "Private data accessed. " +
                "Operation: $opCode\n " +
                "Attribution Tag:$attributionTag\nStack Trace:\n$trace"
        )
    }
}