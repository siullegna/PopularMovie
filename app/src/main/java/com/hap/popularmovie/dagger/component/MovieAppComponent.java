package com.hap.popularmovie.dagger.component;

import com.hap.popularmovie.dagger.module.NetworkModule;
import com.hap.popularmovie.dagger.scope.ApplicationScope;

import dagger.Component;

/**
 * Created by luis on 9/21/17.
 */
@ApplicationScope
@Component(modules = {NetworkModule.class})
public interface MovieAppComponent extends AppGraph {
}