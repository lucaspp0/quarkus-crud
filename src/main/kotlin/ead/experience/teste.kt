package ead.experience
import ead.experience.utils.FileUtil
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import java.io.File
import javax.ws.rs.*
import javax.ws.rs.Consumes

import javax.ws.rs.POST


@Path("/teste")
class Teste {

    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun fileUpload(@MultipartForm resource: Resource): String? {
        if(resource.file != null){
            return try {
                val FullPath = FileUtil.gravarFoto(resource.file!!)
                "data:image/png;base64, " + FileUtil.obterbase64(FullPath)
            }catch (ex: Exception){
                "erro"
            }
        }
        return "nenhum Arquivo passado como parametro"
    }

}

open class Resource{
    @FormParam("file")
    var file: File? = null

    @FormParam("nome")
    var name: String? = null
}