package com.agora.data.manager;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.agora.data.BaseError;
import com.agora.data.DataRepositroy;
import com.agora.data.model.User;
import com.agora.data.observer.DataObserver;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;

import java.util.Random;

public final class UserManager {
    private Logger.Builder mLogger = XLog.tag("UserManager");

    private static final String TAG = UserManager.class.getSimpleName();

    private static final String TAG_USER = "user";

    private Context mContext;
    private volatile static UserManager instance;

    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();

    private UserManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static UserManager Instance(Context context) {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null)
                    instance = new UserManager(context);
            }
        }
        return instance;
    }

    public MutableLiveData<User> getUserLiveData() {
        return mUserLiveData;
    }

    public void loginIn() {
        if (UserManager.Instance(mContext).getUserLiveData().getValue() == null) {
            String userValue = PreferenceManager.getDefaultSharedPreferences(mContext)
                    .getString(TAG_USER, null);
            User mUser = null;
            if (TextUtils.isEmpty(userValue)) {
                mUser = new User();
                mUser.setName(radomName());
                mUser.setAvatar(radomAvatar());
            } else {
                mUser = new Gson().fromJson(userValue, User.class);
            }

            mLogger.d("loginIn() called");
            DataRepositroy.Instance(mContext)
                    .login(mUser)
                    .subscribe(new DataObserver<User>(mContext) {
                        @Override
                        public void handleError(@NonNull BaseError e) {
                            mLogger.e("loginIn faile ", e);
                        }

                        @Override
                        public void handleSuccess(@NonNull User user) {
                            mLogger.i("loginIn success user= %s", user);
                            onLoginIn(user);
                        }
                    });
        }
    }

    public void onLoginIn(User mUser) {
        mUserLiveData.postValue(mUser);

        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(TAG_USER, new Gson().toJson(mUser))
                .apply();
    }

    public void onLoginOut(User mUser) {
        mUserLiveData.postValue(null);

        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .remove(TAG_USER)
                .apply();
    }

    public void update(User mUser) {
        mUserLiveData.postValue(mUser);

        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(TAG_USER, new Gson().toJson(mUser))
                .apply();
    }

    public static String radomAvatar() {
        return String.valueOf(new Random().nextInt(13) + 1);
    }

    public static String radomName() {
        return "User " + String.valueOf(new Random().nextInt(999999));
    }
}
