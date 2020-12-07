package ead.experience.rota

import ead.experience.domain.Aluno
import ead.experience.domain.Professor
import ead.experience.dto.MensagemDto
import ead.experience.dto.autentificacao.*
import ead.experience.repository.DbTemp
import ead.experience.utils.FileUtil
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
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
    @Operation(description = "Autentificação do usuário")
    fun Login(@RequestBody loginDto: LoginDto): LoginSendDto? {
        val alunoEncontrado: Optional<Aluno> = DbTemp.Alunos
                .stream()
                .filter { aluno -> (aluno.email == loginDto.login || aluno.login == loginDto.login) && aluno.senha == loginDto.senha }
                .findFirst()

        if (alunoEncontrado.isPresent) {
            return AlunoRt().AlunoToAlunoSend(alunoEncontrado.get())
        } else {
            val profEncontrado: Optional<Professor> = DbTemp.Professores
                    .stream()
                    .filter { aluno -> (aluno.email == loginDto.login || aluno.login == loginDto.login) && aluno.senha == loginDto.senha }
                    .findFirst()
            return if (profEncontrado.isPresent)
                ProfessorToLoginSend(profEncontrado.get())
            else
                null
        }

    }

    @Path("/cadastro/professor")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Cadastro de professor")
    fun CadastroProf(@MultipartForm profDto: ProfessorDto): MensagemDto {

        var profValido = DbTemp.Professores
                .stream()
                .filter { prof -> prof.login == profDto.login || prof.email == profDto.email }
                .findFirst().isEmpty

        if(profValido){
            profValido = DbTemp.Alunos
                .stream()
                .filter { aluno -> aluno.login == profDto.login || aluno.email == profDto.email }
                .findFirst().isEmpty
        }

        if (profValido) {
            val nextId: Int = DbTemp.Professores.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!
            var fotoUrl: String? = null;
            if (profDto.foto != null)
                fotoUrl = FileUtil.gravarFoto(profDto.foto!!)
            DbTemp.Professores.add(Professor(nextId, profDto.nome, profDto.email, profDto.senha, profDto.login, fotoUrl))
            return MensagemDto("Professor foi cadastrado com sucesso")
        } else {
            return MensagemDto("Professor já existe, cadastre email ou login diferente")
        }

    }

    @Path("/cadastro/aluno")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Cadastro de aluno")
    fun CadastroAluno(@MultipartForm alunoDto: AlunoDto): MensagemDto {

        var alunoValido = DbTemp.Alunos
                .stream()
                .filter { prof -> prof.login == alunoDto.login || prof.email == alunoDto.email }
                .findFirst().isEmpty

        if(alunoValido){
            alunoValido = DbTemp.Professores
                .stream()
                .filter { prof -> prof.login == alunoDto.login || prof.email == alunoDto.email }
                .findFirst().isEmpty
        }

        if (alunoValido) {
            val nextId: Int = DbTemp.Alunos.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!

            var fotoUrl: String? = null;
            if (alunoDto.foto != null)
                fotoUrl = FileUtil.gravarFoto(alunoDto.foto!!)

            DbTemp.Alunos.add(Aluno(nextId, alunoDto.nome, alunoDto.email, alunoDto.senha, alunoDto.login, fotoUrl, alunoDto.CH))
            return MensagemDto("Aluno foi cadastrado com sucesso")
        } else {
            return MensagemDto("Usuário já existe, cadastre um email ou login diferente")
        }
    }

    fun ProfessorToLoginSend(professor: Professor): LoginSendDto {
        val loginSendDto = LoginSendDto(
                professor.id,
                professor.nome,
                professor.email!!,
                professor.senha!!,
                professor.login!!,
                null,
                null
        )
        if(loginSendDto.foto != null)
            loginSendDto.foto = "data:image/png;base64," + FileUtil.obterbase64(professor.foto!!)

        return loginSendDto
    }
}