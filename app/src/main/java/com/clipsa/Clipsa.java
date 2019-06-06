package com.clipsa;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.clipsa.di.component.DaggerApplicationComponent;

import com.clipsa.di.component.ApplicationComponent;
import com.clipsa.di.module.ApplicationModule;
import com.clipsa.utilities.AppLogger;

import static com.clipsa.utilities.FileUtils.createNoMediaFile;

public class Clipsa extends Application {

    private ApplicationComponent mApplicationComponent;


    public static Clipsa getAppContext(Context context) {
        return (Clipsa) context.getApplicationContext();
    }


    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNoMediaFile();

        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);

        //Initialize application logging mechanism
        AppLogger.init(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }

}
