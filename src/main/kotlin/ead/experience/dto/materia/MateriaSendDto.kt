package ead.experience.dto.materia

import ead.experience.dto.autentificacao.LoginSendDto
import ead.experience.dto.autentificacao.ProfessorSendDto
import java.io.File
import javax.ws.rs.FormParam

open class MateriaSendDto (
        @FormParam("id")
        var id: Int? = null,
        @FormParam("nome")
        var nome: String? = null,
        @FormParam("custo")
        var custo: Float? = 0F,
        @FormParam("professor")
        var professor: LoginSendDto,
        @FormParam("foto")
        var foto: String? = null
)