package com.clipsa.di.module;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.clipsa.R;
import com.clipsa.data.AppDataManager;
import com.clipsa.di.Scope.ActivityContext;
import com.clipsa.ui.base.BasePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by AangJnr on 19, September, 2018 @ 7:33 PM
 * Work Mail cibrahim@grameenfoundation.org
 * Personal mail aang.jnr@gmail.com
 */

@Module
public class ViewModule {


    private AppCompatActivity mActivity;

    public ViewModule(AppCompatActivity activity) {
        mActivity = activity;
    }


    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }


    @Provides
    public BasePresenter providesPresenter(AppDataManager appDataManager) {
        return new BasePresenter(appDataManager);
    }



    @Provides
    AlertDialog.Builder provideAppDialogBuilder(){
        return new  AlertDialog.Builder(mActivity, R.style.AppAlertDialog);
    }


    @Provides
    ProgressDialog provideProgressDialog(){
        return new ProgressDialog(mActivity);
    }





}