package com.xdmse.logouttestfromscratch

import android.app.Application
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.kotlin.core.Amplify
import com.amplifyframework.logging.AndroidLoggingPlugin
import com.amplifyframework.logging.LogLevel

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Amplify.addPlugin(AndroidLoggingPlugin(LogLevel.VERBOSE))
        Amplify.addPlugin(AWSApiPlugin())
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        configureAmplify()
    }
}