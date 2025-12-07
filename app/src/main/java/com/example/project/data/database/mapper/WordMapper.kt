package com.example.project.data.database.mapper

import com.example.project.data.database.entity.WordEntity
import com.example.project.domain.Word

object WordMapper {
    fun WordEntity.toWord(): Word {
        return Word(
            word = this.word,
            count = 1
        )
    }

    fun Word.toEntity(): WordEntity {
        return WordEntity(
            word = this.word
        )
    }
}