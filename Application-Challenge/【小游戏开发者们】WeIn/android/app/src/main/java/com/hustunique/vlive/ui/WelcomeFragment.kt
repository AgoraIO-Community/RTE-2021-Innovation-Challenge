package com.hustunique.vlive.ui

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hustunique.vlive.R
import com.hustunique.vlive.databinding.FragmentWelcomeBinding
import com.hustunique.vlive.remote.Service
import com.hustunique.vlive.util.ToastUtil


class WelcomeFragment : Fragment() {

    private var loginState = false

    private val binding by lazy {
        FragmentWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.actor_img_transition)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (loginState) {
                    binding.contentLayout.transitionToStart()
                    binding.actorLayout.transitionToStart()
                    loginState = false
                } else if (!findNavController().popBackStack()) {
                    activity?.finish()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.loginButton.setOnClickListener {
            if (!loginState) {
                binding.contentLayout.transitionToEnd()
                binding.actorLayout.transitionToEnd()
                loginState = true
            } else {
                if (binding.username.text?.isEmpty() == true) {
                    ToastUtil.makeLong("请输入用户名")
                    return@setOnClickListener
                }
                lifecycleScope.launchWhenCreated {
                    Service.userReg(binding.username.text.toString())
                        .apply {
                            if (successful) {
                                findNavController().apply {
                                    navigate(
                                        WelcomeFragmentDirections.actionWelcomeFragmentToChannelListFragment(),
                                        FragmentNavigatorExtras(
                                            binding.actorImg to "channel_list_actor_img"
                                        )
                                    )
                                }
                            } else {
                                ToastUtil.makeLong(msg ?: "")
                            }
                        }
                }
            }
        }
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.root.transitionToState(R.id.end_state)
    }
}