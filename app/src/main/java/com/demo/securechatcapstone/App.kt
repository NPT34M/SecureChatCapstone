package com.demo.securechatcapstone

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class App : Application() {

    companion object {
        var pNumber: String? = null
        var gNumber: String? = null
        val pubKeyServer = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq9WsIexO4yUl4ZJLwkNQIO0swxe2vsSeGuCPBo5D4oBmLWo3dtSqgzkU3yzdR9lCE8GRxn68eU4vlShWgzyIDo/tXtb/knzIhZV9299Vpt4sszcT92LCLLc/G3FykqfpixFB/DZfPZtJ6y+KT77zPYCFrsLsI68oUB0sPFzikzTIjDZ0aypzZH87S00PUfnFV5l0I9I8FmUmAbKUznuP/hn2Vv+5cNbLYW/BkgJphFl/mSwwJcQPRovTL0kGDJEd2GCrZYAZq/Akck3hThVv+lwiJ5a7VUziso/DS6jZcZTHdxWqYvUpox1mjDphOB5w9KHSnITNVYO/WQ4pv9/NpwIDAQAB"
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseDatabase.getInstance().getReference("/numbers/p")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    pNumber = snapshot.value.toString()
                    Log.d("pNum", "$pNumber")
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        FirebaseDatabase.getInstance().getReference("/numbers/g")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    gNumber = snapshot.value.toString()
                    Log.d("gNum", "$gNumber")
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
}