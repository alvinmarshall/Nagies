package com.cheise_proj.data.repository.user

import com.cheise_proj.data.mapper.user.UserDataEntityMapper
import com.cheise_proj.data.source.LocalSource
import com.cheise_proj.data.source.RemoteSource
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import utils.TestUserGenerator

@RunWith(JUnit4::class)
class UserRepositoryImplTest {
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    private lateinit var userDataEntityMapper: UserDataEntityMapper

    @Mock
    private lateinit var remoteSource: RemoteSource
    @Mock
    private lateinit var localSource: LocalSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        userDataEntityMapper = UserDataEntityMapper()

        userRepositoryImpl = UserRepositoryImpl(remoteSource, localSource, userDataEntityMapper)
    }

    @Test
    fun `Authenticate user  success`() {
        val errorMsg = "user not found exception"
        val user = TestUserGenerator.user()
        Mockito.`when`(localSource.getUser(user.username, user.password))
            .thenReturn(Single.error(Throwable(errorMsg)))

        Mockito.`when`(remoteSource.authenticateUser(user.username, user.password, user.role))
            .thenReturn(Observable.just(user))

        with(user) {
            userRepositoryImpl.authenticateUser(username, password, user.role).test()
                .assertSubscribed()
                .assertValueCount(1)
                .assertValue {
                    it == userDataEntityMapper.dataToEntity(user)
                }
                .assertComplete()
            Mockito.verify(localSource, times(1))
                .getUser(username, password)

            Mockito.verify(remoteSource, times(1))
                .authenticateUser(username, password, role)
        }
    }


}