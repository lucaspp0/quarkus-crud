package ead.experience.dto.materia

import ead.experience.dto.materia.MateriaDto
import javax.ws.rs.FormParam

open class MateriaReceiveDto(@FormParam("id") var id: Int ) : MateriaDto()