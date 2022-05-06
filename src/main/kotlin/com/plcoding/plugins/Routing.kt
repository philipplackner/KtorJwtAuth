package com.plcoding.plugins

import com.plcoding.authenticate
import com.plcoding.data.user.UserDataSource
import com.plcoding.getSecretInfo
import com.plcoding.security.hashing.HashingService
import com.plcoding.security.token.TokenConfig
import com.plcoding.security.token.TokenService
import com.plcoding.signIn
import com.plcoding.signUp
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(userDataSource, hashingService, tokenService, tokenConfig)
        signUp(hashingService, userDataSource)
        authenticate()
        getSecretInfo()
    }
}
