package ead.experience.rota

import ead.experience.repository.DbTemp
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/teste")
class Test {

    @Path("/")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun teste() = "Aquele teste maroto só pra ver se funciona que nem aquele ser maravilhoso da sua mãe"

    @Path("/aluno")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun aluno() = Response.ok().entity(DbTemp.Alunos).build()

    @Path("/professor")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun professor() = Response.ok().entity(DbTemp.Professores).build()

}