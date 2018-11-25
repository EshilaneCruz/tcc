package br.feevale.projetofinal.services

import com.google.firebase.firestore.FirebaseFirestore


object FirebaseDatabaseService {

    val firestoreDB: FirebaseFirestore by lazy{ FirebaseFirestore.getInstance() }


}