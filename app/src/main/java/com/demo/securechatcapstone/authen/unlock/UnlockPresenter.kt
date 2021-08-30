package com.demo.securechatcapstone.authen.unlock

import android.util.Log
import com.demo.securechatcapstone.App
import com.demo.securechatcapstone.encryption.AESCrypto
import com.demo.securechatcapstone.encryption.Hashing
import com.demo.securechatcapstone.encryption.RSACrypto
import com.demo.securechatcapstone.encryption.GenerateNumber as genNum
import com.demo.securechatcapstone.localDB.AppDatabase
import com.demo.securechatcapstone.localDB.user.UserInfoLocal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern

class UnlockPresenter(val view: UnlockContract.View, appDatabase: AppDatabase) :
    UnlockContract.Presenter {
    private val appDatabase: AppDatabase

    init {
        view.presenter = this
        this.appDatabase = appDatabase
    }

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userDao = appDatabase.userInfoLocalDAO()

    override fun verifyLogin(): Boolean {
        return firebaseAuth.uid == null
    }

    override fun isUserInfoExistInDevice(): Boolean {
        val user = userDao.getOneUser(firebaseAuth.uid!!)
        return user != null
    }

    override fun loadPrivateInfoFromDB() {
        val ref = firebaseDatabase.getReference("user-private/${firebaseAuth.uid}/privateInfo")
        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result?.value != null) {
                    view.loadFromRealTimeDBAndUnlock()
                } else {
                    view.createNew()
                }
            } else {
                view.unlockFail()
            }
        }
    }

    override fun createUserInfoLocal(password: String) {
        val PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!\\-_?&])(?=\\S+$).{8}")
        if (password.isEmpty() || !PASSWORD_REGEX.matcher(password).matches()) {
            view.unlockFail()
            return
        } else {
            val privateNumber = BigInteger(BigInteger(App.pNumber!!).bitLength() - 2, Random())
            val encryptPrivateForUpload = encryptPriKeyWithFormat(privateNumber, password)
            firebaseDatabase.getReference("/users/${firebaseAuth.uid}").child("privateInfoUpload")
                .setValue(encryptPrivateForUpload).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val userInfo = UserInfoLocal(
                            firebaseAuth.uid!!,
                            privateNumber.toString(),
                            password
                        )
                        val publicKey: BigInteger = genNum().genPublicKey(
                            BigInteger(App.pNumber!!),
                            BigInteger(App.gNumber!!),
                            privateNumber
                        )
                        val resultEncrypt = encryptPubKeyWithFormat(publicKey, App.pubKeyServer)
                        firebaseDatabase.getReference("/users/${firebaseAuth.uid}")
                            .child("keyUpload")
                            .setValue(resultEncrypt).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    userDao.insertNewUser(userInfo)
                                    view.unlockSuccess()
                                } else {
                                    view.unlockFail()
                                }
                            }
                    } else {
                        view.unlockFail()
                    }
                }

        }
    }

    private fun encryptPubKeyWithFormat(pubKey: BigInteger, key: String): String {
        val publicKey = pubKey.toString()
        val pubKeyHash = Hashing().hash(publicKey, "SHA-256")

        //Encrypt with server's public key
        val pubKeyEncrypt = RSACrypto().encrypt(publicKey, key)
        val pubKeyHashEncrypt = RSACrypto().encrypt(pubKeyHash, key)

        //Format
        return pubKeyEncrypt + "M43TPN" + pubKeyHashEncrypt
    }

    private fun encryptPriKeyWithFormat(priKey: BigInteger, passwordUnlock: String): String? {
        val privateKey = priKey.toString()
        val privateKeyHash = Hashing().hash(privateKey, "SHA-256")

        //Create key from unlock password
        val createKeyEncrypt = Hashing().hash(passwordUnlock, "SHA-256")

        //Encrypt private key and hash(private key)
        val privateKeyEncrypt = AESCrypto().encrypt(createKeyEncrypt.take(16), privateKey)
        val privateKeyHashEncrypt = AESCrypto().encrypt(createKeyEncrypt.take(16), privateKeyHash)

        //Format result
        val result = privateKeyEncrypt + "M43TPN" + privateKeyHashEncrypt

        //Hash result
        val hashResult = Hashing().hash(result, "SHA-256")

        //Random session key
        val sessionKey = AESCrypto().keyRandomGenerator()
        val rsaSessionKey = RSACrypto().encrypt(sessionKey, App.pubKeyServer)

        //Encrypt with session key
        val aesResultEncrypt = AESCrypto().encrypt(sessionKey.take(16), result)
        val aesHashEncrypt = AESCrypto().encrypt(sessionKey.take(16), hashResult)

        //Format result
        val finalEncrypt = aesResultEncrypt + "M43TPN" + aesHashEncrypt + "NPT34M" + rsaSessionKey

        return finalEncrypt
    }

    override fun checkSignaturePrivateInfo(password: String) {
        firebaseDatabase.getReference("user-private/${firebaseAuth.uid}/privateInfo").get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //Get private info
                    val privateInfo = it.result?.value.toString()
                    firebaseDatabase.getReference("user-private/${firebaseAuth.uid}/privateInfoSignature")
                        .get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                //Get signature of private info
                                val signature = it.result?.value.toString()
                                //Verify
                                if (RSACrypto().verifySignature(
                                        App.pubKeyServer,
                                        privateInfo,
                                        signature
                                    )
                                ) {
                                    decryptToUnlockAndSavePrivateInfo(privateInfo, password)
                                } else {
                                    //dialog
                                    view.dialog()
                                }
                            } else {
                                Log.e("Get private info signature error", it.exception.toString())
                            }
                        }
                } else {
                    Log.e("Get private info error", it.exception.toString())
                    return@addOnCompleteListener
                }
            }
    }

    private fun decryptToUnlockAndSavePrivateInfo(info: String, password: String) {
        try {
            //Split with format and get cipher text of private number and hash
            val privateNumberEncrypt = info.split("M43TPN")[0].replace("\n","")
            val hashEncrypt = info.split("M43TPN")[1].replace("\n","")

            //Create key for decrypt with unlock password
            val createKeyDecrypt = Hashing().hash(password, "SHA-256")

            //Decrypt
            val privateNumber = AESCrypto().decrypt(createKeyDecrypt.take(16), privateNumberEncrypt)
            val hash = AESCrypto().decrypt(createKeyDecrypt.take(16), hashEncrypt)

            //Compare Hash(privateNumber) and hash after decrypt
            if (Hashing().hash(privateNumber!!, "SHA-256").equals(hash)) {
                val userInfo = UserInfoLocal(
                    firebaseAuth.uid!!,
                    privateNumber,
                    password
                )
                userDao.insertNewUser(userInfo)
                view.unlockSuccess()
            } else {
                view.unlockFail()
            }
        } catch (e: Exception) {
            view.unlockFail()
            Log.e("Decrypt with unlock password error", e.toString())
        }
    }

    override fun checkUnlockPassword(string: String): Boolean {
        if (string.isEmpty()) {
            return false
        }
        val password = userDao.getOneUser(firebaseAuth.uid!!)?.password2
        return password.equals(string)
    }

    override fun start() {
        return
    }
}