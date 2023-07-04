package com.xdmse.logouttestfromscratch

import android.app.Application
import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.kotlin.core.Amplify

private const val MAX_ATTEMPTS = 3

fun Application.configureAmplify(currentTry: Int = 0, listOfFailures: MutableList<String> = mutableListOf()) {
    val timesAttempted = currentTry + 1
    try {
        Amplify.configure(applicationContext)
    } catch (e: AuthException) {
        if (timesAttempted <= MAX_ATTEMPTS) {

            val failureMessage = "Amplify.configure() failure $timesAttempted: ${e.message}"
            listOfFailures.add(failureMessage)
            Log.e("LogoutTestApplication", "Failed with message: $failureMessage")
            Log.e("LogoutTestApplication", "Failed with Exception: $e")
            configureAmplify(timesAttempted, listOfFailures)
        } else {
            Log.e("LogoutTestApplication", "AuthException in Amplify.configure() but $MAX_ATTEMPTS retries exhausted - failures: ${listOfFailures.joinToString()}")
            Log.e("LogoutTestApplication", "Failed with Exception: $e")
        }
    }
    catch (e: Exception){
        // This is most likely due to the Amplify.configure being partially done
        val message = "Non AuthException in Amplify.configure() - " +
                (e.message ?: "Unable to extract message") +
                " Previous failures: - ${listOfFailures.joinToString()}"
        Log.e("LogoutTestApplication", message)
    }
}