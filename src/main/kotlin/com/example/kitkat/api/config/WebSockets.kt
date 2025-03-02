import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlin.time.Duration.Companion.seconds


fun Application.configureWebSocket() {
    install(WebSockets) {
        pingPeriod = 15.seconds // Utilise .seconds avec kotlin.time.Duration
        timeout = 30.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}
