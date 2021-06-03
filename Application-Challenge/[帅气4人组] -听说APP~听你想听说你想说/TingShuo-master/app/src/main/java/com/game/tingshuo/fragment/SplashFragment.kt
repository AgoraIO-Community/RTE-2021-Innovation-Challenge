package com.game.tingshuo.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.SPUtils
import com.game.tingshuo.R
import com.game.tingshuo.ui.MainActivity

class SplashFragment : Fragment() {
    private val bgRes = intArrayOf(
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher_round,
        R.mipmap.ic_launcher
    )

    private val bgText = intArrayOf(
        R.string.tab1_text,
        R.string.tab2_text,
        R.string.tab3_text
    )
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        val button = view.findViewById<Button>(R.id.button)
        val iv_splash = view.findViewById<ImageView>(R.id.iv_splash)
        val tv_splash = view.findViewById<TextView>(R.id.tv_splash)
        var index = 0
        if (arguments != null) {
            index = requireArguments().getInt("index")
        }
        iv_splash.setBackgroundResource(bgRes[index])
        tv_splash.setText(bgText[index])
        button.setOnClickListener {
            SPUtils.getInstance().put("iseverused",true)
            startActivity(Intent(activity, MainActivity::class.java))
            (mContext as Activity?)!!.finish()
        }
        button.visibility = if (index == 2) View.VISIBLE else View.GONE
        return view
    }
}