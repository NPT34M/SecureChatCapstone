package com.demo.securechatcapstone.encryption

import java.util.Base64
import android.util.Log
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESCrypto {
    fun encrypt(key: String, value: String): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            val byte = ByteArray(16)
            SecureRandom().nextBytes(byte)
            val ivParameterSpec = IvParameterSpec(byte)
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec)

            val encrypted = cipher.doFinal(value.toByteArray())
            val a = ivParameterSpec.iv + encrypted
            val rs = Base64.getEncoder().encodeToString(a)
            return rs
        } catch (e: Exception) {
            Log.e("AES encrypt error", e.message.toString())
        }

        return null
    }

    fun decrypt(key: String, encrypted: String?): String? {
        try {
            val dec = Base64.getDecoder().decode(encrypted)
            val byte = dec.take(16).toByteArray()
            val cipherText = dec.takeLast(dec.size - 16).toByteArray()
            val ivParameterSpec = IvParameterSpec(byte)
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec)
            val original = cipher.doFinal(cipherText)

            return String(original)
        } catch (e: Exception) {
            Log.e("AES decrypt error", e.toString())
        }

        return null
    }

    fun keyRandomGenerator(): String {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val secretKey = keyGenerator.generateKey()
        val bytes = secretKey.encoded
        return Base64.getEncoder().encodeToString(bytes)
    }
}