package com.example.e2ee_mvp.encryption

import java.math.BigInteger
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class GenerateNumber {
    fun privateKey(prime: BigInteger): BigInteger {
        var key: BigInteger
        do {
            key = BigInteger(prime.bitLength()-2, Random())
        } while (key < BigInteger.ONE || key >= prime)
        return key
    }

    fun publicKey(p: BigInteger, g: BigInteger, privKey: BigInteger): BigInteger {
        return g.modPow(privKey, p)
    }

    fun secretKeyExchange(prime: BigInteger, publicKey: BigInteger, privateKey: BigInteger): BigInteger {
        return publicKey.modPow(privateKey, prime)
    }
}