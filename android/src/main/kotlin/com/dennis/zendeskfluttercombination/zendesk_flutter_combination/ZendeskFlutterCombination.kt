package com.dennis.zendeskfluttercombination.zendesk_flutter_combination

import android.util.Log
import com.zendesk.service.ErrorResponse
import com.zendesk.service.ZendeskCallback
import io.flutter.plugin.common.MethodChannel
import zendesk.core.JwtIdentity
import zendesk.core.Zendesk
import zendesk.support.Support
import zendesk.support.guide.HelpCenterActivity
import zendesk.support.request.RequestActivity
import zendesk.support.requestlist.RequestListActivity

class ZendeskFlutterCombination(
    private val plugin: ZendeskFlutterCombinationPlugin,
    private val channel: MethodChannel
) {

    companion object {
        const val tag = "[ZendeskMessaging]"

        // Method channel callback keys
        const val initializeSuccess: String = "initialize_success"
        const val initializeFailure: String = "initialize_failure"
        const val loginSuccess: String = "login_success"
        const val loginFailure: String = "login_failure"
        const val logoutSuccess: String = "logout_success"
        const val logoutFailure: String = "logout_failure"
        const val registerFcmTokenSuccess: String = "register_token_success"
    }

    /**
     * @param urlString Application URL from Zendesk admin
     * @param appId Application ID from Zendesk admin
     * @param clientId Client ID from Zendesk admin
     * @param userIdentity User ID for lookup on webhook backend server. Eg.: jwt
     */
    fun initialize(
        urlString: String,
        appId: String,
        clientId: String,
        userIdentity: String,
    ) {
        println("$tag - clientId== - $clientId")
        Zendesk.INSTANCE.init(
            plugin.activity!!,
            urlString,
            appId,
            clientId,
        )

//        val identity = AnonymousIdentity.Builder()
//            .withNameIdentifier(userIdentity)
//            .build()
        val jwtIdentity = JwtIdentity(userIdentity)
        Zendesk.INSTANCE.setIdentity(jwtIdentity)
        Support.INSTANCE.init(Zendesk.INSTANCE)
        plugin.isInitialize = true
        channel.invokeMethod(initializeSuccess, null)
    }

    fun showHelpCenter(){
        HelpCenterActivity.builder().show(plugin.activity!!);
    }

    fun showRequestList(){
        val config = HelpCenterActivity.builder().withLabelNames("status", "is", "solved").config()
        RequestListActivity.builder().show(plugin.activity!!, config)
    }

    fun showRequest() {
        RequestActivity.builder().show(plugin.activity!!)
    }

    fun registerFcmTokenInZendesk(token: String) {
        Zendesk.INSTANCE.provider()
            ?.pushRegistrationProvider()
            ?.registerWithDeviceIdentifier(token, object : ZendeskCallback<String>() {
                override fun onSuccess(p0: String?) {
                    // channel.invokeMethod(registerFcmTokenSuccess)
                }

                override fun onError(p0: ErrorResponse?) {
                    Log.e(tag, "Error at registerFcmTokenInZendesk: ${p0?.reason}")
                }

            })
    }
}