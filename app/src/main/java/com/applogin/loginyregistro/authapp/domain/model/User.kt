package com.applogin.loginyregistro.authapp.domain.model

data class User(
    //Identificador unico del usuairo
    val id: Int,
    //Nombre del usuairo
    val name: String,
    //Correo electronico del usuairo
    val email: String
)
