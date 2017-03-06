(function () {
    'use strict';
    angular.module('cmadBlog').service('AuthenticationService', function ($http, $rootScope) {
        var service = {};
 
        service.Login = Login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;
 
        return service;
 
        function Login(user) {
            console.log("calling signIN");
            $http.post('http://localhost:8080/CMAD_Blogger/online/blog/signIn', user).then(handleSuccess, handleError);
        }

        function handleSuccess(res) {
            console.log("handleSuccess");
            return res.data;
        }
 
        function handleError(error) {
            console.log("handleError");
            return function () {
                return { success: false, message: error };
            };
        }
 
        function SetCredentials(email) {
            console.log("SetCredentials");
            // var authdata = Base64.encode(email + ':' + );
            $rootScope.currentUser = email;
            $rootScope.authenticated = true;
        }
 
        function ClearCredentials() {
            console.log("ClearCredentials");
            $rootScope.currentUser = {};
            $rootScope.authenticated = false;
        }
    });
})();
