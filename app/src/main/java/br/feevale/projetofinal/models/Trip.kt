package br.feevale.projetofinal.models

class Trip(var tripOwner: String, var tripName: String, var tripStartDate: String, var tripEndDate: String, var tripBudget: String) {

    lateinit var tripParts: List<TripPart>

}

class TripPart (var partStartDate: String, var partEndDate: String, var partCityFrom: String, var partCityTo: String, var partCost: String){

    lateinit var partPrepItens: List<PreparationItens>
}
