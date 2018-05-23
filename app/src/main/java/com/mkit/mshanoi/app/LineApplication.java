package com.mkit.mshanoi.app;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mkit.mshanoi.app.di.AppModule;


import dagger.ObjectGraph;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by MinhDN on 28/12/2017.
 */

public class LineApplication extends MultiDexApplication {

    @Getter private String baseUrl;
    public static final String apiBaseUrl = "http://10.145.40.188:8888/";
    public static final String apiFireBaseUrl = "https://ms-ha-noi.firebaseio.com/ms ha noi";
//    public static final String apiBaseUrl = "http://10.145.40.74:8888/";

    public static final Boolean ChangeVplusToVpoint = true;
    private static GoogleAnalytics analytics;
    private static Tracker tracker;
    public static GoogleAnalytics analytics() {
        return analytics;
    }
    public static Tracker tracker() {
        return tracker;
    }
    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        baseUrl = apiBaseUrl;

        //google analytic config
        analytics = GoogleAnalytics.getInstance(this);

        // TODO: Replace the tracker-id with your app one from https://www.google.com/analytics/web/
        tracker = analytics.newTracker("UA-107206025-1");

        // Provide unhandled exceptions reports. Do that first after creating the tracker
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);

        // Enable automatic activity tracking for your app
        tracker.enableAutoActivityTracking(true);

        // dagger
        objectGraph = ObjectGraph.create(new AppModule(this));
        objectGraph.inject(this);
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }
}
