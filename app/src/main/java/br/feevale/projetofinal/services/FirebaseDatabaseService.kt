package br.feevale.projetofinal.services

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseDatabaseService {

    private val firestoreDB: FirebaseFirestore by lazy{ FirebaseFirestore.getInstance() }
    lateinit var lastTripCreated: DocumentReference

    fun saveNewTrip(data: Map<String, String>){
        lastTripCreated = firestoreDB.collection("trip").document()
        lastTripCreated.set(data)
    }

    fun getLastAddedTripId(): String{
        return lastTripCreated.id
    }
}