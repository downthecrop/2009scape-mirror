package core.net.websocket

import core.ServerConstants
import core.api.log
import core.game.system.config.ServerConfigParser
import core.tools.Log
import org.java_websocket.server.DefaultSSLWebSocketServerFactory
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext

object WebSocketTls {

    private const val KEYSTORE_TYPE = "PKCS12"

    fun configure(server: GameWebSocketServer) {
        if (!ServerConstants.WEBSOCKET_TLS_ENABLED) {
            return
        }
        server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(createContext()))
    }

    private fun createContext(): SSLContext {
        val keystorePath = ServerConstants.WEBSOCKET_TLS_KEYSTORE_PATH.trim()
        if (keystorePath.isEmpty()) {
            throw IllegalStateException("server.websocket_tls_enabled is true but websocket_tls_keystore_path is blank")
        }
        val resolvedKeystorePath = ServerConfigParser.parsePath(keystorePath)
        val keystorePassword = ServerConstants.WEBSOCKET_TLS_KEYSTORE_PASSWORD.toCharArray()

        val keyStore = KeyStore.getInstance(KEYSTORE_TYPE)
        FileInputStream(resolvedKeystorePath).use { input ->
            keyStore.load(input, keystorePassword)
        }

        val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyManagerFactory.init(keyStore, keystorePassword)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(keyManagerFactory.keyManagers, null, null)
        log(WebSocketTls::class.java, Log.INFO, "Configured secure websocket listener using keystore $resolvedKeystorePath.")
        return sslContext
    }
}
