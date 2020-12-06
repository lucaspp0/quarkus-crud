package ead.experience.rota
import ead.experience.dto.MensagemDto
import ead.experience.dto.autentificacao.AlunoDto
import ead.experience.dto.autentificacao.MateriaDto
import ead.experience.repository.DbTemp
import org.eclipse.microprofile.openapi.annotations.Operation
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/materia")
class MateriaGet {
    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Retorna as matérias")
    fun materias():Response{
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




        return Response.status(200).entity(MensagemDto("Materia foi cadastrada com sucesso")).build()
    }


}



