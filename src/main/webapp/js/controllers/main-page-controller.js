(function() {
	'use strict';
 
 	/* Adding main page controller*/
    angular.module('cmadBlog')
	.controller('MainPageController', ['BlogService',  '$location' , '$scope', 'blogsList', function (BlogService, $location, $scope, blogsList) {
		console.log('blogsList : ' + blogsList);
		var tempbloglist = blogsList;
		$scope.blogs = [];

		var i = 0;
		for (;i<tempbloglist.length; i++) {
			$scope.blogs[i] = JSON.parse(tempbloglist[i]);
			console.log('$scope.blogs [i] ID: ' + $scope.blogs[i].blogID);
		}

		console.log('$scope.blogs [0] ID: ' + $scope.blogs[0].blogID);

		console.log('$scope.blogs :  ' + $scope.blogs);

		$scope.tempComment = '';
		$scope.tempBlogTitle = '';
		$scope.tempBlogTags = null;
		$scope.tempBlogBody = '';
	}]);

})();