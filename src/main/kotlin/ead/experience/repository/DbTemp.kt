package ead.experience.repository

import ead.experience.domain.*

object DbTemp {

    val Alunos =
            mutableListOf<Aluno>(
                    Aluno(1, "Lucas", "leite", "123", "lucas", null, 10f)
            )

    val Professores =
            mutableListOf<Professor>(
                    Professor(1, "pedro", "joão", "123", "pedro", null)
            )

    val Materias =
            mutableListOf<Materia>(
                    Materia(1, "Português", 1200f, Professores[0])
            )

    val AlunoMateria =
            mutableListOf<AlunoMateria>(
                    AlunoMateria(1, Materias[0], Alunos[0], 0f)
            )
}