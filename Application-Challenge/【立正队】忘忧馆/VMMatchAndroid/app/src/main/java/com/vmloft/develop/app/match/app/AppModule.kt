package com.vmloft.develop.app.match.app

import com.vmloft.develop.app.match.request.repository.*
import com.vmloft.develop.app.match.ui.feedback.FeedbackViewModel
import com.vmloft.develop.library.common.ui.display.DisplayViewModel
import com.vmloft.develop.app.match.ui.main.explore.ExploreViewModel
import com.vmloft.develop.app.match.ui.main.MainViewModel
import com.vmloft.develop.app.match.ui.main.home.MatchViewModel
import com.vmloft.develop.app.match.ui.main.mine.info.InfoViewModel
import com.vmloft.develop.app.match.ui.post.PostViewModel
import com.vmloft.develop.app.match.ui.sign.SignViewModel
import com.vmloft.develop.app.match.ui.user.UserInfoViewModel
import com.vmloft.develop.app.match.ui.room.RoomViewModel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Create by lzan13 on 2020/6/4 18:02
 */
val viewModelModule = module {
    viewModel { DisplayViewModel() }
    viewModel { ExploreViewModel(get(), get()) }
    viewModel { FeedbackViewModel(get()) }
    viewModel { InfoViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MatchViewModel(get()) }
    viewModel { PostViewModel(get(), get(), get()) }
    viewModel { RoomViewModel(get()) }
    viewModel { SignViewModel(get()) }
    viewModel { UserInfoViewModel(get(), get()) }

}

val repositoryModule = module {
//    single { APIRequest.getAPI(APIService::class.java, CConstants.baseHost()) }
    single { CoroutinesDispatcherProvider() }
    single { CommonRepository() }
    single { FollowRepository() }
    single { InfoRepository() }
    single { LikeRepository() }
    single { MainRepository() }
    single { MatchRepository() }
    single { PostRepository() }
    single { RoomRepository() }
    single { SignRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)