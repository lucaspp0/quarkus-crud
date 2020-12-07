package ead.experience.rota

import ead.experience.domain.AlunoMateria
import ead.experience.domain.Aula
import ead.experience.dto.MensagemDto
import ead.experience.repository.DbTemp
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
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
import kotlin.reflect.typeOf


open class AulaDto(
    var materia_id: Int,
    var professor_id: Int
)

open class AlunoMateriaDto(
    var materia_id: Int,
    var aluno_id: Int
)

@Path("/aula")
class AulaRt {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Criar uma aula em aberto")
    fun CriarAula(@RequestBody aulaDto: AulaDto) : MensagemDto {
        val materiaOptional = DbTemp.Materias.stream()
            .filter { x -> x.id!! == aulaDto.materia_id }.findFirst()

        if(materiaOptional.isEmpty){
            return MensagemDto("Matéria não encontrada")
        }else{
            val profOptional = DbTemp.Professores.stream().filter { x -> x.id!! == aulaDto.professor_id }.findFirst()

            if(profOptional.isEmpty){
                return MensagemDto("Professor não encontrada")
            }
            else{
                val nextId = DbTemp.Aulas.maxBy { x -> x.id }!!.id.plus(1)
                if( DbTemp.Aulas
                        .any { x -> x.materia.id == materiaOptional.get().id!! &&
                                x.professor.id!! == profOptional.get().id!! &&
                                x.dataFinal != null
                        } ){
                    return MensagemDto("Aula já está iniciada")
                }
                DbTemp.Aulas.add( Aula(nextId, Date(), null, materiaOptional.get(), profOptional.get()) )
                return MensagemDto("Aula iniciada")
            }

        }
    }

    @Path("/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Fechar uma aula aberta")
    fun fecharAula(@PathParam id: Int) : MensagemDto{
        val aulaOptional = DbTemp.Aulas.stream()
            .filter { x -> x.dataFinal == null && x.id == id }.findFirst()
        if(aulaOptional.isEmpty) {
            return MensagemDto("Aula não encontrada")
        }else{
            aulaOptional.get().dataFinal = Date()
            return MensagemDto("Aula não encontrada")
        }
    }


    @Path("/aluno/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "pegar aulas online do aluno")
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun pegarAulaDealuno(@PathParam id: Int) : List<Aula> {
        val materiasId = DbTemp.AlunoMateria.filter { x -> x.aluno!!.id!! == id }.map { x -> x.id }
        return DbTemp.Aulas.filter { aulas -> materiasId.contains(aulas.materia.id!!) && aulas.dataFinal == null && aulas.dataInicio!!.after(Date()) }
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun pegarAulas() : List<Aula> {
        return  DbTemp.Aulas
    }



}