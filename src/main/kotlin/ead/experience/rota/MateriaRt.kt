package ead.experience.rota

import ead.experience.domain.Materia
import ead.experience.domain.Professor
import ead.experience.dto.MensagemDto
import ead.experience.dto.materia.MateriaDto
import ead.experience.dto.materia.MateriaReceiveDto
import ead.experience.dto.materia.MateriaSendDto
import ead.experience.repository.DbTemp
import ead.experience.utils.FileUtil
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/materia")
class MateriaRt {

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Retorna as matérias")
    fun materias(): List<MateriaSendDto> {
        return DbTemp.Materias.map { x -> MateriaToMateriaSend(x) }
    }

    @Path("/")
    @POST
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Criar a matéria")
    fun materias(@MultipartForm materiaDto: MateriaDto): MensagemDto {
        val nextId: Int = DbTemp.Materias.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!

        val professorEncontrado: Optional<Professor> = DbTemp.Professores
            .stream()
            .filter { professor -> (professor.id == materiaDto.idProfessor) }
            .findFirst()

        var filename: String? = null;
        if (materiaDto.foto != null)
            filename = FileUtil.gravarFoto(materiaDto.foto!!)

        if (professorEncontrado.isPresent()) {
            DbTemp.Materias.add(Materia(nextId, materiaDto.nome, materiaDto.custo, filename, professorEncontrado.get()))
            return MensagemDto("Materia foi cadastrada com sucesso")
        } else {
            return MensagemDto("Professor não encontrado")
        }
    }

    @Path("/")
    @PUT
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Atualiza a matéria")
    fun materias(@MultipartForm materiaReceiveDto: MateriaReceiveDto): MensagemDto {
        val materiaCara = DbTemp.Materias.stream().filter { x -> x.id!! == materiaReceiveDto.id }.findFirst()
        if(materiaCara.isEmpty){
            return MensagemDto("Materia não encontrada")
        }else{

            val professorEncontrado: Optional<Professor> = DbTemp.Professores
                .stream()
                .filter { professor -> (professor.id == materiaReceiveDto.idProfessor) }
                .findFirst()
            if (professorEncontrado.isEmpty) {
                return MensagemDto("ID do professor não encontrado")
            }

            var filename: String? = null;
            if (materiaReceiveDto.foto != null)
                filename = FileUtil.gravarFoto(materiaReceiveDto.foto!!)

            materiaCara.get().nome = materiaReceiveDto.nome;
            materiaCara.get().custo = materiaReceiveDto.custo;
            materiaCara.get().professor = professorEncontrado.get()

            return MensagemDto("Materia atualizada com sucesso")
        }

    }



    fun MateriaToMateriaSend(materia: Materia): MateriaSendDto{
        var materiaSend = MateriaSendDto(
            materia.id!!,
            materia.nome,
            materia.custo,
            AutentificacaoRt().ProfessorToLoginSend(materia.professor!!),
            null
        )

        if(materia.foto != null)
            materiaSend.foto = "data:image/png;base64," + FileUtil.obterbase64(materia.foto!!)

        return materiaSend
    }

}
