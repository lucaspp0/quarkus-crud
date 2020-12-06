package ead.experience.rota

import ead.experience.domain.Aluno
import ead.experience.dto.MensagemDto
import ead.experience.dto.autentificacao.AlunoDto
import ead.experience.dto.autentificacao.AlunoSendDto
import ead.experience.repository.DbTemp
import ead.experience.utils.FileUtil
import org.jboss.resteasy.annotations.jaxrs.PathParam
import java.nio.file.Files
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

open class AlunoReceiveDto(var id: Int ) : AlunoDto()

@Path("/aluno")
class AlunoRt {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getAlunos() : Response{
        return Response.ok().entity(
            DbTemp.Alunos.stream().map { x -> AlunoToAlunoSend(x) }
        ).build()
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun materiaPorAluno(@PathParam id: Int) : Response{
        val listaMaterias = DbTemp.AlunoMateria.filter { x -> x.aluno!!.id!! == id }.map { x -> x.materia!!.id!! }
        return Response.status(200).entity(
            DbTemp.Materias.filter { x -> listaMaterias.contains(x.id!!) }
        ).build()
    }


    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    fun updateAlunos(alunoReceiveDto : AlunoReceiveDto) : Response{
        val alunoOptional = DbTemp.Alunos.stream().filter { x -> x.id!! == alunoReceiveDto.id }.findFirst()
        if(alunoOptional.isEmpty){
            return Response.status(404).entity(MensagemDto("Aluno não encontrado")).build()
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

            return Response.status(200).entity(MensagemDto("Aluno atualizado com sucesso")).build()
        }
    }

    fun AlunoToAlunoSend(aluno: Aluno): AlunoSendDto {
        var alunoSend = AlunoSendDto(
            aluno.id,
            aluno.nome,
            aluno.email,
            aluno.senha,
            aluno.login,
            null,
            if (aluno.CH != null || aluno.CH!! <= 0 ) 10f else aluno.CH
        )
        if(aluno.foto != null)
            alunoSend.foto = FileUtil.obterbase64(aluno.foto!!)

        return alunoSend
    }

}