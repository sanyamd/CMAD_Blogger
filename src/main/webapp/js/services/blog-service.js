(function () {
    'use strict';
     angular.module('cmadBlog').service('BlogService', function ($http) {
        var service = {};
 
        service.GetAllPosts = GetAllPosts;
        service.getPosts = getPosts;
        service.getPost = getPost;

        service.createPost = createPost;
        service.getAllPostsCount = getAllPostsCount;
 
        return service;
 
        function GetAllPosts() {
            console.log("GetAllPosts");
            return $http.get('online/blog/allPosts').then(handleSuccess, handleError);
        }

        function getAllPostsCount() {
            console.log("getAllPostsCount");
            return $http.get('online/blog/allPostsCount').then(handleSuccess, handleError);
        }
 
        function getPosts(searchStringJson) {
            console.log("getPosts : "+ searchStringJson.searchString);
            var searchString = searchStringJson.searchString;
            return $http.get('online/blog/search?searchString='+searchString).then(handleSuccess, handleError);
        }
 
        function getPost(blogIdJson) {
            console.log("getPost : "+blogIdJson.postId);
            var blogId = blogIdJson.postId;
            return $http.get('online/blog/post/'+blogId).then(handleSuccess, handleError);
        }

        function createPost(blog) {
            console.log("getPost");
            return $http.post('online/blog/addPost', blog).then(handleSuccess, handleError);
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

