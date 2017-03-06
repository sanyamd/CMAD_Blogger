(function() {
	'use strict';
	angular
	.module('cmadBlog')
	.controller('NavbarController', ['$window', 'UserService', '$location', '$timeout', '$scope',   function ($window, UserService, $location, $timeout, $scope) {
        var nc = this;
        nc.authenticated = $window.localStorage.getItem("authenticated");
        nc.user = $window.localStorage.getItem("currentUser");
        console.log("nc.user before : " + nc.user);

        nc.user = JSON.parse(nc.user);
        console.log("nc.user after : " + nc.user);

        if (nc.user != null) {
            console.log("nc.user.matchFound : " + nc.user.matchFound);
            console.log("nc.user.userInfo.name : " + nc.user.userInfo.name);
            console.log("nc.user.email : " + nc.user.email);
        }
        nc.userInfo = {};
        
        $scope.searchString = "";
        nc.logout = logout;

        function logout() {
            console.log("Logout entered");
            UserService.ClearCredentials();
            console.log("credentials cleared from sessionStorage");
            $location.path('/login');
            $timeout(function() {$window.location.reload();}, 100);
        }
    }]);
})();
