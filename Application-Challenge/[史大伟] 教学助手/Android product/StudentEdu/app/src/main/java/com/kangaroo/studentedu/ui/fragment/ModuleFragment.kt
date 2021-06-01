package com.kangaroo.studentedu.ui.fragment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangaroo.studentedu.R;
import com.kangaroo.studentedu.data.model.Module
import com.kangaroo.studentedu.tools.UUser
import com.kangaroo.studentedu.ui.activity.*
import com.kangaroo.studentedu.ui.adapter.ModuleAdapter
import com.kangraoo.basektlib.ui.BFragment
import com.kangraoo.basektlib.widget.listener.OnOnceClickListener
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.fragment_module.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-26 14:26
 * #学校功能#
 */
class ModuleFragment : BFragment(){

    companion object{

        @JvmStatic
        fun newInstance() = ModuleFragment()
        
    }

    override fun getLayoutId() = R.layout.fragment_module

    var adaper : ModuleAdapter?= null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)
        recycle.layoutManager = GridLayoutManager(visitActivity(),3)
        adaper = ModuleAdapter()
        recycle.adapter = adaper
        val list = arrayListOf<Module>()
        if(UUser.getType()== UUser.TEACHER){
            list.add(Module("学员考勤",R.drawable.ic_baseline_connect_without_contact_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    KaoQingActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("课堂点评",R.drawable.ic_baseline_emoji_people_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    DianPingActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("布置作业",R.drawable.ic_baseline_event_note_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    BuZhiHomeWorkActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("批改作业",R.drawable.ic_baseline_event_available_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    PiGaiHomeWorkActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("我的班级",R.drawable.ic_baseline_group_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    MyClassActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("数据统计",R.drawable.ic_baseline_multiline_chart_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    TongJiActivity.startFrom(visitActivity())
                }

            }))
        }else{
            list.add(Module("签到",R.drawable.ic_baseline_location_on_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    QianDaoActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("课堂点评",R.drawable.ic_baseline_emoji_people_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    DianPingActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("写作业",R.drawable.ic_baseline_event_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    XieHomeWorkActivity.startFrom(visitActivity())
                }

            }))
            list.add(Module("数据统计",R.drawable.ic_baseline_multiline_chart_24,object : OnOnceClickListener(){
                override fun onOnceClick(var1: View) {
                    TongJiActivity.startFrom(visitActivity())
                }

            }))
        }
        adaper!!.setNewInstance(list)

    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        setTitle("主功能")
    }

}
