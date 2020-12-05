package ead.experience.rota
import ead.experience.repository.DbTemp
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
    fun materias():Response{
            return Response.ok().entity(DbTemp.Materias).build()
}
}

@Path("/materia")
class MateriaPost {
    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun materias(): Response {
        return Response.ok().entity(DbTemp.Materias).build()
    }
}



