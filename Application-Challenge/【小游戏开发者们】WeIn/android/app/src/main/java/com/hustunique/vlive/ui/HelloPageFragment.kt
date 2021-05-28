package com.hustunique.vlive.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hustunique.vlive.databinding.FragmentHelloPageBinding
import com.hustunique.vlive.util.UserInfoManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwΩo@qq.com
 *    date   : 2021/5/23
 */
class HelloPageFragment : Fragment() {

    private val binding by lazy {
        FragmentHelloPageBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenCreated() {
            withContext(Dispatchers.IO) {
                delay(500)
            }
            findNavController().apply {
                lifecycleScope.launchWhenCreated {
                    withContext(Dispatchers.IO) {
                        UserInfoManager.blockRefreshUid()
                    }
                    if (UserInfoManager.uid.isEmpty()) {
                        navigate(
                            HelloPageFragmentDirections.actionHelloPageFragmentToWelcomeFragment(),
                            FragmentNavigatorExtras(
                                binding.actorImg to "welcome_actor_img"
                            )
                        )
                    } else {
                        navigate(
                            HelloPageFragmentDirections.actionHelloPageFragmentToChannelListFragment(),
                            FragmentNavigatorExtras(
                                binding.actorImg to "channel_list_actor_img"
                            )
                        )
                    }
                }
            }
        }
    }
}