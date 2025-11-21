package com.example.project.creator

import com.example.project.data.dto.TrackDto

class Storage {
    private val listTracks = listOf(
        TrackDto(
            id = 1,
            trackName = "Владивосток 2000",
            artistName = "Мумий Троль",
            trackTimeMillis = 158000
        ),
        TrackDto(
            id = 2,
            trackName = "Группа крови",
            artistName = "Кино",
            trackTimeMillis = 283000
        ),
        TrackDto(
            id = 3,
            trackName = "Не смотри назад",
            artistName = "Ария",
            trackTimeMillis = 312000
        ),
        TrackDto(
            id = 4,
            trackName = "Звезда по имени Солнце",
            artistName = "Кино",
            trackTimeMillis = 225000
        ),
        TrackDto(
            id = 5,
            trackName = "Лондон",
            artistName = "Аквариум",
            trackTimeMillis = 272000
        ),
        TrackDto(
            id = 6,
            trackName = "На заре",
            artistName = "Альянс",
            trackTimeMillis = 230000
        ),
        TrackDto(
            id = 7,
            trackName = "Перемен",
            artistName = "Кино",
            trackTimeMillis = 296000
        ),
        TrackDto(
            id = 8,
            trackName = "Розовый фламинго",
            artistName = "Сплин",
            trackTimeMillis = 195000
        ),
        TrackDto(
            id = 9,
            trackName = "Танцевать",
            artistName = "Мельница",
            trackTimeMillis = 222000
        ),
        TrackDto(
            id = 10,
            trackName = "Чёрный бумер",
            artistName = "Серега",
            trackTimeMillis = 241000
        )
    )

    fun search(request: String): List<TrackDto> {
        if (request.isBlank()) return emptyList()

        val lowerCaseRequest = request.lowercase()

        val result = listTracks.filter { track ->
            track.trackName.lowercase().contains(lowerCaseRequest) ||
                    track.artistName.lowercase().contains(lowerCaseRequest)
        }
        return result
    }

    fun getTrackById(id: Long): TrackDto? {
        return listTracks.firstOrNull { it.id == id }
    }
}