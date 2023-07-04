package com.xdmse.logouttestfromscratch

import aws.sdk.kotlin.services.cognitoidentityprovider.model.LimitExceededException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.NotAuthorizedException
import aws.sdk.kotlin.services.cognitoidentityprovider.model.UserNotConfirmedException
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.cognito.exceptions.invalidstate.SignedInException
import com.amplifyframework.auth.cognito.options.AWSCognitoAuthSignInOptions
import com.amplifyframework.auth.cognito.options.AuthFlowType
import com.amplifyframework.kotlin.core.Amplify
import com.xdmse.logouttestfromscratch.LoginAccountEvent.*
import com.xdmse.logouttestfromscratch.LoginFailureCause.*

class CognitoAuthentication {

    suspend fun loginUser(username: String, password: String): LoginAccountEvent =
        try {
            val options = AWSCognitoAuthSignInOptions
                .builder()
                .authFlowType(AuthFlowType.USER_PASSWORD_AUTH)
                .build()
            val isSignedIn = Amplify.Auth.signIn(username, password, options).isSignedIn
            when {
                isSignedIn -> LoginSuccessEvent(Amplify.Auth.authenticatedUserName())
                else -> LoginFailureEvent(GENERIC_FAILURE)
            }
        } catch (exception: AuthException) {
            if(exception is SignedInException) {
                LoginFailureEvent(ALREADY_SIGNED_IN)
            } else {
                when (exception.cause) {
                    is NotAuthorizedException -> LoginFailureEvent(NOT_AUTHORISED)
                    is UserNotConfirmedException -> LoginFailureEvent(UNVERIFIED)
                    is LimitExceededException -> LoginFailureEvent(LIMIT_EXCEEDED)
                    else -> LoginFailureEvent(GENERIC_FAILURE)
                }
            }
        }

}

sealed class LoginAccountEvent {
    data class LoginSuccessEvent(val username: String?) : LoginAccountEvent()
    data class LoginFailureEvent(val cause: LoginFailureCause) : LoginAccountEvent()
}

enum class LoginFailureCause {
    NOT_AUTHORISED,
    UNVERIFIED,
    GENERIC_FAILURE,
    LIMIT_EXCEEDED,
    ALREADY_SIGNED_IN
}