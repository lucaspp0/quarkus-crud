package ead.experience.domain

import java.util.*

class Aula(
    var id: Int,
    var dataInicio: Date? = null,
    var dataFinal: Date? = null,
    var materia: Materia,
    var url: String? = null,
    var urlSalva: String? = "",
    var professor: Professor,
    var conteudo: String? = ""

)