package com.xdmse.logouttestfromscratch

import com.amazonaws.mobileconnectors.cognitoidentityprovider.util.CognitoJWTParser
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.kotlin.auth.KotlinAuthFacade

private const val COGNITO_USERNAME = "username"

suspend fun KotlinAuthFacade.authenticatedUserName() =
    (fetchAuthSession() as AWSCognitoAuthSession).run {
        try {
            buildUsernameFromAuthSession(this)
        } catch (e: NullPointerException) {
            null
        }
    }

internal fun buildUsernameFromAuthSession(session: AWSCognitoAuthSession): String? =
    CognitoJWTParser
        .getPayload(session.userPoolTokensResult.value?.accessToken.toString())
        .getString(COGNITO_USERNAME)