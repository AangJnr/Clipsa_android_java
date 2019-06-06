package com.clipsa.di.component;


import android.app.Application;
import android.content.Context;

import com.clipsa.Clipsa;
import com.clipsa.data.AppDataManager;
import com.clipsa.di.Scope.ApplicationContext;
import com.clipsa.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by AangJnr on 20, September, 2018 @ 2:12 AM
 * Work Mail cibrahim@grameenfoundation.org
 * Personal mail aang.jnr@gmail.com
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(Clipsa app);


    @ApplicationContext
    Context getContext();

    Application getApplication();

    AppDataManager getAppDataManager();

}