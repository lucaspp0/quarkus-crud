package ead.experience.rota

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ead.experience.domain.Aula
import ead.experience.dto.MensagemDto
import ead.experience.repository.DbTemp
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.jboss.resteasy.annotations.jaxrs.PathParam
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType


open class AulaDto(
    var idMateria: Int,
    var idProfessor: Int,
    var conteudo: String? = null
)

open class AlunoMateriaDto(
    var idMateria: Int,
    var idAluno: Int
)

open class FiltroAula(
    var idMateria: Int? = null,
    var idAluno: Int? = null,
    var idProfessor: Int? = null,
    var dataFinal: Long? = null,
    var dataInicial: Long? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
open class AtualizarAulaDto {
    var idAula: Int? = null
    var url: String? = null
    var conteudo: String? = null
}


@Path("/aula")
class AulaRt {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Criar uma aula em aberto")
    fun CriarAula(@RequestBody aulaDto: AulaDto) : MensagemDto {
        val materiaOptional = DbTemp.Materias.stream()
            .filter { x -> x.id!! == aulaDto.idMateria }.findFirst()

        if(materiaOptional.isEmpty){
            return MensagemDto("Matéria não encontrada")
        }else{
            val profOptional = DbTemp.Professores.stream().filter { x -> x.id!! == aulaDto.idProfessor }.findFirst()

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
                DbTemp.Aulas.add( Aula(nextId, Date(), null, materiaOptional.get(), null, profOptional.get(),aulaDto.conteudo) )
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
            return MensagemDto("Aula fechada")
        }
    }

    @Path("/aluno/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "pegar aulas online do aluno")
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun pegarAulaOnlineDeAluno(@PathParam id: Int) : List<Aula> {
        val materiasId = DbTemp.AlunoMateria.filter { x -> x.aluno!!.id!! == id }.map { x -> x.id }
        return DbTemp.Aulas.filter { aulas -> materiasId.contains(aulas.materia.id!!) && aulas.dataFinal == null && aulas.dataInicio!!.before(Date()) }
    }


    @Path("/aluno/todas/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "pegar todas aulas salvas do aluno")
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun pegarTodasAulaDeAluno(@PathParam id: Int) : List<Aula> {
        val materiasId = DbTemp.AlunoMateria.filter { x -> x.aluno!!.id!! == id }.map { x -> x.id }
        return DbTemp.Aulas.filter { aulas -> materiasId.contains(aulas.materia.id!!) && aulas.dataFinal != null }
    }


    @Path("/filtro/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "pegar aulas gravadas e filtradas")
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun filtraAulas(@RequestBody filtroAula: FiltroAula) : List<Aula> {
        // var listaMateria = DbTemp.Aulas.filter { x -> x.dataFinal != null }.toMutableList()
        var listaMateria = DbTemp.Aulas
        if(filtroAula.dataFinal != null)
            listaMateria = listaMateria.filter { x -> x.dataFinal != null && x.dataFinal!!.before(Date(filtroAula.dataFinal!!)) }.toMutableList()

        if(filtroAula.dataInicial != null)
            listaMateria = listaMateria.filter { x -> x.dataInicio!!.before(Date(filtroAula.dataInicial!!)) }.toMutableList()

        if(filtroAula.idAluno != null){
            val materiasId = DbTemp.AlunoMateria.filter { x -> x.aluno!!.id!! == filtroAula.idAluno!! }.map { x -> x.id }
            listaMateria = listaMateria.filter { aulas -> materiasId.contains(aulas.materia.id!!)}.toMutableList()
        }

        if(filtroAula.idProfessor != null)
            listaMateria = listaMateria.filter { x -> x.professor.id!! == filtroAula.idProfessor!! }.toMutableList()

        if(filtroAula.idMateria != null)
            listaMateria = listaMateria.filter { x -> x.materia.id!! == filtroAula.idMateria!! }.toMutableList()
        return listaMateria
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun pegarAulas() : List<Aula> {
        return  DbTemp.Aulas
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(description = "Tudo certo meu chapa", responseCode = "200")
    fun AtaulizarAula(@RequestBody atualizarAulaDto: AtualizarAulaDto) : MensagemDto{
        var aulaOptional = DbTemp.Aulas
            .stream()
            .filter { x -> x.id!! == atualizarAulaDto.idAula }.findFirst()
        if(aulaOptional.isEmpty) return  MensagemDto("Aula não encontrada")

        if(atualizarAulaDto.url!=null){ aulaOptional.get().url= atualizarAulaDto.url }

        if(atualizarAulaDto.conteudo!=null){ aulaOptional.get().conteudo= atualizarAulaDto.conteudo }


        return MensagemDto("Aula alterada com sucesso")
    }


}