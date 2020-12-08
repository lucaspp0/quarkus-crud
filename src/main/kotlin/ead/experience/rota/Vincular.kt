package ead.experience.rota

import ead.experience.dto.MensagemDto
import ead.experience.repository.DbTemp
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/")
class Vincular {

    @Path("/vincular/aluno-materia")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Vincular aluno com materia")
    fun vincularAluno(@RequestBody vincularAlunoMateria: AlunoMateriaDto) : MensagemDto {
        val alunoOptional = DbTemp.Alunos.stream().filter { x -> x.id!! == vincularAlunoMateria.idAluno }.findFirst()
        val MateriaOptional = DbTemp.Materias.stream().filter { x -> x.id!! == vincularAlunoMateria.idMateria }.findFirst()

        val correlacaoValida = DbTemp.AlunoMateria
            .stream()
            .filter {  x -> x.aluno!!.id!! == vincularAlunoMateria.idAluno && x.materia!!.id!! == vincularAlunoMateria.idMateria }
            .findFirst().isEmpty

        if(correlacaoValida){

            if(alunoOptional.isEmpty || MateriaOptional.isEmpty){
                return MensagemDto("Relação já existente")
            }else{
                val nextId: Int = DbTemp.AlunoMateria.maxBy { x -> x.id!! }?.id?.or(0)?.plus(1)!!
                DbTemp.AlunoMateria.add( ead.experience.domain.AlunoMateria(nextId, MateriaOptional.get(), alunoOptional.get())   )
                return MensagemDto("Aluno vinculado com sucesso")
            }
        }else{
            return MensagemDto("Relação já existente")
        }

    }

}