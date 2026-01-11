# Corporate Social Media App - REST API Documentation

## Base URL
```
http://localhost:8080
```

All endpoints are prefixed with `/api` unless otherwise specified.

---

## Table of Contents
1. [Authentication](#authentication)
2. [Users](#users)
3. [Posts](#posts)
4. [Comments](#comments)
5. [Likes](#likes)
6. [Connections (Follow/Unfollow)](#connections)

---

## Authentication

### Login
**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate a user and receive a JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response:** `200 OK`
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Headers:**
- `Set-Cookie: ACCESS_TOKEN=<jwt_token>; HttpOnly; Secure; SameSite=Lax; Path=/; Max-Age=900`

**Error Responses:**
- `401 Unauthorized` - Invalid credentials
- `404 Not Found` - User not found

---

## Users

### Register User
**Endpoint:** `POST /api/users/register`

**Description:** Register a new user with profile picture.

**Content-Type:** `multipart/form-data`

**Form Fields:**
- `username` (String, required) - Unique username
- `email` (String, required) - Unique email address
- `password` (String, required) - User password
- `fullName` (String, required) - Full name of the user
- `position` (String, required) - Job position (enum: JUNIOR, SENIOR, MANAGER, DIRECTOR, VP, CEO, CTO, CFO, COO)
- `department` (String, required) - Department (enum: ENGINEERING, MARKETING, SALES, HR, FINANCE, OPERATIONS, LEGAL, IT, CUSTOMER_SUPPORT, PRODUCT)
- `profilePicture` (File, required) - Profile picture image file

**Response:** `201 Created`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "profilePicture": "http://example.com/profile/john_doe.jpg",
  "position": "SENIOR",
  "department": "ENGINEERING",
  "isFollowing": false,
  "mutualConnectionsCount": 0
}
```

**Error Responses:**
- `409 Conflict` - Email or username already exists
- `400 Bad Request` - Validation error

---

### Get All Users
**Endpoint:** `GET /api/users`

**Description:** Retrieve a list of all users.

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  }
]
```

---

### Get User Summary
**Endpoint:** `GET /api/users/{userId}`

**Description:** Get basic user information.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "profilePicture": "http://example.com/profile/john_doe.jpg",
  "position": "SENIOR",
  "department": "ENGINEERING",
  "isFollowing": false,
  "mutualConnectionsCount": 0
}
```

**Error Responses:**
- `404 Not Found` - User not found

---

### Get User Profile
**Endpoint:** `GET /api/users/{userId}/profile`

**Description:** Get detailed user profile including follower/following counts.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "profilePicture": "http://example.com/profile/john_doe.jpg",
  "position": "SENIOR",
  "department": "ENGINEERING",
  "createdAt": "2024-01-15T10:30:00",
  "followerCount": 25,
  "followingCount": 30,
  "isFollowing": true
}
```

**Error Responses:**
- `404 Not Found` - User not found

---

### Search Users
**Endpoint:** `GET /api/users/search`

**Description:** Search for users by username.

**Query Parameters:**
- `query` (String, required) - Search term
- `page` (int, optional, default: 0) - Page number
- `size` (int, optional, default: 20) - Page size

**Example Request:**
```
GET /api/users/search?query=john&page=0&size=10
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 3
  }
]
```

---

### Update User
**Endpoint:** `PUT /api/users`

**Description:** Update user information.

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "userId": 1,
  "fullName": "John Updated Doe",
  "profilePicture": "http://example.com/new-profile.jpg",
  "position": "MANAGER",
  "department": "ENGINEERING"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Updated Doe",
  "profilePicture": "http://example.com/new-profile.jpg",
  "position": "MANAGER",
  "department": "ENGINEERING",
  "isFollowing": false,
  "mutualConnectionsCount": 0
}
```

**Error Responses:**
- `404 Not Found` - User not found
- `400 Bad Request` - Validation error

---

### Update Password
**Endpoint:** `PUT /api/users/{userId}/password`

**Description:** Update user password.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "newPassword": "newSecurePassword456"
}
```

**Response:** `204 No Content`

**Error Responses:**
- `404 Not Found` - User not found

---

### Get User Followers
**Endpoint:** `GET /api/users/{userId}/followers`

**Description:** Get list of users following the specified user.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 2,
      "username": "jane_smith",
      "fullName": "Jane Smith",
      "profilePicture": "http://example.com/profile/jane_smith.jpg",
      "position": "JUNIOR",
      "department": "MARKETING",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "followedAt": "2024-01-20T14:30:00",
    "isFollowingBack": true
  }
]
```

**Error Responses:**
- `404 Not Found` - User not found

---

### Get User Following
**Endpoint:** `GET /api/users/{userId}/following`

**Description:** Get list of users that the specified user is following.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 3,
      "username": "bob_jones",
      "fullName": "Bob Jones",
      "profilePicture": "http://example.com/profile/bob_jones.jpg",
      "position": "MANAGER",
      "department": "SALES",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "followingSince": "2024-01-18T09:15:00",
    "isFollower": false
  }
]
```

**Error Responses:**
- `404 Not Found` - User not found

---

## Posts

### Create Post (JSON)
**Endpoint:** `POST /api/posts`

**Description:** Create a new post without image.

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "content": "This is my new post content!",
  "user_db_Id": 1
}
```

**Response:** `201 Created`
```json
{
  "id": 10,
  "content": "This is my new post content!",
  "imageUrl": null,
  "createdAt": "2024-01-25T16:45:00",
  "updatedAt": "2024-01-25T16:45:00",
  "author": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "likeCount": 0,
  "commentCount": 0,
  "isLikedByCurrentUser": false,
  "comments": []
}
```

**Error Responses:**
- `400 Bad Request` - Content is empty
- `404 Not Found` - User not found

---

### Create Post with Image
**Endpoint:** `POST /api/posts/with-image`

**Description:** Create a new post with an image.

**Content-Type:** `multipart/form-data`

**Form Fields:**
- `content` (String, required) - Post content
- `user_db_Id` (Long, required) - ID of the post author
- `imageFile` (File, optional) - Image file to upload

**Response:** `201 Created`
```json
{
  "id": 11,
  "content": "Check out this amazing view!",
  "imageUrl": "http://example.com/post-images/image123.jpg",
  "createdAt": "2024-01-25T17:00:00",
  "updatedAt": "2024-01-25T17:00:00",
  "author": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "likeCount": 0,
  "commentCount": 0,
  "isLikedByCurrentUser": false,
  "comments": []
}
```

**Error Responses:**
- `400 Bad Request` - Content is empty or invalid image
- `404 Not Found` - User not found

---

### Get Post by ID
**Endpoint:** `GET /api/posts/{postId}`

**Description:** Retrieve a specific post with all its comments.

**Path Parameters:**
- `postId` (Long) - ID of the post

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/10?currentUserId=1
```

**Response:** `200 OK`
```json
{
  "id": 10,
  "content": "This is my new post content!",
  "imageUrl": null,
  "createdAt": "2024-01-25T16:45:00",
  "updatedAt": "2024-01-25T16:45:00",
  "author": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "likeCount": 5,
  "commentCount": 3,
  "isLikedByCurrentUser": true,
  "comments": [
    {
      "id": 1,
      "content": "Great post!",
      "createdAt": "2024-01-25T17:00:00",
      "author": {
        "id": 2,
        "username": "jane_smith",
        "fullName": "Jane Smith",
        "profilePicture": "http://example.com/profile/jane_smith.jpg",
        "position": "JUNIOR",
        "department": "MARKETING",
        "isFollowing": false,
        "mutualConnectionsCount": 0
      },
      "isCommentOwner": false
    }
  ]
}
```

**Error Responses:**
- `404 Not Found` - Post not found

---

### Get All Posts
**Endpoint:** `GET /api/posts`

**Description:** Retrieve all posts (public feed).

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts?currentUserId=1
```

**Response:** `200 OK`
```json
[
  {
    "id": 10,
    "content": "This is my new post content!",
    "imageUrl": null,
    "createdAt": "2024-01-25T16:45:00",
    "updatedAt": "2024-01-25T16:45:00",
    "author": {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe",
      "profilePicture": "http://example.com/profile/john_doe.jpg",
      "position": "SENIOR",
      "department": "ENGINEERING",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "likeCount": 5,
    "commentCount": 3,
    "isLikedByCurrentUser": true,
    "comments": []
  }
]
```

---

### Get User Posts
**Endpoint:** `GET /api/posts/user/{userId}`

**Description:** Retrieve all posts by a specific user.

**Path Parameters:**
- `userId` (Long) - ID of the user whose posts to retrieve

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/user/1?currentUserId=2
```

**Response:** `200 OK`
```json
[
  {
    "id": 10,
    "content": "This is my new post content!",
    "imageUrl": null,
    "createdAt": "2024-01-25T16:45:00",
    "updatedAt": "2024-01-25T16:45:00",
    "author": {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe",
      "profilePicture": "http://example.com/profile/john_doe.jpg",
      "position": "SENIOR",
      "department": "ENGINEERING",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "likeCount": 5,
    "commentCount": 3,
    "isLikedByCurrentUser": false,
    "comments": []
  }
]
```

**Error Responses:**
- `404 Not Found` - User not found

---

### Get Feed
**Endpoint:** `GET /api/posts/feed`

**Description:** Retrieve posts from users that the current user follows (plus their own posts).

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/feed?currentUserId=1
```

**Response:** `200 OK`
```json
[
  {
    "id": 10,
    "content": "This is my new post content!",
    "imageUrl": null,
    "createdAt": "2024-01-25T16:45:00",
    "updatedAt": "2024-01-25T16:45:00",
    "author": {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe",
      "profilePicture": "http://example.com/profile/john_doe.jpg",
      "position": "SENIOR",
      "department": "ENGINEERING",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "likeCount": 5,
    "commentCount": 3,
    "isLikedByCurrentUser": true,
    "comments": []
  }
]
```

---

### Update Post
**Endpoint:** `PUT /api/posts`

**Description:** Update an existing post (author only).

**Content-Type:** `application/json`

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Request Body:**
```json
{
  "post_db_id": 10,
  "content": "This is my updated post content!",
  "imageUrl": "http://example.com/new-image.jpg"
}
```

**Response:** `200 OK`
```json
{
  "id": 10,
  "content": "This is my updated post content!",
  "imageUrl": "http://example.com/new-image.jpg",
  "createdAt": "2024-01-25T16:45:00",
  "updatedAt": "2024-01-25T18:00:00",
  "author": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "likeCount": 5,
  "commentCount": 3,
  "isLikedByCurrentUser": true,
  "comments": []
}
```

**Error Responses:**
- `400 Bad Request` - Content is empty
- `403 Forbidden` - Not allowed to update this post
- `404 Not Found` - Post not found

---

### Delete Post
**Endpoint:** `DELETE /api/posts/{postId}`

**Description:** Delete a post (author only).

**Path Parameters:**
- `postId` (Long) - ID of the post to delete

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
DELETE /api/posts/10?currentUserId=1
```

**Response:** `204 No Content`

**Error Responses:**
- `403 Forbidden` - Not allowed to delete this post
- `404 Not Found` - Post not found

---

## Comments

### Create Comment
**Endpoint:** `POST /api/posts/{postId}/comments`

**Description:** Add a comment to a post.

**Path Parameters:**
- `postId` (Long) - ID of the post to comment on

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "postId": 10,
  "content": "Great post! I really enjoyed it."
}
```

**Note:** The `postId` in the body is optional as it's already in the URL path.

**Response:** `201 Created`
```json
{
  "id": 1,
  "content": "Great post! I really enjoyed it.",
  "createdAt": "2024-01-25T17:30:00",
  "author": {
    "id": 2,
    "username": "jane_smith",
    "fullName": "Jane Smith",
    "profilePicture": "http://example.com/profile/jane_smith.jpg",
    "position": "JUNIOR",
    "department": "MARKETING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "isCommentOwner": true
}
```

**Error Responses:**
- `400 Bad Request` - Content is empty
- `404 Not Found` - Post or user not found

---

### Get Post Comments
**Endpoint:** `GET /api/posts/{postId}/comments`

**Description:** Retrieve all comments for a specific post.

**Path Parameters:**
- `postId` (Long) - ID of the post

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/10/comments?currentUserId=1
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "content": "Great post! I really enjoyed it.",
    "createdAt": "2024-01-25T17:30:00",
    "author": {
      "id": 2,
      "username": "jane_smith",
      "fullName": "Jane Smith",
      "profilePicture": "http://example.com/profile/jane_smith.jpg",
      "position": "JUNIOR",
      "department": "MARKETING",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "isCommentOwner": false
  }
]
```

**Error Responses:**
- `404 Not Found` - Post not found

---

### Update Comment
**Endpoint:** `PUT /api/posts/comments`

**Description:** Update a comment (author only).

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "commentId": 1,
  "content": "Updated comment content"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "content": "Updated comment content",
  "createdAt": "2024-01-25T17:30:00",
  "author": {
    "id": 2,
    "username": "jane_smith",
    "fullName": "Jane Smith",
    "profilePicture": "http://example.com/profile/jane_smith.jpg",
    "position": "JUNIOR",
    "department": "MARKETING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "isCommentOwner": true
}
```

**Error Responses:**
- `400 Bad Request` - Content is empty
- `403 Forbidden` - Not allowed to update this comment
- `404 Not Found` - Comment not found

---

### Delete Comment
**Endpoint:** `DELETE /api/posts/comments/{commentId}`

**Description:** Delete a comment (author only).

**Path Parameters:**
- `commentId` (Long) - ID of the comment to delete

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
DELETE /api/posts/comments/1?currentUserId=2
```

**Response:** `204 No Content`

**Error Responses:**
- `403 Forbidden` - Not allowed to delete this comment
- `404 Not Found` - Comment not found

---

## Likes

### Like a Post
**Endpoint:** `POST /api/posts/{postId}/like`

**Description:** Add a like to a post.

**Path Parameters:**
- `postId` (Long) - ID of the post to like

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
POST /api/posts/10/like?currentUserId=1
```

**Response:** `201 Created`
```json
{
  "user": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "createdAt": "2024-01-25T18:00:00"
}
```

**Error Responses:**
- `409 Conflict` - Already liked this post
- `404 Not Found` - Post not found

---

### Unlike a Post
**Endpoint:** `DELETE /api/posts/{postId}/like`

**Description:** Remove a like from a post.

**Path Parameters:**
- `postId` (Long) - ID of the post to unlike

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
DELETE /api/posts/10/like?currentUserId=1
```

**Response:** `204 No Content`

**Error Responses:**
- `404 Not Found` - Like or post not found

---

### Get Post Likes
**Endpoint:** `GET /api/posts/{postId}/likes`

**Description:** Retrieve all users who liked a post.

**Path Parameters:**
- `postId` (Long) - ID of the post

**Example Request:**
```
GET /api/posts/10/likes
```

**Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe",
      "profilePicture": "http://example.com/profile/john_doe.jpg",
      "position": "SENIOR",
      "department": "ENGINEERING",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "createdAt": "2024-01-25T18:00:00"
  }
]
```

**Error Responses:**
- `404 Not Found` - Post not found

---

## Connections

### Follow User
**Endpoint:** `POST /api/connections/follow`

**Description:** Follow another user.

**Query Parameters:**
- `followerId` (Long, required) - ID of the user who wants to follow
- `followingId` (Long, required) - ID of the user to be followed

**Example Request:**
```
POST /api/connections/follow?followerId=1&followingId=2
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "follower": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://example.com/profile/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "following": {
    "id": 2,
    "username": "jane_smith",
    "fullName": "Jane Smith",
    "profilePicture": "http://example.com/profile/jane_smith.jpg",
    "position": "JUNIOR",
    "department": "MARKETING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "createdAt": "2024-01-25T19:00:00"
}
```

**Error Responses:**
- `400 Bad Request` - Cannot follow yourself
- `409 Conflict` - Already following this user
- `404 Not Found` - User not found

---

### Unfollow User
**Endpoint:** `DELETE /api/connections/unfollow`

**Description:** Unfollow a user.

**Query Parameters:**
- `followerId` (Long, required) - ID of the user who wants to unfollow
- `followingId` (Long, required) - ID of the user to be unfollowed

**Example Request:**
```
DELETE /api/connections/unfollow?followerId=1&followingId=2
```

**Response:** `204 No Content`

**Error Responses:**
- `404 Not Found` - Connection or user not found

---

### Get User Followers
**Endpoint:** `GET /api/connections/{userId}/followers`

**Description:** Get list of users following the specified user.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Example Request:**
```
GET /api/connections/1/followers
```

**Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 2,
      "username": "jane_smith",
      "fullName": "Jane Smith",
      "profilePicture": "http://example.com/profile/jane_smith.jpg",
      "position": "JUNIOR",
      "department": "MARKETING",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "followedAt": "2024-01-20T14:30:00",
    "isFollowingBack": true
  }
]
```

**Error Responses:**
- `404 Not Found` - User not found

---

### Get User Following
**Endpoint:** `GET /api/connections/{userId}/following`

**Description:** Get list of users that the specified user is following.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Example Request:**
```
GET /api/connections/1/following
```

**Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 3,
      "username": "bob_jones",
      "fullName": "Bob Jones",
      "profilePicture": "http://example.com/profile/bob_jones.jpg",
      "position": "MANAGER",
      "department": "SALES",
      "isFollowing": false,
      "mutualConnectionsCount": 0
    },
    "followingSince": "2024-01-18T09:15:00",
    "isFollower": false
  }
]
```

**Error Responses:**
- `404 Not Found` - User not found

---

## Error Response Format

All error responses follow this format:

```json
{
  "message": "Detailed error message",
  "errorCode": "ERROR_CODE_ENUM",
  "timestamp": "18:30:45"
}
```

### Common Error Codes
- `USER_NOT_FOUND` - User does not exist
- `USER_EMAIL_ALREADY_EXISTS` - Email is already registered
- `USER_USERNAME_ALREADY_EXISTS` - Username is already taken
- `POST_NOT_FOUND` - Post does not exist
- `POST_CONTENT_EMPTY` - Post content is required
- `POST_UPDATE_NOT_ALLOWED` - Not authorized to update post
- `POST_DELETE_NOT_ALLOWED` - Not authorized to delete post
- `COMMENT_NOT_FOUND` - Comment does not exist
- `COMMENT_CONTENT_EMPTY` - Comment content is required
- `COMMENT_DELETE_NOT_ALLOWED` - Not authorized to delete comment
- `LIKE_ALREADY_EXISTS` - Already liked this post
- `LIKE_NOT_FOUND` - Like does not exist
- `FOLLOW_ALREADY_EXISTS` - Already following this user
- `FOLLOW_SELF_NOT_ALLOWED` - Cannot follow yourself
- `INVALID_IMAGE` - Invalid image file
- `IMAGE_UPLOAD_ERROR` - Error uploading image
- `VALIDATION_ERROR` - Request validation failed
- `INTERNAL_SERVER_ERROR` - Unexpected server error

---

## Enums

### Position
```
JUNIOR, SENIOR, MANAGER, DIRECTOR, VP, CEO, CTO, CFO, COO
```

### Department
```
ENGINEERING, MARKETING, SALES, HR, FINANCE, OPERATIONS, LEGAL, IT, CUSTOMER_SUPPORT, PRODUCT
```

### Role (System Role)
```
USER, ADMIN, MODERATOR
```

---

## Notes

1. **Authentication**: Most endpoints require authentication. In production, use JWT tokens from the login response.

2. **CORS**: The API allows requests from all origins. In production, restrict this to specific domains.

3. **File Uploads**: Use `multipart/form-data` for endpoints accepting files (profile pictures, post images).

4. **Pagination**: Search endpoints support pagination with `page` and `size` query parameters.

5. **Current User**: Many endpoints require `currentUserId` as a query parameter. In production, this should be extracted from the JWT token automatically.

6. **Timestamps**: All timestamps are in ISO 8601 format (e.g., `2024-01-25T16:45:00`).

7. **Image URLs**: Image URLs are returned as complete URLs pointing to the storage location.

8. **Validation**: Request bodies are validated. Check error messages for specific validation requirements.

---

## Example Frontend Integration

### JavaScript/Fetch Example

```javascript
// Login
const login = async (username, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include', // Include cookies
    body: JSON.stringify({ username, password })
  });
  return await response.json();
};

// Create Post
const createPost = async (content, userId) => {
  const response = await fetch('http://localhost:8080/api/posts', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ content, user_db_Id: userId })
  });
  return await response.json();
};

// Upload Post with Image
const createPostWithImage = async (content, userId, imageFile) => {
  const formData = new FormData();
  formData.append('content', content);
  formData.append('user_db_Id', userId);
  formData.append('imageFile', imageFile);

  const response = await fetch('http://localhost:8080/api/posts/with-image', {
    method: 'POST',
    body: formData // Don't set Content-Type header, browser will set it with boundary
  });
  return await response.json();
};

// Follow User
const followUser = async (followerId, followingId) => {
  const response = await fetch(
    `http://localhost:8080/api/connections/follow?followerId=${followerId}&followingId=${followingId}`,
    { method: 'POST' }
  );
  return await response.json();
};
```

---

## Testing

Use tools like Postman, cURL, or any HTTP client to test the API endpoints.

### cURL Example

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"password123"}'

# Create Post
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"content":"Hello World!","user_db_Id":1}'

# Get Feed
curl -X GET "http://localhost:8080/api/posts/feed?currentUserId=1"
```

---

**Last Updated:** January 2026
**API Version:** 1.0

