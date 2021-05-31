package com.hustunique.vlive.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hustunique.vlive.databinding.FragmentCreateRoomBinding
import com.hustunique.vlive.remote.Service
import com.hustunique.vlive.util.ToastUtil
import com.hustunique.vlive.util.UserInfoManager

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/24
 */
class CreateRoomFragment : Fragment() {

    private val binding by lazy {
        FragmentCreateRoomBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.createBtn.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                Service.createChannel(
                    UserInfoManager.uname,
                    binding.channelDesc.text.toString()
                ).let {
                    if (it.successful) {
                        if (binding.virtualCb.isChecked) {
                            findNavController().navigate(
                                CreateRoomFragmentDirections.actionCreateRoomFragmentToActorChooseFragment(
                                    UserInfoManager.uname,
                                    false
                                )
                            )
                        } else {
                            findNavController().navigate(
                                CreateRoomFragmentDirections.actionCreateRoomFragmentToSceneActivity(
                                    UserInfoManager.uname,
                                    0
                                )
                            )
                        }
                    } else {
                        ToastUtil.makeShort("创建失败 ${it.msg}")
                    }
                }
            }
        }
        binding.channelName.text = "${UserInfoManager.uname}的派对"
        return binding.root
    }
}