/*
 * Copyright (C) 2013-2015 RoboVM AB
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 * Portions of this code is based on Google Inc's Google Mobile Ads sample
 * which is copyright (C) 2015 Google Inc.
 */
package tbs.bassjump;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSErrorException;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationDelegateAdapter;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.pods.google.GGLContextMobileAds;

public class GoogleMobileAdsApp extends UIApplicationDelegateAdapter {

    public static void main(String[] args) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(args, null, GoogleMobileAdsApp.class);
        }
    }

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        try {
            GGLContextMobileAds.getSharedInstance().configure();
        } catch (NSErrorException e) {
            System.err.println("Error configuring the Google context: " + e);
        }

        return true;
    }
}
