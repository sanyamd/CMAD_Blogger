(function() {
	'use strict';
	angular
	.module('cmadBlog')
	.controller('LoginController', ['UserService',  '$location' , '$scope', '$timeout', '$window', function (UserService, $location, $scope, $timeout, $window) {
		var lc = this;
		lc.login = login;
		$scope.loginError = "";

		(function initController() {
            // reset session status
            console.log("Clearing the session storage if any data is there");
            UserService.ClearCredentials();
        })();

        function login() {
        	lc.dataLoading = true;
        	console.log("Login entered");

        	UserService.Login(lc.user).then(function(response) {
        		console.log("matchFound : "+response.matchFound);
        		console.log("response : "+response);
        		if (response.matchFound) {
        			UserService.SetCredentials(response);
        			console.log("credentials set in session storage");
        			$location.path('/');
        			$timeout(function() {$window.location.reload();}, 100);
        		} else {
        			console.log("match not found");
        			$scope.loginError = "Username or password doesn't match. Please try again...";
        			lc.dataLoading = false;
        		}
        	});
        }
    }]);
})();
