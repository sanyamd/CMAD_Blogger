(function() {
    'use strict';

    angular.module('cmadBlog')
    .controller('SinglePostController', ['BlogService',  '$timeout', '$window' , '$scope', 'blogsList', 'singleBlog', function (BlogService, $timeout, $window, $scope, blogsList, singleBlog) {
        var spc = this;
        spc.submitComment = submitComment;
        spc.tempComment = {};

        var tempbloglist = blogsList;
        $scope.blogs = [];
        $scope.blog = singleBlog;
        console.log('$scope.blog : ' + $scope.blog);
        console.log('$scope.blog.post.blogTitle : ' + $scope.blog.post.blogTitle);
        
        spc.authenticated = $window.localStorage.getItem("authenticated");
        spc.user = $window.localStorage.getItem("currentUser");

        spc.user = JSON.parse(spc.user);

        var i = 0;
        for (;i<tempbloglist.length; i++) {
            $scope.blogs[i] = JSON.parse(tempbloglist[i]);
        }
        $scope.blogComments = [];
        if ($scope.blog.post.blogComments) {
            console.log("blog.blogComments : " + $scope.blog.post.blogComments);
            console.log("$scope.blog.post.blogComments.length : " +$scope.blog.post.blogComments.length);
            $scope.blogComments = $scope.blog.post.blogComments;
            console.log("$scope.blogComments : " + JSON.stringify($scope.blogComments));
        }


        function submitComment () {
            console.log("scope.blog.post.blogComments.length : " +$scope.blog.post.blogComments.length);
            spc.tempComment.commentId = $scope.blog.post.blogComments.length + 1;
            console.log("scope.tempComment.commentId : "+ spc.tempComment.commentId);
            spc.tempComment.commentAuthor = spc.user.userInfo.name;
            console.log("scope.tempComment.commentAuthor : "+ spc.tempComment.commentAuthor);
            spc.tempComment.avatarImgSrc = spc.user.userInfo.avatarImgSrc;
            console.log("scope.tempComment.avatarImgSrc : "+ spc.tempComment.avatarImgSrc);
            spc.tempComment.commentedOn = new Date();
            console.log("scope.tempComment.commentedOn : "+ spc.tempComment.commentedOn);
            console.log("scope.blog.post.blogID : "+ $scope.blog.post.blogID);

            console.log("spc.tempComment : "+ spc.tempComment);
            console.log("spc.tempComment.comment : "+spc.tempComment.comment);

            BlogService.addComment(spc.tempComment, $scope.blog.post.blogID).then(function(response) {
                console.log("writtenToDb : "+response.writtenToDb);
                console.log("response : "+response);
                if (response.writtenToDb) {
                    console.log("response after: "+response);
                    console.log("No need of refresh");
                    $scope.blogComments.push(spc.tempComment);
                    spc.tempComment=[];
                } else {
                    console.log("match not found");
                }
            });
        }
    }]);

})();