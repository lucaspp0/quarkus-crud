package ead.experience.rota

import ead.experience.domain.Aluno
import ead.experience.domain.Professor
import ead.experience.dto.MensagemDto
import ead.experience.dto.autentificacao.*
import ead.experience.repository.DbTemp
import ead.experience.utils.FileUtil
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import java.util.*


import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/autentificacao")
class AutentificacaoRt {

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun Login(loginDto: LoginDto): Response {
        val alunoEncontrado: Optional<Aluno> = DbTemp.Alunos
                .stream()
                .filter { aluno -> (aluno.email == loginDto.login || aluno.login == loginDto.login) && aluno.senha == loginDto.senha }
                .findFirst()

        if (alunoEncontrado.isPresent) {
            return Response.status(200)
                .entity(
                    AlunoRt().AlunoToAlunoSend(alunoEncontrado.get())
                ).build()
        } else {
            val alunoEncontrado: Optional<Professor> = DbTemp.Professores
                    .stream()
                    .filter { aluno -> (aluno.email == loginDto.login || aluno.login == loginDto.login) && aluno.senha == loginDto.senha }
                    .findFirst()
            return if (alunoEncontrado.isPresent)
                Response.status(200).entity(ProfessorToProfessorSend(alunoEncontrado.get())).build()
            else
                Response.status(404).entity(MensagemDto("Usuário não encontrado")).build()
        }

    }

    @Path("/cadastro/professor")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun CadastroProf(@MultipartForm profDto: ProfessorDto): Response {

        val profValido = DbTemp.Professores
                .stream()
                .filter { prof -> prof.login == profDto.login || prof.email == profDto.email }
                .findFirst().isEmpty

        if (profValido) {
            val nextId: Int = DbTemp.Professores.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!
            var fotoUrl: String? = null;
            if (profDto.foto != null)
                fotoUrl = FileUtil.gravarFoto(profDto.foto!!)
            DbTemp.Professores.add(Professor(nextId, profDto.nome, profDto.email, profDto.senha, profDto.login, fotoUrl))
            return Response.status(200).entity(MensagemDto("Professor foi cadastrado com sucesso")).build()
        } else {
            return Response.status(400).entity(MensagemDto("Professor já existe, cadastre email ou login diferente")).build()
        }

    }

    @Path("/cadastro/aluno")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun CadastroAluno(@MultipartForm alunoDto: AlunoDto): Response {
        val alunoValido = DbTemp.Alunos
                .stream()
                .filter { prof -> prof.login == alunoDto.login || prof.email == alunoDto.email }
                .findFirst().isEmpty

        if (alunoValido) {
            val nextId: Int = DbTemp.Alunos.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!

            var fotoUrl: String? = null;
            if (alunoDto.foto != null)
                fotoUrl = FileUtil.gravarFoto(alunoDto.foto!!)

            DbTemp.Alunos.add(Aluno(nextId, alunoDto.nome, alunoDto.email, alunoDto.senha, alunoDto.login, fotoUrl, alunoDto.CH))
            return Response.status(200).entity(MensagemDto("Aluno foi cadastrado com sucesso")).build()
        } else {
            return Response.status(400).entity(MensagemDto("Usuário já existe, cadastre um email ou login diferente")).build()
        }
    }

    fun ProfessorToProfessorSend(professor: Professor): ProfessorSendDto {
        var ProfessorSend = ProfessorSendDto(
                professor.id,
                professor.nome,
                professor.email,
                professor.senha,
                professor.login,
                null
        )
        if(ProfessorSend.foto != null)
            ProfessorSend.foto = "data:image/png;base64," + FileUtil.obterbase64(professor.foto!!)

        return ProfessorSend
    }
}