package com.dennis.zendeskfluttercombination.zendesk_flutter_combination


import android.app.Activity
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** ZendeskFlutterCombinationPlugin */
class ZendeskFlutterCombinationPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private val tag = "[ZendeskMessagingPlugin]"
    private lateinit var channel: MethodChannel
    var activity: Activity? = null
    var isInitialize: Boolean = false

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        val sendData: Any? = call.arguments
        val zendeskFlutterCombination = ZendeskFlutterCombination(this, channel)

        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }
            "initialize" -> {
                if (isInitialize) {
                    println("$tag - Messaging is already initialized!")
                    return
                }
                val appId = call.argument<String>("appId")!!
                val clientId = call.argument<String>("clientId")!!
                val userIdentity = call.argument<String>("userIdentity")!!
                val urlString = call.argument<String>("urlString")!!
                zendeskFlutterCombination.initialize(
                    appId = appId,
                    clientId = clientId,
                    userIdentity = userIdentity, urlString = urlString
                )
            }
            "showHelpCenter" -> {
                if (!isInitialize) {
                    println("$tag - Messaging needs to be initialized first")
                    return
                }
                zendeskFlutterCombination.showHelpCenter()
            }
            "showRequestList" -> {
                if (!isInitialize) {
                    println("$tag - Messaging needs to be initialized first")
                    return
                }
                zendeskFlutterCombination.showRequestList()
            }
            "showRequest" -> {
                if (!isInitialize) {
                    println("$tag - Messaging needs to be initialized first")
                    return
                }
                zendeskFlutterCombination.showRequest()
            }
            "registerFcmTokenInZendesk" -> {
                if (!isInitialize) {
                    println("$tag - Messaging needs to be initialized first")
                    return
                }
                val token = call.argument<String>("token")!!
                zendeskFlutterCombination.registerFcmTokenInZendesk(token)
            }
            else -> {
                result.notImplemented()
            }
        }

        if (sendData != null) {
            result.success(sendData)
        } else {
            try {
                result.success(0)
            } catch (e: Exception) {
                println("error e==")
                e.printStackTrace();
            }
        }
    }


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "zendesk_flutter_combination")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

}
