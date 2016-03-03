/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');

        var bgGeo = window.BackgroundGeolocation || window.backgroundGeoLocation || window.bgGeoLocationAndroid;

        /**
        * This callback will be executed every time a geolocation is recorded in the background.
        */
        var callbackFn = function(location, taskId) {
            console.log("location callback received " + JSON.stringify(location));
            var coords = location.coords;
            var lat    = coords.latitude;
            var lng    = coords.longitude;

            console.log("Location updated " + lat + "," + lng);
            // Simulate doing some extra work with a bogus setTimeout.  This could perhaps be an Ajax request to your server.
            // The point here is that you must execute bgGeo.finish after all asynchronous operations within the callback are complete.
            setTimeout(function() {
              bgGeo.finish(taskId); // <-- execute #finish when your work in callbackFn is complete
            }, 1000);
        };

        var failureFn = function(error) {
            console.log('BackgroundGeoLocation error' +  JSON.stringify(error));
        }

        bgGeo.configure(callbackFn, failureFn, {
            desiredAccuracy: 10,
            stationaryRadius: 10,
            distanceFilter: 15,
            debug: true, // <-- enable this hear sounds for background-geolocation life-cycle.
            stopOnTerminate: false, // <-- enable this to clear background location settings when the app terminates
            locationService: bgGeo.service.ANDROID_FUSED_LOCATION,
            interval: 3000,
            fastestInterval: 2500,
            notificationTitle: "LOCATIONTEST", // Android
            notificationText: "Background location tracking is ON.", // Android
            notificationIconLarge: "icon", // Android
            notificationIconSmall: "icon", // Android
            startOnBoot: true,
            startForeground: false,

            url: 'http://posttestserver.com/post.php?dir=jumpbyte-background-test',
            //url: 'https://obscure-hamlet-79929.herokuapp.com',
            method: 'POST',
            batchSync: true,       // <-- [Default: false] Set true to sync locations to server in a single HTTP request.
            autoSync: true,         // <-- [Default: true] Set true to sync each location to server as it arrives.
            maxDaysToPersist: 1,    // <-- Maximum days to persist a location in plugin's SQLite database when HTTP fails
            headers: {
                "X-FOO": "bar"
            },
            params: {
                "auth_token": "maybe_your_server_authenticates_via_token_YES?"
            }
        });

        bgGeo.start();

        window.navigator.geolocation.getCurrentPosition(function(location) {
            console.log('Location from Phonegap');             
        });
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();