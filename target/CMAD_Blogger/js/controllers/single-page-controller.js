(function() {
    'use strict';

    angular.module('cmadBlog')
    .controller('SinglePostController', ['BlogService',  '$window' , '$scope', 'blogsList', 'singleBlog', function (BlogService, $window, $scope, blogsList, singleBlog) {
        var spc = this;
        console.log('blogsList : ' + blogsList);
        console.log('singleBlog : ' + singleBlog);

        var tempbloglist = blogsList;
        $scope.blogs = [];
        $scope.blog = singleBlog;
        console.log('$scope.blog : ' + $scope.blog);
        
        spc.authenticated = $window.localStorage.getItem("authenticated");
        spc.user = $window.localStorage.getItem("currentUser");
        console.log("spc.user before : " + spc.user);

        spc.user = JSON.parse(spc.user);

        var i = 0;
        for (;i<tempbloglist.length; i++) {
            $scope.blogs[i] = JSON.parse(tempbloglist[i]);
            console.log('$scope.blogs [i] ID: ' + $scope.blogs[i].blogID);
        }

        console.log('$scope.blogs [0] ID: ' + $scope.blogs[0].blogID);
        console.log('$scope.blogs :  ' + $scope.blogs);
        console.log('$scope.blog :  ' + $scope.blog);

        $scope.tempComment = '';
    }]);

})();