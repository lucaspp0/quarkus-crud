package ead.experience.dto.materia

import ead.experience.dto.materia.MateriaDto
import javax.ws.rs.FormParam

open class MateriaReceiveDto(@FormParam("nome") var id: Int ) : MateriaDto()