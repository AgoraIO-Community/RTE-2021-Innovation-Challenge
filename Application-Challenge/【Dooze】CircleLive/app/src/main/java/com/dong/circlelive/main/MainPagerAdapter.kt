package com.dong.circlelive.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dong.circlelive.base.lazyFast
import com.dong.circlelive.im.ConversationListFragment
import com.dong.circlelive.live.LivesFragment
import com.dong.circlelive.posts.PostsFragment

/**
 * Create by dooze on 2021/5/14  11:16 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
@Suppress("DEPRECATION")
class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    val livesFragment by lazyFast { LivesFragment() }

    val conversationListFragment by lazyFast { ConversationListFragment() }

    val postsFragment by lazyFast { PostsFragment() }

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> livesFragment
            2 -> conversationListFragment
            else -> postsFragment
        }
    }
}