/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kangraoo.basektlib.tools

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * This provides methods to help Activities load their UI.
 */
object UFragment {
    /**
     * The `fragment` is added to the container view with id `frameId`. The operation is
     * performed by the `fragmentManager`.
     *
     */
    fun addFragmentToActivity(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        frameId: Int
    ) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }

    /**
     * 如果没有fragment 就创建新的
     * @param fragmentManager
     * @param fragmentId
     * @param myFragment
     * @param <T>
     * @return
    </T> */
    fun <T : Fragment> getFragment(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        myFragment: T
    ): T {
        var fragment = fragmentManager.findFragmentById(fragmentId) as T?
        if (fragment == null) {
            fragment = myFragment
            addFragmentToActivity(fragmentManager, fragment, fragmentId)
        }
        return fragment
    }
    //    /**
    //     * 如果原来的fragment不再使用,可用如下方法用新的替换旧的,如果你的fragment依旧要使用的话 用 {@link #switchFragment(FragmentManager, int, Fragment, Fragment)}  }
    //     * @param fragmentManager
    //     * @param fragmentId
    //     * @param myFragment
    //     * @param <T>
    //     * @return
    //     */
    //    public static  <T extends Fragment>T replaceFragment(@NonNull FragmentManager fragmentManager, @NonNull int fragmentId, @NonNull T myFragment){
    //        Check.checkNotNull(fragmentManager);
    //        Check.checkNotNull(fragmentId);
    //        Check.checkNotNull(myFragment);
    //        T fragment = (T)fragmentManager.findFragmentById(fragmentId);
    //        if (fragment == null) {
    //            fragment = myFragment;
    //            addFragmentToActivity(fragmentManager, fragment,fragmentId);
    //        }else{
    //            fragment = myFragment;
    //            FragmentTransaction transaction = fragmentManager.beginTransaction();
    //            transaction.replace(fragmentId, fragment);
    //            transaction.commit();
    //        }
    //        return fragment;
    //    }
    /**
     * 切换 fragment 显示
     * @param fragmentManager
     * @param fragmentId
     * @param from
     * @param to
     * @param <T1>
     * @param <T2>
     * @return
    </T2></T1> */
    fun <T1 : Fragment?, T2 : Fragment> switchFragment(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        from: T1?,
        to: T2
    ): T2 {
        val transaction = fragmentManager.beginTransaction()
        if (from == null) {
            if (!to.isAdded) {
                transaction.add(fragmentId, to).commit()
            } else {
                transaction.show(to).commit()
            }
        } else {
            if (!to.isAdded) {
                transaction.hide(from).add(fragmentId, to).commit()
            } else {
                transaction.hide(from).show(to).commit()
            }
        }
        return to
    }

    /**
     * 如果原来的fragment不再使用,可用如下方法用新的替换旧的,如果你的fragment依旧要使用的话 用 [.switchFragment]  }
     * @param fragmentManager
     * @param fragmentId
     * @param myFragment
     * @param <T>
     * @return
    </T> */
    fun <T : Fragment> replaceFragment(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        myFragment: T
    ): T {
        return replaceFragment(fragmentManager, fragmentId, myFragment, false)
    }

    /**
     * 如果原来的fragment不再使用,可用如下方法用新的替换旧的,如果你的fragment依旧要使用的话 用 [.switchFragment]  }
     * @param fragmentManager
     * @param fragmentId
     * @param myFragment
     * @param needAddToBackStack 是否可以用back按键退栈
     * @param <T>
     * @return
    </T> */
    fun <T : Fragment> replaceFragment(
        fragmentManager: FragmentManager,
        fragmentId: Int,
        myFragment: T,
        needAddToBackStack: Boolean
    ): T {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentId, myFragment)
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commitAllowingStateLoss()
        return myFragment
    }
}
