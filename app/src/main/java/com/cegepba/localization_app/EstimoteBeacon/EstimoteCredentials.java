package com.cegepba.localization_app.EstimoteBeacon;

import android.app.Application;

import com.estimote.proximity_sdk.api.EstimoteCloudCredentials;

public class EstimoteCredentials extends Application {
        public EstimoteCloudCredentials cloudCredentials =
                new EstimoteCloudCredentials("localization-app-o5p", "a38e9ecd8297e4ee81e372564fe23434");
}
