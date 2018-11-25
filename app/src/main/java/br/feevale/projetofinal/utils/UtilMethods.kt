package br.feevale.projetofinal.utils

class UtilMethods {

    companion object {
        val month = mapOf(
                "1" to "JAN",
                "2" to "FEB",
                "3" to "MAR",
                "4" to "APR",
                "5" to "MAY",
                "6" to "JUN",
                "7" to "JUL",
                "8" to "AUG",
                "9" to "SEP",
                "10" to "OCT",
                "11" to "NOV",
                "12" to "DEC")
        @JvmStatic
        fun formatDate(notFormatedDate: String): String{
            val dayValue = notFormatedDate.split("-")[0]
            val monthValue = notFormatedDate.split("-")[1]
            val yearValue = notFormatedDate.split("-")[2]

            return "${month[monthValue]} $dayValue ($yearValue)"
        }
    }


}