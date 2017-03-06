(function () {
    'use strict';
    angular
    .module('cmadBlog')
    .controller('RegisterController', ['UserService',  '$location' , function (UserService, $location) {
        var rc = this;

        rc.register = register;

        function register(isValid) {
            if (isValid) {
                rc.dataLoading = true;
                console.log("going to call : UserService");

                // Create user
                UserService.CreateUser(rc.user).then(function (response) {
                    console.log(response);
                    console.log('got the response : response.writtenToDb : '+response.writtenToDb);
                    if (response.writtenToDb) {
                        $location.path('/');
                        console.log('path set');
                    } else {
                        rc.dataLoading = false;
                    }
                });
            }
        }
    }]);
    
})();