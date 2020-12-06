package ead.experience.rota

import ead.experience.domain.Materia
import ead.experience.domain.Professor
import ead.experience.dto.MensagemDto
import ead.experience.dto.materia.MateriaDto
import ead.experience.dto.materia.MateriaReceiveDto
import ead.experience.repository.DbTemp
import org.eclipse.microprofile.openapi.annotations.Operation
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/materia")
class MateriaGet {
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Retorna as matérias")
    fun materias(): Response {
        return Response.ok().entity(DbTemp.Materias).build()
    }
}

@Path("/materia")
class MateriaPost {
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Criar a matéria")
    fun materias(@MultipartForm materiaDto: MateriaDto): Response {
        val nextId: Int = DbTemp.Materias.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!

        val professorEncontrado: Optional<Professor> = DbTemp.Professores
                .stream()
                .filter { professor -> (professor.id == materiaDto.idProfessor) }
                .findFirst()


        if (professorEncontrado.isPresent()) {
            DbTemp.Materias.add(Materia(nextId, materiaDto.nome, materiaDto.custo, professorEncontrado.get()))
            return Response.status(200).entity(MensagemDto("Materia foi cadastrada com sucesso")).build()

        } else {
            return Response.status(400).entity(MensagemDto("Professor não encontrado")).build()
        }
    }


}
@Path("/materia")
class MateriaPut {
    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Atualiza a matéria")
    fun materias(materiaReceiveDto: MateriaReceiveDto): Response {
        val materiaCara = DbTemp.Materias.stream().filter { x -> x.id!! == materiaReceiveDto.id }.findFirst()
        if(materiaCara.isEmpty){
            return Response.status(404).entity(MensagemDto("Materia não encontrada")).build()
        }else{

        val professorEncontrado: Optional<Professor> = DbTemp.Professores
                .stream()
                .filter { professor -> (professor.id == materiaReceiveDto.idProfessor) }
                .findFirst()
            if (professorEncontrado.isEmpty()) {
                return Response.status(400).entity(MensagemDto("ID do professor não encontrado")).build()
            }

            materiaCara.get().nome = materiaReceiveDto.nome;
            materiaCara.get().custo = materiaReceiveDto.custo;
            materiaCara.get().professor = professorEncontrado.get()

            return Response.status(200).entity(MensagemDto("Materia atualizada com sucesso")).build()
        }


    }
}




