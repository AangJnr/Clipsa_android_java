package com.clipsa.di.component;


import com.clipsa.di.Scope.PerActivity;
import com.clipsa.di.module.ViewModule;
import com.clipsa.ui.base.BaseActivity;

import dagger.Component;

/**
 * Created by AangJnr on 20, September, 2018 @ 2:12 AM
 * Work Mail cibrahim@grameenfoundation.org
 * Personal mail aang.jnr@gmail.com
 */

@PerActivity
@Component(modules = {ViewModule.class}, dependencies = {ApplicationComponent.class})
public interface ActivityComponent {


    void inject(BaseActivity view);

}