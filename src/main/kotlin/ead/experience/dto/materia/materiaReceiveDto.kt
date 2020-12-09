package ead.experience.dto.materia

import ead.experience.dto.materia.MateriaDto
import java.io.File
import javax.ws.rs.FormParam

open class MateriaReceiveDto () {
    @FormParam("id")
    var id: Int = 0
    @FormParam("nome")
    var nome: String = ""
    @FormParam("custo")
    var custo: Float = 0F
    @FormParam("idProfessor")
    var idProfessor: Int = 0
    @FormParam("foto")
    var foto: File? = null

}