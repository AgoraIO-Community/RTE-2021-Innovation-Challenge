package com.dong.circlelive.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dong.circlelive.R
import com.dong.circlelive.base.BaseFragment
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.databinding.FragmentActivityBinding
import com.dong.circlelive.live.OnAdapterItemClickListener
import com.dong.circlelive.live.OnAdapterItemLongClickListener
import com.dong.circlelive.main.MainViewModel


/**
 * Create by dooze on 2021/5/25  10:26 上午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class ActivitiesFragment :BaseFragment(R.layout.fragment_activity), OnAdapterItemClickListener, OnAdapterItemLongClickListener,
    Toolbar.OnMenuItemClickListener {

    private val binding by viewBinding(FragmentActivityBinding::bind)

    private val viewModel by viewModels<ActivitiesViewModel>()

    private val activitiesAdapter = ActivitiesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.toolbar.inflateMenu(R.menu.menu_activty_toolbar)
        binding.toolbar.setOnMenuItemClickListener(this)
        binding.rvActivities.layoutManager = LinearLayoutManager(requireContext())
        binding.rvActivities.adapter = activitiesAdapter
        activitiesAdapter.itemClickListener = this
        activitiesAdapter.itemLongClickListener = this
        viewModel.activities.observe(viewLifecycleOwner) { activities ->
            activitiesAdapter.refresh(activities ?: emptyList())
        }
    }

    override fun onAdapterItemClick(view: View, position: Int) {
        when(view.id) {
            R.id.iv_activity_cover -> {

            }
            R.id.tv_activity_action -> {
                val activity = activitiesAdapter.getItem(position) ?: return
                viewModel.makeActivityAction(activity)
            }
        }
    }

    override fun onAdapterItemLongClick(view: View, position: Int): Boolean {
        when(view.id) {
            R.id.layout_activity -> {
                val activity = activitiesAdapter.getItem(position) ?: return true
                viewModel.deleteActivity(activity)
            }
        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_activity_clear_all -> {
                viewModel.clearAll()
            }
        }
        return true
    }

    override fun onDestroyView() {
        val mainViewModel by activityViewModels<MainViewModel>()
        mainViewModel.markReadActivity()
        super.onDestroyView()
    }


}