package com.moonsu.assignment.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.moonsu.assignment.core.common.di.IoDispatcher
import com.moonsu.assignment.data.model.CharacterEntity
import com.moonsu.assignment.data.remote.RemoteCharacterDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CharacterPagingSource(
    private val remoteDataSource: RemoteCharacterDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        return withContext(ioDispatcher) {
            try {
                val page = params.key ?: STARTING_PAGE_INDEX
                val response = remoteDataSource.getCharacters(page = page)

                val nextKey = if (response.isEmpty()) {
                    null
                } else {
                    // Rick and Morty API는 info.next를 통해 다음 페이지 여부를 알려줌
                    // 하지만 여기서는 간단하게 다음 페이지 번호를 반환
                    // 실제로는 API 응답의 info.next를 파싱하여 null 체크를 할 수 있음
                    page + 1
                }

                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1

                LoadResult.Page(
                    data = response,
                    prevKey = prevKey,
                    nextKey = nextKey,
                )
            } catch (exception: Exception) {
                LoadResult.Error(exception)
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

