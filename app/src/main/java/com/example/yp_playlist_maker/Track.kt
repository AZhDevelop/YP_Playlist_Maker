package com.example.yp_playlist_maker

data class Track(
    var trackName: String,
    var artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) {
    companion object {
        const val TRACK_NAME: String = "track_name"
        const val ARTIST_NAME: String = "artist_name"
        const val TRACK_TIME_MILLIS = "track_time_millis"
        const val ARTWORK_URL_500: String = "artwork_url_500"
        const val COLLECTION_NAME: String = "collection_name"
        const val RELEASE_DATE: String = "release_date"
        const val PRIMARY_GENRE_NAME: String = "primary_genre_name"
        const val COUNTRY: String = "country"
    }
}
