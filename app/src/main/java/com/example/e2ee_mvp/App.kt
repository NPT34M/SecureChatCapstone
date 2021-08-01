package com.example.e2ee_mvp

import android.app.Application
import android.content.Context
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