package br.com.asoncsts.multi.gymtrack.data.user.remote

import br.com.asoncsts.multi.gymtrack.data._utils.Response
import br.com.asoncsts.multi.gymtrack.data.user.api.ExerciseExecutionApi
import br.com.asoncsts.multi.gymtrack.data.user.remote.model.ExerciseExecutionSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface ExerciseExecutionRemote {

    class Impl(
        private val api: ExerciseExecutionApi,
        private val client: HttpClient
    ) : ExerciseExecutionRemote {

        override suspend fun getExerciseExecution(
            id: String
        ): Response<ExerciseExecutionSource> {
            return client.get(api.exerciseExecution(id))
                .body()
        }

        override suspend fun getExerciseExecutionsGet(
            ids: List<String>
        ): Response<List<ExerciseExecutionSource>> {
            return client.get(api.exerciseExecutionsGet(ids))
                .body()
        }

        override suspend fun getExerciseExecutionsPost(
            ids: List<String>
        ): Response<List<ExerciseExecutionSource>> {
            return client.post(api.exerciseExecutionsPost()) {
                contentType(ContentType.Application.Json)
                setBody(ids)
            }.body()
        }

    }

    suspend fun getExerciseExecution(
        id: String
    ): Response<ExerciseExecutionSource>

    suspend fun getExerciseExecutionsGet(
        ids: List<String>
    ): Response<List<ExerciseExecutionSource>>

    suspend fun getExerciseExecutionsPost(
        ids: List<String>
    ): Response<List<ExerciseExecutionSource>>

}
