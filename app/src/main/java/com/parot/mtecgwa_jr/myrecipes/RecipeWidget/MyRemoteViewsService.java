package com.parot.mtecgwa_jr.myrecipes.RecipeWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by mtecgwa-jr on 6/25/17.
 */

public class MyRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactory(getApplicationContext() , intent);
    }
}
