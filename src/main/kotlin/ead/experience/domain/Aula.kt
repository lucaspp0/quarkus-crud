package ead.experience.domain

import ead.experience.domain.Materia
import java.util.*

class Aula(
    var id: Int? = null,
    var dataInicio: Date? = null,
    var dataFinal: Date? = null,
    var materia: Materia? = null,
    var professor: Professor? = null
)