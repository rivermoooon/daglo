package com.moonsu.assignment.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moonsu.assignment.data.DataErrorMapper.toAppError
import com.moonsu.assignment.data.model.CharacterEntity
import com.moonsu.assignment.data.remote.RemoteCharacterDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CharacterPagingSource(
    private val remoteDataSource: RemoteCharacterDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        return withContext(ioDispatcher) {
            try {
                val page = params.key ?: STARTING_PAGE_INDEX
                val response = remoteDataSource.getCharacters(page)
                val nextKey = if (response.info.next != null) {
                    page + 1
                } else {
                    null
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1

                LoadResult.Page(
                    data = response.results,
                    prevKey = prevKey,
                    nextKey = nextKey,
                )
            } catch (e: Exception) {
                val appError = e.toAppError()
                Log.e(
                    "CharacterPagingSource",
                    "[load] Page ${params.key ?: STARTING_PAGE_INDEX} failed: ${appError.message}",
                    appError.cause,
                )
                LoadResult.Error(appError)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}
