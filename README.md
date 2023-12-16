<h1 align="center"> Social MediaPlatform API </h1>

* Tech stack used: Spring Boot, Hibernate, MySQL, OOPs, Postman, and Java
>### Prerequisites
* ![MySql](https://img.shields.io/badge/DBMS-MYSQL%205.7%20or%20Higher-red)
 * ![SpringBoot](https://img.shields.io/badge/Framework-SpringBoot-green)


* ![Java](https://img.shields.io/badge/Language-Java%208%20or%20higher-yellow)

## Dependencies
The following dependencies are required to run the project:

* Spring Boot Dev Tools
* Spring Web
* Spring Data JPA
* MySQL Driver
* Lombok
* Validation
* spring security
* spring-boot-starter-mail
*  swagger

## Data Flow

1. The user at client side sends a request to the application through the API endpoints.
2. The API receives the request and sends it to the appropriate controller method.
3. The controller method makes a call to the method in service class.
4. The method in service class builds logic and retrieves or modifies data from the database, which is in turn given to controller class
5. The controller method returns a response to the API.
6. The API sends the response back to the user.
   
>## Description
The Social Media Platform API project aims to create a robust and user-friendly social networking application, providing individuals with a versatile and engaging platform for connecting and communicating.

## tables created
- User 
- UserProfile
- Post
- Friendship
- Comment
- PostLike
  
##Endpoints
## User Controller
>## Authentication and Authentication with email notification
Implements a secure authentication system, including options for users to log in using a username and password.
Email verification is encouraged for added security.


> http://localhost:8080/api/users/register
```JSON
  {
      "username": "ankit_sarwar",
      "name": "Ankit Sarwar",
      "email": "ankitsarwar039@gmail.com",
      "password": "ankit",
      "enabled": true,
      "phoneNumber": "91-9370219475",
      "verificationToken": "abcd1234",
      "roles": "ADMIN",
      "userProfile": {
        "bio": "This is Ankit's bio.",
        "profilePictureUrl": "https://example.com/ankit_profile.jpg"
      }
    }
```

>http://localhost:8080/api/users/logIn
```JSON
{
   "username" :"ankit_sarwar",
   "password" :"ankit"
}

```
### verifiy user based on token
>http://localhost:8080/api/users/verify?token={ give created token}
### logout user based on bearer token
>http://localhost:8080/api/users/logout?username=
### update user
>http://localhost:8080/api/users/update?email=ankitsarwar039@gmail.com&&token=

## Admin Controller
All endpoints require  authorization using bearer token
Only users with the ADMIN role have access to the controller's functionalities
- Admin can get All user ->http://localhost:8080/api/users/getAll
- get user by user id ->http://localhost:8080/api/admin/2
- give bluetick of existing user ->http://localhost:8080/api/admin/BlueTickUpdate/ankit_sarwar/true
- Admin can delete any user and also its post by PostID ->http://localhost:8080/api/admin/delete/user?username=abhi_sarwar
        ->http://localhost:8080/api/admin/delete/post?PostId=1
## Post Controller
Posting and Sharing:
- Allow users to create posts with text, images, and videos.
- Include options for users to comment on posts.
- Implement reposting functionalities.(reposting will add the post to their posts).

Privacy Settings:
- Provide customizable privacy settings for user posts (public, private).
- public(everyone will be able to see the posts).
- private(Only friends will be able to see the posts so only who is following and follower related to user that can only see the posts).

http://localhost:8080/api/posts/create?username=abhi_sarwar&token={User token}
```JSON
{
  "text": "Sample post text",
  "imageUrl": "https://example.com/image.jpg",
  "videoUrl": "https://example.com/video.mp4",
  "createdDate": "2023-12-14T12:00:00",
  "location": "Sample Location",
  "repostedByUsers": [
    // Reposted users here
  ],
  "privacySetting": "PRIVATE"
}
```
- http://localhost:8080/api/posts/delete/5?username=abhi_sarwar&token={token}
- http://localhost:8080/api/posts/repost/3?token={token}
- http://localhost:8080/api/posts/public?userName=onkar
- http://localhost:8080/api/posts/private?token={token}

## S3FileUploadController 
- first select extension and after getting a link and select put option and select binary 
- http://localhost:8080/generate-presigned-url?extension=jpg
  >## open link and verify in AWS account - https://s3.console.aws.amazon.com/s3/buckets/app.socialmedia.fileupload?region=ap-south-1&tab=objects

## FriendshipController
- send FriendRequest to another user -> http://localhost:8080/friendship/sendFriendRequest/1/2?token={token}
- every user can check FriendRequest status using token and create direct link output for accept or reject request -> http://localhost:8080/friendship/checkFriendRequest?token={}

## ActivityFeedController
- Activity Feed:
- Create a personalized activity feed showing posts from friends and followed users. ->http://localhost:8080/activity-feed/personalized?userId=1&token={}
- Include features like get information of likes, comments, and friend requests. ->http://localhost:8080/activity-feed/getNotification/3?token={userIdToken}
- check friend request and Accept or reject on selecting one of it's link ->http://localhost:8080/friendship/checkFriendRequest?token={}

## CommentController
  - only User can comment on posts using bearer token ->http://localhost:8080/api/comment/create
>## comment json
```JSON
{
    "commentBody": "This is a sample comment.",
    "post": {
        "id": 2
    },
    "text": "This is a sample post.",
    "imageUrl": "https://example.com/sample-image.jpg",
    "videoUrl": "https://example.com/sample-video.mp4",
    "createdDate": "2023-12-13T12:34:56",
    "location": "Sample Location",
    "user": {
        "id": 2
    }
}
```

## Analytics:
User Analytics:
- only user can like post ->http://localhost:8080/api/posts/like?token={}
```JSON
{
  "post": {
    "id": 5
  }
  ,
  "user": {
    "id": 3
  }
}
```
- Everyone can see likes on that post ->http://localhost:8080/api/posts/5/likeCount

<br>

## Technology Stack:
-Backend:
- Java(Spring Boot)
## DataBase Used
- SQL database


## Author

👤 **Ankit Sarwar**

* GitHub: [Ankit Sarwar](https://github.com/ankitSarwar)

* LinkedIn: [Ankit Sarwar](https://www.linkedin.com/in/ankit-sarwar/)

