package com.example.learner.data.user

import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao): UserRepository {
    override fun getUserData(): Flow<UserEntity> {
        return userDao.getUserData()
    }

    override suspend fun insert(userEntity: UserEntity) {
        userDao.insert(userEntity)
    }

    override suspend fun update(userEntity: UserEntity) {
        userDao.update(userEntity)
    }

    override suspend fun reset() = userDao.reset()
}