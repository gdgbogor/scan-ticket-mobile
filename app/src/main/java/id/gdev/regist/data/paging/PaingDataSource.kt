package id.gdev.regist.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import id.gdev.regist.data.source.remote.collection.ParticipantCollection
import id.gdev.regist.data.source.remote.collection.toParticipant
import id.gdev.regist.domain.model.Participant
import kotlinx.coroutines.tasks.await

class ParticipantPagingSource(
    private val queryProductsByName: Query
) : PagingSource<QuerySnapshot, Participant>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Participant>): QuerySnapshot? =
        null

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Participant> =
        try {
            val currentPage = params.key ?: queryProductsByName.get().await()
            val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
            val nextPage = queryProductsByName.startAfter(lastVisibleProduct).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(ParticipantCollection::class.java)
                    .map { it.toParticipant() },
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}