(function() {
    'use strict';

    angular.module('cmadBlog')
    .controller('SinglePostController', ['BlogService',  '$timeout', '$window' , '$scope', 'blogsList', 'singleBlog', function (BlogService, $timeout, $window, $scope, blogsList, singleBlog) {
        var spc = this;
        spc.submitComment = submitComment;
        spc.tempComment = {};
        // console.log('blogsList : ' + blogsList);
        // console.log('singleBlog : ' + singleBlog);

        var tempbloglist = blogsList;
        $scope.blogs = [];
        $scope.blog = singleBlog;
        // spc.tempComment = {};
        console.log('$scope.blog : ' + $scope.blog);
        console.log('$scope.blog.post.blogTitle : ' + $scope.blog.post.blogTitle);
        
        spc.authenticated = $window.localStorage.getItem("authenticated");
        spc.user = $window.localStorage.getItem("currentUser");

        // console.log("spc.user before : " + spc.user);

        spc.user = JSON.parse(spc.user);

        var i = 0;
        for (;i<tempbloglist.length; i++) {
            $scope.blogs[i] = JSON.parse(tempbloglist[i]);
            // console.log('$scope.blogs [i] ID: ' + $scope.blogs[i].blogID);
        }
        $scope.blogComments = [];
        if ($scope.blog.post.blogComments) {
            console.log("blog.blogComments : " + $scope.blog.post.blogComments);
            console.log("$scope.blog.post.blogComments.length : " +$scope.blog.post.blogComments.length);
            // for (;i<$scope.blog.post.blogComments.length; i++) {
                // console.log("blog.blogComments[i] : " +$scope.blog.post.blogComments[i]);
                $scope.blogComments = $scope.blog.post.blogComments;
            // console.log('$scope.blogs [i] ID: ' + $scope.blogs[i].blogID);
            // }
            console.log("$scope.blogComments : " + JSON.stringify($scope.blogComments));
        }
        // console.log('$scope.blogs [0] ID: ' + $scope.blogs[0].blogID);
        // console.log('$scope.blogs :  ' + $scope.blogs);
        // console.log('$scope.blog :  ' + $scope.blog);


        function submitComment () {
            // {"commentId": commentId, "comment" : comment, "commentAuthor" : author, "avatarImgSrc" : avatarImgSrc, "commentedOn" : Date.now()}
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
                    // $scope.blog = response.post;
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