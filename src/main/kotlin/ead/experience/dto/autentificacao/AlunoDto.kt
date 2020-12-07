package ead.experience.dto.autentificacao

import org.eclipse.microprofile.openapi.annotations.media.Schema
import java.io.File
import javax.ws.rs.FormParam

open class AlunoDto{

    @FormParam("nome")
    var nome: String = ""

    @FormParam("email")
    lateinit var email: String

    @FormParam("senha")
    lateinit var senha: String

    @FormParam("login")
    lateinit var login: String

    @FormParam("foto")
    var foto: File? = null

    @FormParam("CH")
    var CH: Float? = null

}