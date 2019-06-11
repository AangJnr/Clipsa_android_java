package com.clipsa.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
 import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.clipsa.Clipsa;
import com.clipsa.R;
import com.clipsa.data.AppDataManager;
import com.clipsa.di.component.ActivityComponent;
 import com.clipsa.di.component.DaggerActivityComponent;
 import com.clipsa.di.module.ViewModule;
import com.clipsa.utilities.AppLogger;
import com.clipsa.utilities.BounceInterpolator;
import com.clipsa.utilities.CommonUtils;
import com.clipsa.utilities.CustomToast;
import com.clipsa.utilities.KeyboardUtils;
import com.clipsa.utilities.NetworkUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.inject.Inject;
import butterknife.Unbinder;
import timber.log.Timber;
import static com.clipsa.utilities.AppConstants.ROOT_DIR;


/**
 * Created by AangJnr on 18, September, 2018 @ 8:21 PM
 * Work Mail cibrahim@grameenfoundation.org
 * Personal mail aang.jnr@gmail.com
 */


public abstract class BaseActivity extends AppCompatActivity
        implements BaseContract.View{;

    @Inject
    public AppDataManager mAppDataManager;

    @Inject
    public AlertDialog.Builder mAlertDialogBuilder;

    @Inject
    ProgressDialog mProgressDialog;
    static Gson gson;

    private Unbinder mUnBinder;
    public String TAG;
    ActivityComponent activityComponent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gson = new Gson();

    }


    public static Gson getGson() {
        return gson;
    }

    @Override
    public void toggleFullScreen(Boolean hideNavBar, Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = getWindow().getDecorView().getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(flags);
            window.setStatusBarColor(Color.WHITE);
        }
    }



    public void hideStatusBar(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = getWindow().getDecorView().getSystemUiVisibility();
            window.getDecorView().setSystemUiVisibility(flags);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(mProgressDialog);
    }

    @Override
    public void setLoadingMessage(String message) {
     runOnUiThread(() -> {
    if(mProgressDialog != null && mProgressDialog.isShowing())
    mProgressDialog.setMessage(message);

        });
    }

    @Override
    public void showLoading(String title, String message, boolean indeterminate, int icon, boolean cancelableOnTouchOutside) {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(mProgressDialog, title, message, indeterminate, icon, cancelableOnTouchOutside);

    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void showMessage(String message) {

        runOnUiThread(() -> {

    if (message != null)
        CustomToast.makeToast(BaseActivity.this, message, Toast.LENGTH_LONG).show();
     else
        CustomToast.makeToast(BaseActivity.this, getString(R.string.some_error), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideSoftInput(this);
        }
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    public ActivityComponent getActivityComponent() {
       if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .viewModule(new ViewModule(this))
                    .applicationComponent(Clipsa.getAppContext(this).getComponent())
                    .build();
        }

        return activityComponent;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

      /*  MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);*/
        return true;

        //return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {



        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void openLoginActivityOnTokenExpire() {
        /*
      showDialog(true, "Re-authenticate", "Please login again to download updated data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAppDataManager.setUserLoggedInMode(DataManager.LoggedInMode.LOGGED_OUT);

                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    openActivity(intent);
                    finish();
                }, 1000);



            }
        }, "OK", (dialog, which) -> dialog.cancel(), "CANCEL", 0);
        */
    }

    public String getStringResources(int resource) {
        return getString(resource);
    }


    @Override
    public void showDialog(Boolean cancelable, String title, String message, DialogInterface.OnClickListener onPositiveButtonClickListener, String positiveText, DialogInterface.OnClickListener onNegativeButtonClickListener, String negativeText, int icon_drawable) {
        CommonUtils.showAlertDialog(mAlertDialogBuilder, cancelable, title, message, onPositiveButtonClickListener, positiveText, onNegativeButtonClickListener,
                negativeText, icon_drawable);

    }


    protected Toolbar setToolbar(String title) {
        Toolbar toolbar = null;
        try {

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white_24dp);

            toolbar.setNavigationOnClickListener((v) ->{
                    onBackPressed();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return toolbar;

    }


    public AppDataManager getAppDataManager() {
        return mAppDataManager;
    }

    public void showNoDataView(){
       // findViewById(R.id.place_holder).setVisibility(View.VISIBLE);
    }

    public void hideNoDataView(){
        // findViewById(R.id.place_holder).setVisibility(View.GONE);
     }

    public void setBackListener(@Nullable View view){
        onBackPressed();
     }

    protected boolean hasPermissions(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {


                }
                return false;
            }

        }
        return true;
    }

    protected File createTemporaryFile(String part, String ext) throws Exception {
        File dir = new File(ROOT_DIR + File.separator + ".temp/");
        if (!dir.exists()) Timber.i("Is DIR created?  %s", dir.mkdirs());
        AppLogger.i("Destination path is %s", dir);


        return File.createTempFile(part, ext, dir);
    }


    public void openActivity(Intent intent){
        startActivity(intent);
    }

    @Override
    public void finishActivity(){
        finish();
    }


    public static void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public static void runLayoutAnimation(final ListView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);
        recyclerView.setLayoutAnimation(controller);
        //recyclerView.getAdapter().notifyAll();
        recyclerView.scheduleLayoutAnimation();
    }


/*
    public CameraPosition moveCameraToPosition(GoogleMap mMap, LatLng latLng, Float ZOOM) {

        CameraPosition mCameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(ZOOM)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));

      */
/*
        if (mLastLocation != null) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastLocation.getLatitude(),
                            mLastLocation.getLongitude()), DEFAULT_ZOOM));
        }*//*



        return mCameraPosition;
    }
*/
/*

    public GroundOverlay addOverlay(GoogleMap mMap, LatLng place) {

        GroundOverlay groundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(place, 100)
                .transparency(0.5f)
                .zIndex(3)
                .image(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(ContextCompat.getDrawable(this, R.drawable.map_overlay)))));

        startOverlayAnimation(groundOverlay);


        return groundOverlay;
    }
*/


/*
    public void startOverlayAnimation(final GroundOverlay groundOverlay) {


        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator vAnimator = ValueAnimator.ofInt(0, 100);
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);
        vAnimator.setInterpolator(new LinearInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final Integer val = (Integer) valueAnimator.getAnimatedValue();
                groundOverlay.setDimensions(val);


            }
        });

        ValueAnimator tAnimator = ValueAnimator.ofFloat(0, 1);
        tAnimator.setRepeatCount(ValueAnimator.INFINITE);
        tAnimator.setRepeatMode(ValueAnimator.RESTART);
        tAnimator.setInterpolator(new LinearInterpolator());
        tAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float val = (Float) valueAnimator.getAnimatedValue();
                groundOverlay.setTransparency(val);

            }
        });

        animatorSet.setDuration(3000);
        animatorSet.playTogether(vAnimator, tAnimator);
        animatorSet.start();
    }
*/

    Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

/*
    protected void logOut(){
        //Todo show dialog to confirm logout
        CommonUtils.showAlertDialog(mAlertDialogBuilder, true, getString(R.string.log_out), getString(R.string.log_out_rational),
                (dialogInterface, i) ->   {
                    mAppDataManager.setUserAsLoggedOut();
                    dialogInterface.dismiss();
                }, getString(R.string.yes), (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }, getString(R.string.no), 0);
    }
*/

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean contains(List<Integer> list1, List<Integer> list2){
        for(Integer i : list2){
            if(list1.contains(i))
                return true;
        }
        return false;
    }

    public static boolean containsString(List<String> values, String v){
        for(String s : values){
            if(s.toLowerCase().contains(v.toLowerCase()))
                return true;
        }
        return false;
    }

    public void triggerBounceAnimation(View view){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
         anim.setInterpolator(new BounceInterpolator(0.2, 10));
        view.startAnimation(anim);
    }


}