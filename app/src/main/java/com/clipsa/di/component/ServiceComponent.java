package com.clipsa.di.component;


import com.clipsa.di.Scope.PerService;
import com.clipsa.di.module.ServiceModule;

import dagger.Component;

/**
 * Created by AangJnr on 20, September, 2018 @ 2:20 AM
 * Work Mail cibrahim@grameenfoundation.org
 * Personal mail aang.jnr@gmail.com
 */

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

   // void inject(SyncService service);

}

