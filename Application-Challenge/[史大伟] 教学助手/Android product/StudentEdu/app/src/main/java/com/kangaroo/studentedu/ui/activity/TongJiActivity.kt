package com.kangaroo.studentedu.ui.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.gyf.immersionbar.ktx.immersionBar
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.tools.UUser
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.ui.BActivity
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import kotlinx.android.synthetic.main.activity_tong_ji.*
import kotlinx.android.synthetic.main.fragment_me.*


/**
 * 自动生成：by WaTaNaBe on 2021-05-26 15:17
 * #数据统计#
 */
class TongJiActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, TongJiActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_tong_ji

    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }


        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "数据统计"
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener() {})


        if(UUser.getType()== UUser.TEACHER){
            student.visibility = View.GONE
        }else{
            teacher.visibility = View.GONE
        }
        var list = arrayListOf<PieEntry>()
        list.add(PieEntry(1.0f,"男性"))
        list.add(PieEntry(1.0f,"女性"))

        var pieDataSet = PieDataSet(list,"班级男女比例")
        pie.setData(PieData(pieDataSet));

        pieDataSet.setColors(Color.RED,Color.BLUE);//设置各个数据的颜色



        var list2 = arrayListOf<BarEntry>()


        list2.add( BarEntry(1.0f,5.0f));
        list2.add(BarEntry(2.0f,5.0f));
        list2.add(BarEntry(3.0f,5.0f));
        list2.add(BarEntry(4.0f,5.0f));

        var barDataSet= BarDataSet(list2,UUser.getKe());
        var barData=BarData(barDataSet);
        bar.setData(barData);

        bar.getDescription().setEnabled(false);//隐藏右下角英文
        bar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//X轴的位置 默认为上面
        bar.getAxisRight().setEnabled(false);//隐藏右侧Y轴   默认是左右两侧都有Y轴





        var list3 = arrayListOf<RadarEntry>()

        list3.add(RadarEntry(1.0f));
        list3.add(RadarEntry(1.0f));
        list3.add(RadarEntry(1.0f));

        var radarDataSet= RadarDataSet(list3,"素质教育各科");
        var radarData= RadarData(radarDataSet);
        radar.setData(radarData);

        //Y轴最小值不设置会导致数据中最小值默认成为Y轴最小值
        radar.getYAxis().setAxisMinimum(0.0f);


        var list4 = arrayListOf<Entry>()
        list4.add(Entry(0.0f,2.0f));     //其中两个数字对应的分别是   X轴   Y轴
        list4.add(Entry(1.0f,2.0f));
        list4.add(Entry(2.0f,2.0f));
        list4.add(Entry(3.0f,1.0f));
        list4.add(Entry(4.0f,1.0f));

        //list是你这条线的数据  "语文" 是你对这条线的描述（也就是图例上的文字）
        var lineDataSet= LineDataSet(list4,"班级出勤率");
        var lineData= LineData(lineDataSet);
        line.setData(lineData);

        //简单美化

        //   X轴所在位置   默认为上面
        line.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //隐藏右边的Y轴
        line.getAxisRight().setEnabled(false);

    }

}
