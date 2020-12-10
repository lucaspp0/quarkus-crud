package ead.experience.rota

import ead.experience.domain.Aluno
import ead.experience.domain.Materia
import ead.experience.dto.MensagemDto
import ead.experience.dto.autentificacao.AlunoDto
import ead.experience.dto.autentificacao.LoginSendDto
import ead.experience.repository.DbTemp
import ead.experience.utils.FileUtil
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.jboss.resteasy.annotations.jaxrs.PathParam
import java.io.File
import java.nio.file.Files
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

open class AlunoReceiveDto{

    @FormParam("id")
    var id: Int = 0

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

@Path("/aluno")
class AlunoRt {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlunos() : List<LoginSendDto>{
        return DbTemp.Alunos.map { x -> AlunoToAlunoSend(x) }
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun materiaPorAluno(@PathParam id: Int) : List<Materia>{
        val listaMaterias = DbTemp.AlunoMateria.filter { x -> x.aluno!!.id!! == id }.map { x -> x.materia!!.id!! }
        return DbTemp.Materias.filter { x -> listaMaterias.contains(x.id!!) }
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun updateAlunos(@RequestBody alunoReceiveDto : AlunoReceiveDto) : MensagemDto{
        val alunoOptional = DbTemp.Alunos.stream().filter { x -> x.id!! == alunoReceiveDto.id }.findFirst()
        if(alunoOptional.isEmpty){
            return MensagemDto("Aluno não encontrado")
        }else{
            alunoOptional.get().email = alunoReceiveDto.email
            alunoOptional.get().login = alunoReceiveDto.login
            alunoOptional.get().senha = alunoReceiveDto.senha
            alunoOptional.get().nome = alunoReceiveDto.nome
            alunoOptional.get().CH = alunoReceiveDto.CH

            if(alunoOptional.get().foto != null && alunoReceiveDto.foto != null){
                FileUtil.writeFile( Files.readAllBytes(alunoReceiveDto.foto!!.toPath()), alunoOptional.get().foto!! )
            }else if(alunoOptional.get().foto == null && alunoReceiveDto.foto != null){
                alunoOptional.get().foto = FileUtil.gravarFoto(alunoReceiveDto.foto!!)
            }

            return MensagemDto("Aluno atualizado com sucesso")
        }
    }

    fun AlunoToAlunoSend(aluno: Aluno): LoginSendDto {
        var alunoSend = LoginSendDto(
            aluno.id,
            aluno.nome,
            aluno.email,
            aluno.senha,
            aluno.login,
            null,
            if (aluno.CH == null || aluno.CH!! <= 0) 10f else aluno.CH
        )
        if(aluno.foto != null)
            alunoSend.foto = FileUtil.obterbase64(aluno.foto!!)

        return alunoSend
    }

}