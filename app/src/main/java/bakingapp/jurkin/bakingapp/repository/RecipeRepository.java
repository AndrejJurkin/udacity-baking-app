/*
 *
 *  * Copyright 2017 Andrej Jurkin
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package bakingapp.jurkin.bakingapp.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import bakingapp.jurkin.bakingapp.model.Recipe;
import io.reactivex.Observable;

/**
 * Created by Andrej Jurkin on 7/15/17.
 */

@Singleton
public class RecipeRepository {
    private RecipeRemoteDataSource remoteData;

    @Nullable
    private List<Recipe> recipeCache;

    @Inject
    RecipeRepository(RecipeRemoteDataSource remote) {
        this.remoteData = remote;
    }

    @NonNull
    public Observable<List<Recipe>> getRecipes() {
        if (recipeCache != null) {
            return Observable.just(recipeCache);
         }

         return remoteData.getRecipes().doOnNext(recipes -> this.recipeCache = recipes);
    }

    @NonNull
    public Observable<Recipe> getRecipe(int id) {
        if (recipeCache == null) {
            return Observable.error(new Throwable("Recipe not found."));
        }

        for (Recipe r : recipeCache) {
            if (r.getId() == id) {
                return Observable.just(r);
            }
        }

        return Observable.error(new Throwable("Recipe not found."));
    }
}
