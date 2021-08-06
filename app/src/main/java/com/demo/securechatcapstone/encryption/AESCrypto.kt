package com.demo.securechatcapstone.encryption

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AESCrypto {
    fun encrypt(input: String, secret: String): String {
        val cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(secret.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encrypt = cipher.doFinal(input.toByteArray());
        return Base64.encodeToString(encrypt, Base64.DEFAULT)
    }

    fun decrypt(input: String, secret: String): String {
        val cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(secret.toByteArray(), "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decrypt = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        return String(decrypt)
    }
}