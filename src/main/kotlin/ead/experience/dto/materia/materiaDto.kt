package ead.experience.dto.materia

import java.io.File
import javax.ws.rs.FormParam

open class MateriaDto (
        @FormParam("nome")
        var nome: String = "",
        @FormParam("nome")
        var custo: Float = 0F,
        @FormParam("nome")
        var idProfessor: Int = 0,
        @FormParam("foto")
        var foto: File? = null
)