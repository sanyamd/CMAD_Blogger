        var npc = this;
        npc.createPost = createPost;
        $scope.loginError = "";

        $scope.tempTags = "";
        npc.user = $window.localStorage.getItem("currentUser");
        npc.authenticated = $window.localStorage.getItem("authenticated");

        console.log('npc.user : ' +npc.user);

function createPost() {
            if (npc.authenticated) {
                console.log("createPost entered");
                
                npc.dataLoading = true;
                var tags = $scope.tempTags.split(',');
                
                console.log("tags length : "+tags.length);
                console.log("tag 0 : "+tags[0]);
                
                npc.blog.blogID = BlogService.getAllPostsCount();
                npc.blog.blogTags = tags;
                npc.blog.blogLikes = 0;

                // To change this current user
                npc.blog.blogAuthor = {"name" : "Kapil","email" : "kapil@gmail.com","avatarImgSrc" : "./img/img_avatar4.png"};
                npc.blog.blogPostedOn = new Date();
                npc.blog.blogThumbnailLoc = "";
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