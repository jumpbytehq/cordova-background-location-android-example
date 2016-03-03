cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/cordova-plugin-whitelist/whitelist.js",
        "id": "cordova-plugin-whitelist.whitelist",
        "runs": true
    },
    {
        "file": "plugins/cordova-background-location-android/www/bgGeoLocationAndroid.js",
        "id": "cordova-background-location-android.bgGeoLocationAndroid",
        "clobbers": [
            "bgGeoLocationAndroid"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{}
// BOTTOM OF METADATA
});