package com.dong.circlelive

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import java.io.ByteArrayOutputStream


/**
 * Create by dooze on 5/3/21  7:35 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */


fun getAppName(context: Context, pID: Int): String? {
    var processName: String? = null
    val l: List<*> = (context.getSystemService(ACTIVITY_SERVICE) as ActivityManager).runningAppProcesses
    val i = l.iterator()
    while (i.hasNext()) {
        val info = i.next() as RunningAppProcessInfo
        try {
            if (info.pid == pID) {
                processName = info.processName
                return processName
            }
        } catch (e: Exception) {
            // Log.d("Process", "Error>> :"+ e.toString());
        }
    }
    return processName
}


fun Activity.start(activityClazz: Class<*>, finishActivity: Boolean = false) {
    startActivity(Intent(this, activityClazz))
    if (finishActivity) {
        finish()
    }
}

fun Fragment.start(activityClazz: Class<*>, finishActivity: Boolean = false) {
    startActivity(Intent(requireContext(), activityClazz))
    if (finishActivity) {
        activity?.finish()
    }
}

fun Bitmap.toByteArray(): ByteArray {
    val output = ByteArrayOutputStream()
    var result: ByteArray
    output.use {
        compress(Bitmap.CompressFormat.JPEG, 100, it)
        recycle()
        result = output.toByteArray()
    }
    return result
}

fun FragmentActivity.showFragment(
    targetFragment: Fragment,
    fromFragment: Fragment? = null,
    containerId: Int,
    addToBackStack: Boolean = true,
    tag: String? = null
) {
    val fragmentTag = tag ?: targetFragment::class.java.simpleName
    val tr = supportFragmentManager.beginTransaction()
    if (fromFragment != null) {
        tr.hide(fromFragment)
    }
    tr.add(containerId, targetFragment, fragmentTag)
        .show(targetFragment)
    if (addToBackStack) {
        tr.addToBackStack(fragmentTag)
    }
    tr.commit()
}

fun Fragment.transactionByActivity(): FragmentTransaction {
    return requireActivity().supportFragmentManager.beginTransaction()
}

fun Fragment.showOtherFragment(
    targetFragment: Fragment,
    containerId: Int,
    addToBackStack: Boolean = true,
    hide: Boolean = true,
    tag: String? = null
) {
    val fragmentTag = tag ?: targetFragment::class.java.simpleName
    val tr = transactionByActivity()
    if (hide) {
        tr.hide(this)
    }
    tr.add(containerId, targetFragment, fragmentTag)
        .show(targetFragment)
    if (addToBackStack) {
        tr.addToBackStack(fragmentTag)
    }
    tr.commit()
}

fun Throwable.toast() {
    toast(this.message ?: return)
}

fun toast(@StringRes stringRes: Int) {
    Toast.makeText(appContext, stringRes, Toast.LENGTH_SHORT).show()
}

fun toast(text: String) {
    Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()
}

fun getString(@StringRes resId: Int) = appContext.getString(resId)

fun getColor(@ColorRes resId: Int, context: Context = appContext) = ContextCompat.getColor(context, resId)


fun Activity.showIme(focusView: View) {
    window?.let {
        WindowInsetsControllerCompat(it, focusView).show(WindowInsetsCompat.Type.ime())
    }
}

fun Activity.hideIme() {
    window?.let {
        WindowInsetsControllerCompat(it, it.decorView).hide(WindowInsetsCompat.Type.ime())
    }
}

fun Fragment.showIme(focusView: View) {
    activity?.showIme(focusView)
}

fun Fragment.hideIme() {
    activity?.hideIme()
}