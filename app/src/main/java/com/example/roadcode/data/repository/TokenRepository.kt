package com.example.roadcode.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token_preferences")

@Singleton
class TokenRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TOKEN_KEY = stringPreferencesKey("jwt_token")

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    /* 토큰 저장 */
    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
        Log.d("TOKEN", "토큰 저장됨: $token")
    }

    /* 토큰 1회 조회 */
    suspend fun getTokenOnce(): String? = tokenFlow.first()

    /* Bearer 토큰 바로 반환 (BaseRepository 기능 통합) */
    suspend fun getBearerToken(): String {
        val token = getTokenOnce()
        require(!token.isNullOrBlank()) { "토큰이 없습니다." }
        return "Bearer $token"
    }

    /* 토큰 삭제 */
    suspend fun clearToken() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
        Log.d("TOKEN", "토큰 삭제됨")
    }
}