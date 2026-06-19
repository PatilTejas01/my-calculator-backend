package com.tejas.securechatserver

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.* // Explicitly added routing package extension
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.channels.consumeEach
import org.json.JSONObject

// Global thread-safe map tracking active clients: Key = User ID, Value = Open Socket Session
val activeSessions = ConcurrentHashMap<Int, DefaultWebSocketServerSession>()

fun main() {
    val systemPort = System.getenv("PORT")?.toIntOrNull() ?: 8080

    println("Initializing Secure Switchboard Engine on Port $systemPort...")

    embeddedServer(Netty, port = systemPort, host = "0.0.0.0") {
        install(WebSockets)

        // Using explicit routing wrapper block removes 'receiver type mismatch' errors completely
        routing {
            webSocket("/chat") {
                val userId = call.request.headers["X-User-ID"]?.toIntOrNull()

                if (userId == null) {
                    close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Missing Identity Authorization"))
                    return@webSocket
                }

                activeSessions[userId] = this
                println(">>> Session Authenticated: User ID [$userId] is ONLINE. (Total active nodes: ${activeSessions.size})")

                try {
                    // explicitly scoping the incoming channel removes unresolved 'incoming' error
                    this.incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val rawText = frame.readText()

                            val jsonPacket = JSONObject(rawText)
                            val targetRecipientId = jsonPacket.getInt("receiverId")

                            val destinationPipe = activeSessions[targetRecipientId]
                            if (destinationPipe != null) {
                                destinationPipe.send(Frame.Text(rawText))
                                println("Routed Encrypted Frame: User $userId -> User $targetRecipientId")
                            } else {
                                println("Delivery Failed: User $targetRecipientId is currently offline.")
                            }
                        }
                    }
                } catch (e: Exception) {
                    println("Exception encountered on User $userId channel: ${e.localizedMessage}")
                } finally {
                    activeSessions.remove(userId)
                    println("<<< Session Severed: User ID [$userId] is OFFLINE. (Total active nodes: ${activeSessions.size})")
                }
            }
        }
    }.start(wait = true)
}