package com.example.learner.data.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    /**get the state of the user*/
    fun getUserData(): Flow<UserEntity>

    /**insert the user when the app is first started*/
    suspend fun insert(userEntity: UserEntity)

    /**update the state of the user*/
    suspend fun update(userEntity: UserEntity)
}