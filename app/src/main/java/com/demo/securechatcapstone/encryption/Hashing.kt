package com.demo.securechatcapstone.encryption

import at.favre.lib.crypto.bcrypt.BCrypt
import java.security.MessageDigest

class Hashing {
    fun hash(input: String, algorithm:String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance(algorithm)
        return md.digest(bytes).fold("", { str, it -> str + "%02x".format(it) })
    }

    fun hashWithBCrypt(input: String): ByteArray {
        val hash = BCrypt.withDefaults().hash(10, input.toCharArray())
        return hash
    }

    fun verifyWithBCrypt(input: String, hash: ByteArray): Boolean {
        val result = BCrypt.verifyer().verify(input.toCharArray(), hash)
        return result.verified
    }
}