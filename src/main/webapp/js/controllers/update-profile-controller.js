(function () {
    'use strict';
    angular
    .module('cmadBlog')
    .controller('UpdateProfileController', ['UserService',  '$location' , '$window', '$timeout', function (UserService, $location, $window, $timeout) {
        var upc = this;
        var tempUser = $window.localStorage.getItem("currentUser");
        tempUser = JSON.parse(tempUser);
        upc.user = {};

        console.log('tempUser.user.email : '+tempUser.email);
        console.log('tempUser.user.userInfo.name : '+tempUser.userInfo.name);
        console.log('tempUser.user.userInfo.avatarImgSrc : '+tempUser.userInfo.avatarImgSrc);
        console.log('tempUser.user.userInfo.areaOfInterest : '+tempUser.userInfo.areaOfInterest);

        upc.user.email = tempUser.email;
        upc.user.name  = tempUser.userInfo.name;
        upc.user.avatarImgSrc = tempUser.userInfo.avatarImgSrc;
        upc.user.areaOfInterest  = tempUser.userInfo.areaOfInterest;

        upc.updateUser = updateUser;

        function updateUser() {
            upc.dataLoading = true;
            console.log("going to call : UserService");

            // Create user
            UserService.UpdateUser(upc.user).then(function (response) {
                console.log(response);
                console.log('got the response : response.writtenToDb : '+response.writtenToDb);
                if (response.writtenToDb) {
                    setCredentials();
                    $location.path('/');
                    $timeout(function() {$window.location.reload();}, 100);
                    console.log('path set');
                } else {
                    upc.dataLoading = false;
                }
            });
        }

        function setCredentials() {
            console.log("tempUser.userInfo.name : "+tempUser.userInfo.name);
            console.log("upc.user.name : "+upc.user.name);
            tempUser.userInfo.name = upc.user.name;
            $window.localStorage.setItem("currentUser", JSON.stringify(tempUser));
        }
    }]);
    
})();
