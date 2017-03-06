
(function() {
	'use strict';

	var app = angular.module('cmadBlog', ["ngRoute"]);
	app.config(function($routeProvider) {
		$routeProvider
		.when("/", {
			templateUrl : "./partials/main.html",
			controller : 'MainPageController',
			controllerAs : 'mpc',
			resolve: {
                    blogsList : ['$route', 'BlogService', function ($route, BlogService) {
                        return BlogService.GetAllPosts();
                    }]
                }
		})
		.when("/signupForm", {
			templateUrl : "./partials/signup.html",
			controller : 'RegisterController',
			controllerAs : 'rc'
		})
		.when ("/login", {
			templateUrl : "./partials/login.html",
			controller : 'LoginController',
			controllerAs : 'lc'
		})
		.when ("/newPost", {
			templateUrl : "./partials/new-blog-post.html",
			controller : 'NewPostController',
			controllerAs : 'npc'
		})
		.when ("/singlePost/:postId", {
			templateUrl : "./partials/single-page-blog.html",
			controller : 'SinglePostController',
			controllerAs : 'spc',
			resolve: {
                singleBlog : ['$route', 'BlogService', function ($route, BlogService) {
                	console.log('postId : '+$route.current.params);
                    console.log('postId : '+JSON.stringify($route.current.params));
                    return BlogService.getPost($route.current.params);
               	}],
                blogsList : ['BlogService', function (BlogService) {
                   return BlogService.GetAllPosts();
                }]
            }
		})
		.when ("/searchResults/:searchString", {
			templateUrl : "./partials/search-results.html",
			controller : 'SearchResultController',
			controllerAs : 'src',
			resolve: {
                searchblogsList : ['$route', 'BlogService', function ($route, BlogService) {
                	console.log('postId : '+$route.current.params);
                    console.log('postId : '+JSON.stringify($route.current.params));
                    return BlogService.getPosts($route.current.params);
               	}]
         	}
		})
		.when ("/updateProfile", {
			templateUrl : "./partials/update-profile.html",
			controller : 'UpdateProfileController',
			controllerAs : 'upc'
		})
		.otherwise ({
			redirectTo : "./partials/main.html",
			controller : 'MainPageController',
			controllerAs : 'mpc',
			resolve: {
                    blogsList : ['$route', 'BlogService', function ($route, BlogService) {
                        return BlogService.GetAllPosts();
                    }]
                }
		});
	});

	app.run(function($rootScope) {
    	$rootScope.currentUser = {};
        $rootScope.authenticated = false;
	});

})();
