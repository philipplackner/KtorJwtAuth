package com.plcoding

import com.plcoding.data.user.MongoUserDataSource
import com.plcoding.data.user.User
import io.ktor.server.application.*
import com.plcoding.plugins.*
import com.plcoding.security.hashing.SHA256HashingService
import com.plcoding.security.token.JwtTokenService
import com.plcoding.security.token.TokenConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://philipplackner:$mongoPw@cluster0.mvi9z.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)
    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureRouting(userDataSource, hashingService, tokenService, tokenConfig)
    configureSerialization()
    configureMonitoring()
    configureSecurity(tokenConfig)
}
