package darko.com.weatherapp.activities;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import darko.com.weatherapp.server.ServerEndpoints;
import retrofit.RestAdapter;

/**
 * Created by Petkovski on 01.02.2016.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public RestAdapter myRestAdapter;
    public ServerEndpoints serverEndpoints;
    private ProgressDialog progress;


    @Override
    public void onClick(View v) {
        //todo
    }

    public void showLoadingDialog(String loadingText) {
        dismissProgressDialog();
        progress = ProgressDialog.show(this, "",
                loadingText, true);
    }

    public void dismissProgressDialog() {
        if (progress != null) {
            if (progress.isShowing())
                progress.dismiss();
        }
    }

    /**
     * Shows a snackbar
     *
     * @param view - parent view
     * @param text - text to show in the snackbar
     */
    public void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void showSnackBar(View view, String text, String actionText, View.OnClickListener clickListener) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Undo", clickListener).show();
    }

    /**
     * Show toast
     *
     * @param text - text to be displayed in the Toast
     */
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * focus to a view
     */
    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
