package com.demo.securechatcapstone.encryption

import android.util.Base64
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
            val ivappend = Base64.encodeToString(ivParameterSpec.iv,Base64.DEFAULT)
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec)

            val encrypted = cipher.doFinal(value.toByteArray())

            return ivappend + String(Base64.encode(encrypted,Base64.DEFAULT))
        } catch (e: Exception) {
            Log.e("AES encrypt error", e.toString())
        }

        return null
    }

    fun decrypt(key: String, encrypted: String?): String? {
        try {
            val ivAppend = encrypted?.take(24)
            val cipherText = encrypted?.substring(24)
            val byte: ByteArray = Base64.decode(ivAppend,Base64.DEFAULT)
            val ivParameterSpec = IvParameterSpec(byte)
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec)

            val original = cipher.doFinal(Base64.decode(cipherText,Base64.DEFAULT))

            return String(original)
        } catch (e: Exception) {
            Log.e("AES decrypt error", e.toString())
        }

        return null
    }

    fun keyRandomGenerator():String{
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val secretKey = keyGenerator.generateKey()
        val bytes = secretKey.encoded
        return Base64.encodeToString(bytes,Base64.DEFAULT)
    }
}