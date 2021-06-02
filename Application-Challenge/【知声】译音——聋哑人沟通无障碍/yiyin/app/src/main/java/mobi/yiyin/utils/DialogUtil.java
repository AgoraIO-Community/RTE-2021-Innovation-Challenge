package mobi.yiyin.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mobi.yiyin.R;
import mobi.yiyin.view.ProgressBarLayout;

/**
 *
 * @author wlk
 * @date 2020/9/2
 */

public class DialogUtil {
    public static Dialog createProgressDiaolg(Context context, String prompt, boolean isCancle) {
        Dialog mProgressDialog = new Dialog(context, R.style.CustomTheme_Dialog);
        ProgressBarLayout mProgressDialogView = (ProgressBarLayout) View.inflate(context, R.layout.progressbar_dialog, null);
        if (!TextUtils.isEmpty(prompt)) {
            TextView promptTv = (TextView) mProgressDialogView.findViewById(R.id.tvProgress);
            promptTv.setText(prompt);
        }
        mProgressDialog.setContentView(mProgressDialogView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mProgressDialog.setCanceledOnTouchOutside(isCancle);
        mProgressDialog.setCancelable(isCancle);
        return mProgressDialog;
    }
}
