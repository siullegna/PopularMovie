package com.hap.popularmovie.dagger.component;

import com.hap.popularmovie.MovieActivity;
import com.hap.popularmovie.MovieDetailActivity;

/**
 * Created by luis on 9/22/17.
 */

interface AppGraph {
    void inject(MovieActivity movieActivity);

    void inject(MovieDetailActivity movieDetailActivity);
}
