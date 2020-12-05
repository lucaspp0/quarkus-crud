package ead.experience.dto

import org.jboss.resteasy.annotations.providers.multipart.PartType
import java.io.InputStream
import javax.ws.rs.FormParam
import javax.ws.rs.core.MediaType

class MultipartBody {
    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    var file: InputStream? = null

    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    var fileName: String? = null
}