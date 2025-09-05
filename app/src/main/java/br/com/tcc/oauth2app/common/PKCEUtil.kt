package br.com.tcc.oauth2app.common

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PKCEUtil {
    // Tamanho recomendado para o code verifier (entre 43 e 128 caracteres)
    private const val CODE_VERIFIER_LENGTH = 64

    /**
     * Gera um code verifier aleatório para PKCE
     * @return code verifier como string
     */
    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(CODE_VERIFIER_LENGTH)
        secureRandom.nextBytes(bytes)

        // Codifica os bytes para Base64 URL safe sem padding
        return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }

    /**
     * Gera um code challenge a partir do code verifier usando SHA-256
     * @param codeVerifier code verifier gerado previamente
     * @return code challenge como string
     */
    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(Charsets.US_ASCII)
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes)
        val digest = messageDigest.digest()

        // Codifica o hash para Base64 padrão
        val base64 = Base64.encodeToString(digest, Base64.NO_WRAP)

        // Aplica as mesmas substituições que o backend
        return base64
            .replace("+", "-")
            .replace("/", "_")
            .replace("=", "")
    }
}