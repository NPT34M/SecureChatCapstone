package com.demo.securechatcapstone.encryption

import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import android.util.Base64
import javax.crypto.Cipher

class RSACrypto {
    fun getPublicKey(pubKey: String?): PublicKey {
        var publicKey: PublicKey? = null
        try {
            val keySpec =
                X509EncodedKeySpec(Base64.decode(pubKey?.toByteArray(), Base64.DEFAULT))
            val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
            publicKey = keyFactory.generatePublic(keySpec)
            return publicKey
        } catch (e: Exception) {
            Log.e("Get public key error", e.toString())
        }
        return publicKey!!
    }

    fun encrypt(plainText: String, publicKey: String): String? {
        try {
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey))
            return Base64.encodeToString(
                cipher.doFinal
                    (plainText.toByteArray()), Base64.DEFAULT
            )
        } catch (e: Exception) {
            Log.e("RSA encrypt error", e.toString())
        }
        return null
    }

    fun verifySignature(
        pubKeyServer: String,
        plainText: String?,
        signature: String?
    ): Boolean {
        val publicSignature: Signature = Signature.getInstance("SHA256withRSA")
        publicSignature.initVerify(RSACrypto().getPublicKey(pubKeyServer))
        publicSignature.update(plainText?.toByteArray(StandardCharsets.UTF_8))
        return publicSignature.verify(Base64.decode(signature?.toByteArray(),Base64.DEFAULT))
    }
}