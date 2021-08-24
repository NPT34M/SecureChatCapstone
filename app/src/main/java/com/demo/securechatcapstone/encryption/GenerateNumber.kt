package com.demo.securechatcapstone.encryption

import java.math.BigInteger

class GenerateNumber {
    fun genPublicKey(p: BigInteger, g: BigInteger, privKey: BigInteger): BigInteger {
        return g.modPow(privKey, p)
    }

    fun secretKeyExchange(
        primeStr: String,
        publicKeyStr: String,
        privateKeyStr: String
    ): BigInteger {
        val prime = BigInteger(primeStr)
        val publicKey = BigInteger(publicKeyStr)
        val privateKey = BigInteger(privateKeyStr)
        return publicKey.modPow(privateKey, prime)
    }
}