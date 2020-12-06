package ead.experience.rota

import ead.experience.domain.AlunoMateria
import ead.experience.domain.Aula
import ead.experience.dto.MensagemDto
import ead.experience.repository.DbTemp
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.resteasy.annotations.jaxrs.PathParam
import java.util.*
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


open class AulaDto(
    var materia_id: Int,
    var professor_id: Int
)

@Path("/aula")
class AulaRt {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Criar uma aula em aberto")
    @APIResponse(responseCode = "400", description = "Matéria ou Professor não encontrados ou  aula já aberta")
    fun CriarAula(aulaDto: AulaDto) : Response {
        val materiaOptional = DbTemp.Materias.stream()
            .filter { x -> x.id!! == aulaDto.materia_id }.findFirst()

        if(materiaOptional.isEmpty){
            return Response.status(400).entity(MensagemDto("Matéria não encontrada")).build()
        }else{
            val profOptional = DbTemp.Professores.stream().filter { x -> x.id!! == aulaDto.professor_id }.findFirst()

            if(profOptional.isEmpty){
                return Response.status(400).entity(MensagemDto("Professor não encontrada")).build()
            }
            else{
                val nextId = DbTemp.Aulas.maxBy { x -> x.id!! }!!.id!!.plus(1)
                if( DbTemp.Aulas
                        .any { x -> x.materia!!.id == materiaOptional.get().id!! &&
                                x.professor!!.id!! == profOptional.get().id!! &&
                                x.dataFinal != null
                        } ){
                    return Response.status(400).entity(MensagemDto("Aula já está iniciada")).build()
                }
                DbTemp.Aulas.add( Aula(nextId, Date(), null, materiaOptional.get(), profOptional.get()) )
                return Response.ok().entity(MensagemDto("Aula iniciada")).build()
            }

        }
    }

    @Path("/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Fechar uma aula aberta")
    fun fecharAula(@PathParam id: Int) : Response{
        val aulaOptional = DbTemp.Aulas.stream()
            .filter { x -> x.dataFinal == null && x.id!! == id }.findFirst()
        if(aulaOptional.isEmpty) {
            return Response.status(400).entity(MensagemDto("Aula não encontrada")).build()
        }else{
            aulaOptional.get().dataFinal = Date()
            return Response.status(400).entity(MensagemDto("Aula não encontrada")).build()
        }
    }


    open class AlunoMateria(
        var materia_id: Int,
        var aluno_id: Int
    )

    @Path("/aluno")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Vincular aluno com materia")
    fun vincularAluno(vincularAlunoMateria: AlunoMateria) : Response{
        val alunoOptional = DbTemp.Alunos.stream().filter { x -> x.id!! == vincularAlunoMateria.aluno_id }.findFirst()
        val MateriaOptional = DbTemp.Materias.stream().filter { x -> x.id!! == vincularAlunoMateria.materia_id }.findFirst()

        val correlacaoValida = DbTemp.AlunoMateria
            .stream()
            .filter {  x -> x.aluno!!.id!! == vincularAlunoMateria.materia_id && x.materia!!.id!! == vincularAlunoMateria.aluno_id }
            .findFirst().isEmpty

        if(correlacaoValida){

            if(alunoOptional.isEmpty || MateriaOptional.isEmpty){
                return Response.status(400).entity(MensagemDto("Relação já existente")).build()
            }else{
                val nextId: Int = DbTemp.AlunoMateria.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!
                DbTemp.AlunoMateria.add( ead.experience.domain.AlunoMateria(nextId, MateriaOptional.get(), alunoOptional.get())   )
                return Response.status(202).entity(MensagemDto("Aluno vinculado com sucesso")).build()
            }

        }else{
            return Response.status(400).entity(MensagemDto("Relação já existente")).build()
        }

    }
}