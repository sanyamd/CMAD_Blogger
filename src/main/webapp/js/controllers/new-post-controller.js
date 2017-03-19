(function() {
	'use strict';
	angular
    .module('cmadBlog')
    .controller('NewPostController', ['BlogService',  '$location' , '$scope', '$timeout', '$window', function (BlogService, $location, $scope, $timeout, $window) {
        
        var npc = this;
        npc.createPost = createPost;
        $scope.loginError = "";
        $scope.blogsCount = "0";

        (function initController() {
            // getting blogs count
            BlogService.getAllPostsCount().then(function(response) {
                    console.log('response.postsCount : '+response.postsCount);
                    $scope.blogsCount = "" + response.postsCount;
                });
        })();

        $scope.tempTags = "";
        npc.user = $window.localStorage.getItem("currentUser");
        npc.user = JSON.parse(npc.user);

        npc.authenticated = $window.localStorage.getItem("authenticated");

        console.log('npc.user : ' +npc.user);
        if (npc.user != null) {
            console.log("npc.user.userInfo.name : " + npc.user.userInfo.name);
            console.log("npc.user.avatarImgSrc : " + npc.user.userInfo.avatarImgSrc);
        }

        function createPost() {
            if (npc.authenticated) {
                console.log("createPost entered");
                
                npc.dataLoading = true;
                var tags = $scope.tempTags.split(',');
                
                console.log("tags length : "+tags.length);
                console.log("tag 0 : "+tags[0]);
                console.log("$scope.blogsCount : "+$scope.blogsCount);
                
                npc.blog.blogID = parseInt($scope.blogsCount)+1;
                console.log(npc.blog.blogID);
                npc.blog.blogTags = tags;
                npc.blog.blogLikes = 0;

                // To change this current user
                npc.blog.blogAuthor = {"name" : npc.user.userInfo.name,"email" : npc.user.email,"avatarImgSrc" : npc.user.userInfo.avatarImgSrc};
                npc.blog.blogPostedOn = new Date();
                npc.blog.blogThumbnailLoc = "./img/longform-banner.jpg";
                npc.blog.blogComments = [];

                BlogService.createPost(npc.blog).then(function (response) {
                    console.log(response);
                    console.log('got the response : response.writtenToDb : '+response.writtenToDb);
                    if (response.writtenToDb) {
                        $location.path('/');
                        console.log('blog writtenToDb');
                    } else {
                        console.log("user not authenticated");
                        $scope.loginError = "User not authenticated";
                        npc.dataLoading = false;
                        $location.path('/');
                        $timeout(function() {$window.location.reload();}, 100);
                    }
                });
            } else {
                console.log("user not authenticated");
                $scope.loginError = "User not authenticated";
                npc.dataLoading = false;
                $location.path('/');
                $timeout(function() {$window.location.reload();}, 100);
            }
        }
    }]);
})();
