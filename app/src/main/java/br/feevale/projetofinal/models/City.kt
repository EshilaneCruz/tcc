package br.feevale.projetofinal.models

class City (
        val cityId: String,
        val capital: Boolean,
        val country: String,
        val image: String,
        val name: String,
        val population: String,
        val state: String,
        val touristSites: List<String>
)