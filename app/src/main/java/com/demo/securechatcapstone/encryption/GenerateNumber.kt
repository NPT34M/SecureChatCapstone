package com.demo.securechatcapstone.encryption

import java.math.BigInteger

class GenerateNumber {
    fun genPublicKey(p: BigInteger, g: BigInteger, privKey: BigInteger): BigInteger {
        return g.modPow(privKey, p)
    }

    fun secretKeyExchange(prime: BigInteger, publicKey: BigInteger, privateKey: BigInteger): BigInteger {
        return publicKey.modPow(privateKey, prime)
    }
}