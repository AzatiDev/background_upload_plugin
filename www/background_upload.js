/*global cordova, module*/

module.exports = {
    uploadFiles: function (successCallback, errorCallback, options) {
        if (!options) {
            options = {};
        }

        var params = {
            filesArray: options.files,
            data: options.data
        };
        cordova.exec(successCallback, errorCallback, "BackgroundUpload", "uploadFiles", [params]);
    }
};
