(function () {
    'use strict';
     angular.module('cmadBlog').service('UserService', function ($http, $window) {
        var service = {};
 
        service.GetUserInfo = GetUserInfo;
        service.CreateUser = CreateUser;
        service.UpdateUser = UpdateUser;

        service.Login = Login;
        service.SetCredentials = SetCredentials;
        service.ClearCredentials = ClearCredentials;
 
        return service;
 
        function GetUserInfo(userEmail) {
            console.log("GetUserInfo");
            return $http.get('online/blog/userInfo/' + userEmail).then(handleSuccess, handleError);
        }
 
        function CreateUser(user) {
            console.log("CreateUser");
            return $http.post('online/blog/registerUser', user).then(handleSuccess, handleError);
        }
 
        function UpdateUser(user) {
            console.log("UpdateUser");
            return $http.put('online/blog/updateUserInfo', user).then(handleSuccess, handleError);
        }

        function Login(user) {
            console.log("Login");
            return $http.post('online/blog/signIn', user).then(handleSuccess, handleError);
        }

        function SetCredentials(response) {
            console.log("SetCredentials");
            // var authdata = Base64.encode(email + ':' + );
            $window.localStorage.setItem("currentUser", JSON.stringify(response));
            $window.localStorage.setItem("authenticated", true);
        }
 
        function ClearCredentials() {
            console.log("ClearCredentials");
            $window.localStorage.removeItem("currentUser");
            $window.localStorage.removeItem("authenticated");
            // $rootScope.currentUser = {};
            // $rootScope.authenticated = false;
        }

        // private functions 
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
    });
})();

