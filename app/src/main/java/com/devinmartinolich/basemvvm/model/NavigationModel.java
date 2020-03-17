package com.devinmartinolich.basemvvm.model;

import com.devinmartinolich.basemvvm.BaseMVVM;
import com.devinmartinolich.basemvvm.R;

/**
 * Name: NavigationModel
 * Created by Devin Martinolich 12/11/2019
 * Modified by
 * Purpose: Define the Navigation items and their properties
 */
public class NavigationModel {

    public static class Main {
        public static final int id = 0;
        public static final int drawable = R.drawable.ic_launcher_foreground;
        public static final String name = BaseMVVM.getAppInstance().getResources().getString(R.string.app_name);
    }
}
