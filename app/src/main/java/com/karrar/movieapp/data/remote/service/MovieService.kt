package com.karrar.movieapp.data.remote.service

import com.karrar.movieapp.data.remote.response.AddListResponse
import com.karrar.movieapp.data.remote.response.AddMovieDto
import com.karrar.movieapp.data.remote.response.BaseListResponse
import com.karrar.movieapp.data.remote.response.CreatedListDto
import com.karrar.movieapp.data.remote.response.CreditsDto
import com.karrar.movieapp.data.remote.response.DailyTrendingDto
import com.karrar.movieapp.data.remote.response.LogoutResponse
import com.karrar.movieapp.data.remote.response.MovieDto
import com.karrar.movieapp.data.remote.response.MyListsDto
import com.karrar.movieapp.data.remote.response.RatedMoviesDto
import com.karrar.movieapp.data.remote.response.RatedTvShowDto
import com.karrar.movieapp.data.remote.response.SeasonDto
import com.karrar.movieapp.data.remote.response.TVShowsDTO
import com.karrar.movieapp.data.remote.response.account.AccountDto
import com.karrar.movieapp.data.remote.response.actor.ActorDto
import com.karrar.movieapp.data.remote.response.actor.ActorMoviesDto
import com.karrar.movieapp.data.remote.response.actor.ActorProfileResponse
import com.karrar.movieapp.data.remote.response.actor.ActorSocialMediaResponse
import com.karrar.movieapp.data.remote.response.genre.GenreResponse
import com.karrar.movieapp.data.remote.response.login.RequestTokenResponse
import com.karrar.movieapp.data.remote.response.login.SessionResponse
import com.karrar.movieapp.data.remote.response.movie.MovieDetailsDto
import com.karrar.movieapp.data.remote.response.movie.RatingDto
import com.karrar.movieapp.data.remote.response.review.ReviewsDto
import com.karrar.movieapp.data.remote.response.trailerVideosDto.TrailerDto
import com.karrar.movieapp.data.remote.response.tvShow.TvShowDetailsDto
import com.karrar.movieapp.domain.enums.TrendingTimeWindow
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<BaseListResponse<MovieDto>>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(@Query("page") page: Int = 1): Response<BaseListResponse<MovieDto>>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): Response<BaseListResponse<MovieDto>>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(@Query("page") page: Int = 1): Response<BaseListResponse<MovieDto>>

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String = TrendingTimeWindow.DAY.value,
        @Query("page") page: Int = 1
    ): Response<BaseListResponse<MovieDto>>

    @GET("discover/movie")
    suspend fun getMatchListMovie(
        @Query("page") page: Int = 1,
        @Query("WITH_GENRES") genres: List<String>? = null,
        @Query("with_runtime.gte") withRuntimeGte: Int?= null,
        @Query("with_runtime.lte") withRuntimeLte: Int? = null,
        @Query("primary_release_date.gte") primaryReleaseDateGte: String? = null,
        @Query("primary_release_date.lte") primaryReleaseDateLte: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<BaseListResponse<MovieDto>>

    @GET("trending/person/{time_window}")
    suspend fun getTrendingActors(
        @Path("time_window") timeWindow: String = TrendingTimeWindow.DAY.value,
        @Query("page") page: Int = 1
    ): Response<BaseListResponse<ActorDto>>

    @GET("search/person")
    suspend fun searchForActor(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<BaseListResponse<ActorDto>>

    @GET("person/{person_id}/external_ids")
    suspend fun getActorExternalIds(
        @Path("person_id") personId: Int
    ): Response<ActorSocialMediaResponse>

    @GET("person/{person_id}/images")
    suspend fun getActorImages(
        @Path("person_id") personId: Int
    ): Response<ActorProfileResponse>

    @GET("search/movie")
    suspend fun searchForMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<BaseListResponse<MovieDto>>

    @GET("search/tv")
    suspend fun searchForSeries(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<BaseListResponse<TVShowsDTO>>

    @GET("authentication/token/new")
    suspend fun getRequestToken(): Response<RequestTokenResponse>

    @GET("genre/movie/list")
    suspend fun getGenreList(): Response<GenreResponse>

    @JvmSuppressWildcards
    @FormUrlEncoded
    @POST("authentication/token/validate_with_login")
    suspend fun validateRequestTokenWithLogin(@FieldMap body: Map<String, Any>): Response<RequestTokenResponse>

    @FormUrlEncoded
    @POST("authentication/session/new")
    suspend fun createSession(@Field("request_token") requestToken: String): Response<SessionResponse>

    @GET("person/{person_id}")
    suspend fun getActorDetails(@Path("person_id") actorId: Int): Response<ActorDto>

    @GET("person/{person_id}/movie_credits")
    suspend fun getActorMovies(@Path("person_id") actorId: Int): Response<ActorMoviesDto>

    @GET("discover/movie")
    suspend fun getMovieListByGenre(
        @Query("with_genres") genreID: Int,
        @Query("page") page: Int = 1
    ): Response<BaseListResponse<MovieDto>>

    @GET("trending/all/day")
    suspend fun getDailyTrending(): Response<BaseListResponse<DailyTrendingDto>>

    @GET("discover/movie")
    suspend fun getAllMovies(@Query("page") page: Int = 1): Response<BaseListResponse<MovieDto>>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: Int): Response<MovieDetailsDto>


    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(@Path("movie_id") movieId: Int): Response<CreditsDto>


    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovie(@Path("movie_id") movieId: Int): Response<BaseListResponse<MovieDto>>


    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(@Path("movie_id") movieId: Int): Response<BaseListResponse<ReviewsDto>>

    @FormUrlEncoded
    @POST("movie/{movie_id}/rating")
    suspend fun postRating(
        @Path("movie_id") movieId: Int,
        @Field("value") rating: Float,
    ): Response<RatingDto>

    @DELETE("movie/{movie_id}/rating")
    suspend fun deleteRating(
        @Path("movie_id") movieId: Int,
    ): Response<RatingDto>

    @FormUrlEncoded
    @POST("tv/{tv_id}/rating")
    suspend fun postTvShowRating(
        @Path("tv_id") tvShowId: Int,
        @Field("value") rating: Float,
        @Query("session_id") sessionId: String = ""
    ): Response<RatingDto>


    @GET("movie/{movie_id}/videos")
    suspend fun getMovieTrailer(@Path("movie_id") movieId: Int): Response<TrailerDto>


    @GET("account/{account_id}/lists")
    suspend fun getCreatedLists(
        @Path("account_id") accountId: Int = 0,
        @Query("session_id") sessionId: String
    ): Response<BaseListResponse<CreatedListDto>>


    @FormUrlEncoded
    @POST("list/{list_id}/add_item")
    suspend fun addMovieToList(
        @Path("list_id") ListId: Int,
        @Query("session_id") sessionId: String,
        @Field("media_id") movieId: Int
    ): Response<AddMovieDto>


    @GET("list/{list_id}")
    suspend fun getList(@Path("list_id") listId: Int): Response<MyListsDto>

    @GET("tv/on_the_air")
    suspend fun getOnTheAir(@Query("page") page: Int = 1): Response<BaseListResponse<TVShowsDTO>>

    @GET("tv/airing_today")
    suspend fun getAiringToday(@Query("page") page: Int = 1): Response<BaseListResponse<TVShowsDTO>>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShow(@Query("page") page: Int = 1): Response<BaseListResponse<TVShowsDTO>>

    @GET("tv/popular")
    suspend fun getPopularTvShow(@Query("page") page: Int = 1): Response<BaseListResponse<TVShowsDTO>>

    @GET("tv/latest")
    suspend fun getLatestTvShow(): Response<BaseListResponse<TVShowsDTO>>

    @GET("genre/tv/list")
    suspend fun getGenreTvShowList(): Response<GenreResponse>

    @GET("discover/tv")
    suspend fun getTvListByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1
    ): Response<BaseListResponse<TVShowsDTO>>

    @GET("discover/tv")
    suspend fun getAllTvShows(@Query("page") page: Int = 1): Response<BaseListResponse<TVShowsDTO>>

    @GET("account")
    suspend fun getAccountDetails(@Query("session_id") sessionId: String? = ""): Response<AccountDto>

    @DELETE("authentication/session")
    suspend fun logout(@Query("session_id") sessionId: String): Response<LogoutResponse>


    @FormUrlEncoded
    @POST("list")
    suspend fun createList(
        @Query("session_id") sessionId: String,
        @Field("name") name: String,
        @Field("description") description: String = ""
    ): Response<AddListResponse>

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(@Path("tv_id") tvShowId: Int): Response<TvShowDetailsDto>

    @GET("tv/{tv_id}/credits")
    suspend fun getTvShowCast(@Path("tv_id") tvShowId: Int): Response<CreditsDto>

    @GET("tv/{tv_id}/reviews")
    suspend fun getTvShowReviews(@Path("tv_id") tvShowId: Int): Response<BaseListResponse<ReviewsDto>>

    @GET("account/{account_id}/rated/tv")
    suspend fun getRatedTvShow(
        @Path("account_id") listId: Int = 0,
    ): Response<BaseListResponse<RatedTvShowDto>>

    @GET("account/{account_id}/rated/movies")
    suspend fun getRatedMovie(
        @Path("account_id") accountId: Int = 0,
    ): Response<BaseListResponse<RatedMoviesDto>>

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getSeasonDetails(
        @Path("tv_id") tvShowId: Int,
        @Path("season_number") seasonId: Int,
    ): Response<SeasonDto>

    @GET("tv/{tv_id}/videos")
    suspend fun getTvShowTrailer(@Path("tv_id") tvShowId: Int): Response<TrailerDto>

    @DELETE("tv/{tv_id}/rating")
    suspend fun deleteTvShowRating(
        @Path("tv_id") tvId: Int,
    ): Response<RatingDto>

}