package br.feevale.projetofinal.models

class TripPart (var partStartDate: String, var partEndDate: String, var destination: String, var partCost: String){

    lateinit var partPrepItens: List<PreparationItem>
}
