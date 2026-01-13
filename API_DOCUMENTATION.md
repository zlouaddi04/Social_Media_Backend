# Corporate Social Media App - REST API Documentation

## Base URL
```
http://localhost:8080
```

All endpoints are prefixed with `/api`.

---

## 🔐 Authentication

This API uses **JWT (JSON Web Token)** authentication via HTTP-only cookies.

### Authentication Flow
1. **Register** a new user via `POST /api/users/register` (no authentication required)
2. **Login** via `POST /api/auth/login` to receive a JWT token in an HTTP-only cookie
3. Include the cookie in all subsequent requests (browser handles this automatically)
4. The backend automatically extracts the current user from the JWT token

### Authentication Cookie
- **Name:** `ACCESS_TOKEN`
- **Type:** HTTP-only, Secure
- **SameSite:** Lax
- **Path:** /
- **Max-Age:** 900 seconds (15 minutes)

### Protected Endpoints
**Public endpoints** (no authentication required):
- `POST /api/auth/login`
- `POST /api/users/register`

**All other endpoints require authentication.**

### CORS Configuration
The API allows requests from all origins with credentials enabled for development purposes.

---

## 📋 Table of Contents
1. [Authentication](#authentication-endpoints)
2. [Users](#user-endpoints)
3. [Posts](#post-endpoints)
4. [Comments](#comment-endpoints)
5. [Likes](#like-endpoints)
6. [Connections (Follow/Unfollow)](#connection-endpoints)
7. [Error Response Format](#error-response-format)
8. [Error Codes Reference](#error-codes-reference)
9. [Enums Reference](#enums-reference)
10. [Frontend Integration Guide](#frontend-integration-guide)

---

## 📝 Important Response Structure Notes

### Post Responses
All post endpoints now return the **full author information** as a `UserSummaryDTO` object instead of just the `author_id`. This provides immediate access to author details without additional API calls.

**Author Object in Post Response:**
```json
{
  "author": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe"
  }
}
```

**Note:** The UserSummaryDTO contains basic user information (id, username, fullName). If you need additional details like profilePicture, position, or department, you can fetch the full user profile using the user ID.

This change applies to:
- Create Post responses
- Get Post responses
- Update Post responses
- Feed responses
- All list/collection post responses

### Comment Responses
Similarly, comments include the **full author information** as a `UserSummaryDTO` object.

---

## Authentication Endpoints

### Login
**Endpoint:** `POST /api/auth/login`

**Authentication Required:** No

**Description:** Authenticate a user and receive a JWT token.

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Validation:**
- `username`: Required, not blank
- `password`: Required, not blank

**Success Response:** `200 OK`
```json
{
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1
}
```

**Response Headers:**
```
Set-Cookie: ACCESS_TOKEN=<jwt_token>; HttpOnly; Secure; SameSite=Lax; Path=/; Max-Age=900
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 401 | `AUTH_INVALID_CREDENTIALS` | Invalid username or password |
| 404 | `USER_NOT_FOUND` | User does not exist |
| 400 | `VALIDATION_ERROR` | Request validation failed |

---

## User Endpoints

### Register User
**Endpoint:** `POST /api/users/register`

**Authentication Required:** No

**Description:** Register a new user with optional profile picture.

**Content-Type:** `multipart/form-data`

**Form Fields:**
- `username` (String, required) - Unique username (3-50 characters)
- `email` (String, required) - Valid email address
- `password` (String, required) - Password (minimum 6 characters)
- `fullName` (String, required) - Full name of the user
- `position` (String, required) - Job position (see Position enum)
- `department` (String, required) - Department (see Department enum)
- `profilePicture` (File, **optional**) - Profile picture image file

**Validation:**
- `username`: Required, 3-50 characters, unique
- `email`: Required, valid email format, unique
- `password`: Required, minimum 6 characters
- `fullName`: Required, not blank
- `position`: Required, must be valid Position enum value
- `department`: Required, must be valid Department enum value
- `profilePicture`: **Optional** - if not provided, user will have no profile picture

**Success Response:** `201 Created`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe_123456.jpg",
  "position": "SENIOR",
  "department": "ENGINEERING",
  "isFollowing": false,
  "mutualConnectionsCount": 0
}
```

**Note:** If no profile picture is provided, the `profilePicture` field will be `null`.

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 409 | `USER_EMAIL_ALREADY_EXISTS` | Email is already registered |
| 409 | `USER_USERNAME_ALREADY_EXISTS` | Username is already taken |
| 400 | `VALIDATION_ERROR` | Request validation failed |
| 400 | `INVALID_IMAGE` | Invalid image file format |
| 500 | `IMAGE_UPLOAD_ERROR` | Error uploading image |

---

### Get All Users
**Endpoint:** `GET /api/users`

**Authentication Required:** Yes

**Description:** Retrieve a list of all registered users.

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  }
]
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |
| 401 | `AUTH_TOKEN_EXPIRED` | JWT token has expired |
| 401 | `AUTH_TOKEN_INVALID` | Invalid JWT token |

---

### Get User Summary
**Endpoint:** `GET /api/users/{userId}`

**Authentication Required:** Yes

**Description:** Get basic user information.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
  "position": "SENIOR",
  "department": "ENGINEERING",
  "isFollowing": false,
  "mutualConnectionsCount": 0
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get User Profile
**Endpoint:** `GET /api/users/{userId}/profile`

**Authentication Required:** Yes

**Description:** Get detailed user profile including follower/following counts and posts.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Doe",
  "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
  "position": "SENIOR",
  "department": "ENGINEERING",
  "createdAt": "2024-01-15T10:30:00",
  "followerCount": 25,
  "followingCount": 30,
  "isFollowing": true
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Search Users
**Endpoint:** `GET /api/users/search`

**Authentication Required:** Yes

**Description:** Search for users by username.

**Query Parameters:**
- `query` (String, required) - Search term
- `page` (int, optional, default: 0) - Page number
- `size` (int, optional, default: 20) - Page size

**Example Request:**
```
GET /api/users/search?query=john&page=0&size=10
```

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 3
  }
]
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Update User
**Endpoint:** `PUT /api/users`

**Authentication Required:** Yes

**Description:** Update user information. Users can only update their own profile.

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "userId": 1,
  "fullName": "John Updated Doe",
  "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/new-profile.jpg",
  "position": "MANAGER",
  "department": "ENGINEERING"
}
```

**Validation:**
- `userId`: Required, not null
- `fullName`: Required, not blank
- `profilePicture`: Optional
- `position`: Required, valid Position enum value
- `department`: Required, valid Department enum value

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "username": "john_doe",
  "fullName": "John Updated Doe",
  "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/new-profile.jpg",
  "position": "MANAGER",
  "department": "ENGINEERING",
  "isFollowing": false,
  "mutualConnectionsCount": 0
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 400 | `VALIDATION_ERROR` | Request validation failed |
| 403 | `AUTH_FORBIDDEN` | Cannot update another user's profile |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Update Password
**Endpoint:** `PUT /api/users/{userId}/password`

**Authentication Required:** Yes

**Description:** Update user password. Users can only update their own password.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "newPassword": "newSecurePassword456"
}
```

**Validation:**
- `newPassword`: Required, minimum 6 characters

**Success Response:** `204 No Content`

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 400 | `VALIDATION_ERROR` | Password validation failed |
| 403 | `AUTH_FORBIDDEN` | Cannot update another user's password |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get User Followers
**Endpoint:** `GET /api/users/{userId}/followers`

**Authentication Required:** Yes

**Description:** Get list of users following the specified user.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Success Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 2,
      "username": "jane_smith",
      "fullName": "Jane Smith",
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/jane_smith.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get User Following
**Endpoint:** `GET /api/users/{userId}/following`

**Authentication Required:** Yes

**Description:** Get list of users that the specified user is following.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Success Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 3,
      "username": "bob_jones",
      "fullName": "Bob Jones",
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/bob_jones.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

## Post Endpoints

### Create Post (JSON)
**Endpoint:** `POST /api/posts`

**Authentication Required:** Yes

**Description:** Create a new post without image.

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "content": "This is my new post content!",
  "user_db_Id": 1,
  "imageUrl": null
}
```

**Validation:**
- `user_db_Id`: Required, not null
- `content`: Required, not blank
- `imageUrl`: Optional

**Success Response:** `201 Created`
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
    "fullName": "John Doe"
  },
  "likeCount": 0,
  "commentCount": 0,
  "isLikedByCurrentUser": false,
  "comments": []
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 400 | `POST_CONTENT_EMPTY` | Content is empty or blank |
| 400 | `VALIDATION_ERROR` | Request validation failed |
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Create Post with Image
**Endpoint:** `POST /api/posts/with-image`

**Authentication Required:** Yes

**Description:** Create a new post with an image.

**Content-Type:** `multipart/form-data`

**Form Fields:**
- `content` (String, required) - Post content
- `user_db_Id` (Long, required) - ID of the post author
- `imageFile` (File, optional) - Image file to upload

**Validation:**
- `content`: Required, not blank
- `user_db_Id`: Required, not null
- `imageFile`: Optional, must be valid image format

**Success Response:** `201 Created`
```json
{
  "id": 11,
  "content": "Check out this amazing view!",
  "imageUrl": "http://localhost:8080/uploads/images/post-pictures/post_123456.jpg",
  "createdAt": "2024-01-25T17:00:00",
  "updatedAt": "2024-01-25T17:00:00",
  "author": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING"
  },
  "likeCount": 0,
  "commentCount": 0,
  "isLikedByCurrentUser": false,
  "comments": []
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 400 | `POST_CONTENT_EMPTY` | Content is empty or blank |
| 400 | `INVALID_IMAGE` | Invalid image file format |
| 400 | `VALIDATION_ERROR` | Request validation failed |
| 404 | `USER_NOT_FOUND` | User does not exist |
| 500 | `IMAGE_UPLOAD_ERROR` | Error uploading image |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get Post by ID
**Endpoint:** `GET /api/posts/{postId}`

**Authentication Required:** Yes

**Description:** Retrieve a specific post with all its comments.

**Path Parameters:**
- `postId` (Long) - ID of the post

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/10?currentUserId=1
```

**Success Response:** `200 OK`
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
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING"
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
        "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/jane_smith.jpg",
        "position": "JUNIOR",
        "department": "MARKETING"
      },
      "isCommentOwner": false
    }
  ]
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get All Posts
**Endpoint:** `GET /api/posts`

**Authentication Required:** Yes

**Description:** Retrieve all posts (public feed).

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts?currentUserId=1
```

**Success Response:** `200 OK`
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
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
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

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get User Posts
**Endpoint:** `GET /api/posts/user/{userId}`

**Authentication Required:** Yes

**Description:** Retrieve all posts by a specific user.

**Path Parameters:**
- `userId` (Long) - ID of the user whose posts to retrieve

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/user/1?currentUserId=2
```

**Success Response:** `200 OK`
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
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get Feed
**Endpoint:** `GET /api/posts/feed`

**Authentication Required:** Yes

**Description:** Retrieve posts from users that the current user follows (plus their own posts).

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/feed?currentUserId=1
```

**Success Response:** `200 OK`
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
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
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

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Update Post
**Endpoint:** `PUT /api/posts`

**Authentication Required:** Yes

**Description:** Update an existing post. Only the author can update their post.

**Content-Type:** `application/json`

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Request Body:**
```json
{
  "post_db_id": 10,
  "content": "This is my updated post content!",
  "imageUrl": "http://localhost:8080/uploads/images/post-pictures/new-image.jpg"
}
```

**Validation:**
- `post_db_id`: Required, not null, minimum value 1
- `content`: Required, not blank
- `imageUrl`: Optional

**Success Response:** `200 OK`
```json
{
  "id": 10,
  "content": "This is my updated post content!",
  "imageUrl": "http://localhost:8080/uploads/images/post-pictures/new-image.jpg",
  "createdAt": "2024-01-25T16:45:00",
  "updatedAt": "2024-01-25T18:00:00",
  "author": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 400 | `POST_CONTENT_EMPTY` | Content is empty or blank |
| 400 | `VALIDATION_ERROR` | Request validation failed |
| 403 | `POST_UPDATE_NOT_ALLOWED` | Not allowed to update this post (not the author) |
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Delete Post
**Endpoint:** `DELETE /api/posts/{postId}`

**Authentication Required:** Yes

**Description:** Delete a post. Only the author can delete their post.

**Path Parameters:**
- `postId` (Long) - ID of the post to delete

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
DELETE /api/posts/10?currentUserId=1
```

**Success Response:** `204 No Content`

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 403 | `POST_DELETE_NOT_ALLOWED` | Not allowed to delete this post (not the author) |
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

## Comment Endpoints

### Create Comment
**Endpoint:** `POST /api/posts/{postId}/comments`

**Authentication Required:** Yes

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

**Validation:**
- `postId`: Required, not null
- `content`: Required, not blank

**Note:** The `postId` in the body is optional as it's already in the URL path.

**Success Response:** `201 Created`
```json
{
  "id": 1,
  "content": "Great post! I really enjoyed it.",
  "createdAt": "2024-01-25T17:30:00",
  "author": {
    "id": 2,
    "username": "jane_smith",
    "fullName": "Jane Smith",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/jane_smith.jpg",
    "position": "JUNIOR",
    "department": "MARKETING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "isCommentOwner": true
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 400 | `COMMENT_CONTENT_EMPTY` | Content is empty or blank |
| 400 | `VALIDATION_ERROR` | Request validation failed |
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get Post Comments
**Endpoint:** `GET /api/posts/{postId}/comments`

**Authentication Required:** Yes

**Description:** Retrieve all comments for a specific post.

**Path Parameters:**
- `postId` (Long) - ID of the post

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
GET /api/posts/10/comments?currentUserId=1
```

**Success Response:** `200 OK`
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
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/jane_smith.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Update Comment
**Endpoint:** `PUT /api/posts/comments`

**Authentication Required:** Yes

**Description:** Update a comment. Only the author can update their comment.

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

**Validation:**
- `commentId`: Required, not null
- `content`: Required, not blank

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "content": "Updated comment content",
  "createdAt": "2024-01-25T17:30:00",
  "author": {
    "id": 2,
    "username": "jane_smith",
    "fullName": "Jane Smith",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/jane_smith.jpg",
    "position": "JUNIOR",
    "department": "MARKETING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "isCommentOwner": true
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 400 | `COMMENT_CONTENT_EMPTY` | Content is empty or blank |
| 400 | `VALIDATION_ERROR` | Request validation failed |
| 403 | `COMMENT_DELETE_NOT_ALLOWED` | Not allowed to update this comment (not the author) |
| 404 | `COMMENT_NOT_FOUND` | Comment does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Delete Comment
**Endpoint:** `DELETE /api/posts/comments/{commentId}`

**Authentication Required:** Yes

**Description:** Delete a comment. Only the author can delete their comment.

**Path Parameters:**
- `commentId` (Long) - ID of the comment to delete

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
DELETE /api/posts/comments/1?currentUserId=2
```

**Success Response:** `204 No Content`

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 403 | `COMMENT_DELETE_NOT_ALLOWED` | Not allowed to delete this comment (not the author) |
| 404 | `COMMENT_NOT_FOUND` | Comment does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

## Like Endpoints

### Like a Post
**Endpoint:** `POST /api/posts/{postId}/like`

**Authentication Required:** Yes

**Description:** Add a like to a post.

**Path Parameters:**
- `postId` (Long) - ID of the post to like

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
POST /api/posts/10/like?currentUserId=1
```

**Success Response:** `201 Created`
```json
{
  "user": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "createdAt": "2024-01-25T18:00:00"
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 409 | `LIKE_ALREADY_EXISTS` | Already liked this post |
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Unlike a Post
**Endpoint:** `DELETE /api/posts/{postId}/like`

**Authentication Required:** Yes

**Description:** Remove a like from a post.

**Path Parameters:**
- `postId` (Long) - ID of the post to unlike

**Query Parameters:**
- `currentUserId` (Long, required) - ID of the currently logged-in user

**Example Request:**
```
DELETE /api/posts/10/like?currentUserId=1
```

**Success Response:** `204 No Content`

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `LIKE_NOT_FOUND` | Like does not exist |
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get Post Likes
**Endpoint:** `GET /api/posts/{postId}/likes`

**Authentication Required:** Yes

**Description:** Retrieve all users who liked a post.

**Path Parameters:**
- `postId` (Long) - ID of the post

**Example Request:**
```
GET /api/posts/10/likes
```

**Success Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 1,
      "username": "john_doe",
      "fullName": "John Doe",
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `POST_NOT_FOUND` | Post does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

## Connection Endpoints

### Follow User
**Endpoint:** `POST /api/connections/follow`

**Authentication Required:** Yes

**Description:** Follow another user. Users cannot follow themselves.

**Query Parameters:**
- `followerId` (Long, required) - ID of the user who wants to follow
- `followingId` (Long, required) - ID of the user to be followed

**Example Request:**
```
POST /api/connections/follow?followerId=1&followingId=2
```

**Success Response:** `201 Created`
```json
{
  "id": 1,
  "follower": {
    "id": 1,
    "username": "john_doe",
    "fullName": "John Doe",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/john_doe.jpg",
    "position": "SENIOR",
    "department": "ENGINEERING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "following": {
    "id": 2,
    "username": "jane_smith",
    "fullName": "Jane Smith",
    "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/jane_smith.jpg",
    "position": "JUNIOR",
    "department": "MARKETING",
    "isFollowing": false,
    "mutualConnectionsCount": 0
  },
  "createdAt": "2024-01-25T19:00:00"
}
```

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 400 | `FOLLOW_SELF_NOT_ALLOWED` | Cannot follow yourself |
| 409 | `FOLLOW_ALREADY_EXISTS` | Already following this user |
| 404 | `USER_NOT_FOUND` | Follower or following user does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Unfollow User
**Endpoint:** `DELETE /api/connections/unfollow`

**Authentication Required:** Yes

**Description:** Unfollow a user.

**Query Parameters:**
- `followerId` (Long, required) - ID of the user who wants to unfollow
- `followingId` (Long, required) - ID of the user to be unfollowed

**Example Request:**
```
DELETE /api/connections/unfollow?followerId=1&followingId=2
```

**Success Response:** `204 No Content`

**Error Responses:**

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `RESOURCE_NOT_FOUND` | Connection does not exist |
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get User Followers
**Endpoint:** `GET /api/connections/{userId}/followers`

**Authentication Required:** Yes

**Description:** Get list of users following the specified user.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Example Request:**
```
GET /api/connections/1/followers
```

**Success Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 2,
      "username": "jane_smith",
      "fullName": "Jane Smith",
      "profilePicture": "http://localhost:8080/uploads/images/profile-pictures/jane_smith.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

### Get User Following
**Endpoint:** `GET /api/connections/{userId}/following`

**Authentication Required:** Yes

**Description:** Get list of users that the specified user is following.

**Path Parameters:**
- `userId` (Long) - ID of the user

**Example Request:**
```
GET /api/connections/1/following
```

**Success Response:** `200 OK`
```json
[
  {
    "user": {
      "id": 3,
      "username": "bob_jones",
      "fullName": "Bob Jones",
      "profilePicture": "http://localhost:8080/uploads/images/post-pictures/bob_jones.jpg",
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

| HTTP Status | Error Code | Description |
|-------------|-----------|-------------|
| 404 | `USER_NOT_FOUND` | User does not exist |
| 401 | `AUTH_UNAUTHORIZED` | No authenticated user found |

---

## Error Response Format

All error responses follow this standardized format:

```json
{
  "message": "Detailed error message explaining what went wrong",
  "errorCode": "ERROR_CODE_ENUM_VALUE",
  "timestamp": "18:30:45"
}
```

### Error Response Fields
- `message` (String): Human-readable description of the error
- `errorCode` (String): Enum code for programmatic error handling
- `timestamp` (String): Time when the error occurred (HH:mm:ss format)

---

## Error Codes Reference

### General Errors

| Error Code | HTTP Status | Description | Context |
|-----------|-------------|-------------|---------|
| `INVALID_REQUEST` | 400 | The request is malformed or contains invalid data | Generic validation errors |
| `VALIDATION_ERROR` | 400 | Request validation failed | DTO validation failures, invalid input |
| `DB_VALIDATION_ERROR` | 400 | Database constraint violation | Unique constraint violations, data integrity issues |
| `RESOURCE_NOT_FOUND` | 404 | The requested resource does not exist | Generic resource not found |
| `OPERATION_NOT_ALLOWED` | 403 | The operation is not permitted | Generic forbidden operation |
| `INTERNAL_SERVER_ERROR` | 500 | An unexpected error occurred | Unhandled exceptions |

### Authentication & Authorization Errors

| Error Code | HTTP Status | Description | Context |
|-----------|-------------|-------------|---------|
| `AUTH_INVALID_CREDENTIALS` | 401 | Invalid username or password | Login with wrong credentials |
| `AUTH_UNAUTHORIZED` | 401 | No authenticated user found | Missing or invalid JWT token |
| `AUTH_FORBIDDEN` | 403 | Access to this resource is forbidden | Insufficient permissions |
| `AUTH_TOKEN_EXPIRED` | 401 | JWT token has expired | Token expiration (15 minutes) |
| `AUTH_TOKEN_INVALID` | 401 | Invalid JWT token | Malformed token, wrong signature, tampered token |

### User Errors

| Error Code | HTTP Status | Description | Context |
|-----------|-------------|-------------|---------|
| `USER_NOT_FOUND` | 404 | User does not exist | Accessing non-existent user by ID or username |
| `USER_ALREADY_EXISTS` | 409 | User already exists | Generic user duplication |
| `USER_EMAIL_ALREADY_EXISTS` | 409 | Email is already registered | Registration with duplicate email |
| `USER_USERNAME_ALREADY_EXISTS` | 409 | Username is already taken | Registration with duplicate username |

### Post Errors

| Error Code | HTTP Status | Description | Context |
|-----------|-------------|-------------|---------|
| `POST_NOT_FOUND` | 404 | Post does not exist | Accessing non-existent post by ID |
| `POST_CONTENT_EMPTY` | 400 | Post content is required | Creating/updating post with empty content |
| `POST_UPDATE_NOT_ALLOWED` | 403 | Not authorized to update this post | Attempting to update another user's post |
| `POST_DELETE_NOT_ALLOWED` | 403 | Not authorized to delete this post | Attempting to delete another user's post |

### Comment Errors

| Error Code | HTTP Status | Description | Context |
|-----------|-------------|-------------|---------|
| `COMMENT_NOT_FOUND` | 404 | Comment does not exist | Accessing non-existent comment by ID |
| `COMMENT_CONTENT_EMPTY` | 400 | Comment content is required | Creating/updating comment with empty content |
| `COMMENT_DELETE_NOT_ALLOWED` | 403 | Not authorized to delete this comment | Attempting to delete another user's comment |

### Interaction Errors

| Error Code | HTTP Status | Description | Context |
|-----------|-------------|-------------|---------|
| `LIKE_ALREADY_EXISTS` | 409 | Already liked this post | Attempting to like a post that's already liked |
| `LIKE_NOT_FOUND` | 404 | Like does not exist | Attempting to unlike a post that wasn't liked |
| `FOLLOW_ALREADY_EXISTS` | 409 | Already following this user | Attempting to follow a user already being followed |
| `FOLLOW_SELF_NOT_ALLOWED` | 400 | Cannot follow yourself | Attempting to follow your own account |

### Storage Errors

| Error Code | HTTP Status | Description | Context |
|-----------|-------------|-------------|---------|
| `INVALID_IMAGE` | 400 | Invalid image file | Uploading non-image file or corrupted image |
| `IMAGE_UPLOAD_ERROR` | 500 | Error uploading image | File system or cloud storage failure |

---

## Enums Reference

### Position Enum
Available job positions:

```
JUNIOR
SENIOR
MANAGER
DIRECTOR
VP
CEO
CTO
CFO
COO
```

**Usage in requests:**
```json
{
  "position": "SENIOR"
}
```

---

### Department Enum
Available departments:

```
ENGINEERING
MARKETING
SALES
HR
FINANCE
OPERATIONS
LEGAL
IT
CUSTOMER_SUPPORT
PRODUCT
```

**Usage in requests:**
```json
{
  "department": "ENGINEERING"
}
```

---

## Frontend Integration Guide

### Authentication Flow

#### 1. User Registration
```javascript
const registerUser = async (formData) => {
  const data = new FormData();
  data.append('username', formData.username);
  data.append('email', formData.email);
  data.append('password', formData.password);
  data.append('fullName', formData.fullName);
  data.append('position', formData.position); // e.g., "SENIOR"
  data.append('department', formData.department); // e.g., "ENGINEERING"
  
  // Profile picture is optional
  if (formData.profilePictureFile) {
    data.append('profilePicture', formData.profilePictureFile);
  }

  const response = await fetch('http://localhost:8080/api/users/register', {
    method: 'POST',
    body: data
    // Don't set Content-Type header - browser will set it with boundary
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message);
  }

  return await response.json();
};
```

#### 2. User Login
```javascript
const login = async (username, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include', // IMPORTANT: Include cookies in requests
    body: JSON.stringify({ username, password })
  });

  if (!response.ok) {
    const error = await response.json();
    // error.errorCode will be "AUTH_INVALID_CREDENTIALS" or "USER_NOT_FOUND"
    throw new Error(error.message);
  }

  const data = await response.json();
  // data contains: { message, token, userId }
  
  // Store userId in localStorage or state management for use in API calls
  localStorage.setItem('userId', data.userId);
  
  return data;
};
```

#### 3. Making Authenticated Requests
```javascript
// After login, all requests must include credentials
const getUserProfile = async (userId) => {
  const response = await fetch(`http://localhost:8080/api/users/${userId}/profile?currentUserId=${currentUserId}`, {
    method: 'GET',
    credentials: 'include', // IMPORTANT: Include JWT cookie
  });

  if (!response.ok) {
    const error = await response.json();
    if (error.errorCode === 'AUTH_TOKEN_EXPIRED') {
      // Redirect to login page
      window.location.href = '/login';
    }
    throw new Error(error.message);
  }

  return await response.json();
};
```

### Error Handling

```javascript
const handleApiError = (error) => {
  switch (error.errorCode) {
    case 'AUTH_UNAUTHORIZED':
    case 'AUTH_TOKEN_EXPIRED':
      // Redirect to login
      window.location.href = '/login';
      break;
    
    case 'AUTH_INVALID_CREDENTIALS':
      alert('Invalid username or password');
      break;
    
    case 'USER_EMAIL_ALREADY_EXISTS':
      alert('This email is already registered');
      break;
    
    case 'VALIDATION_ERROR':
      // Show validation errors
      console.error('Validation failed:', error.message);
      break;
    
    case 'POST_UPDATE_NOT_ALLOWED':
    case 'COMMENT_DELETE_NOT_ALLOWED':
      alert('You do not have permission to perform this action');
      break;
    
    default:
      alert('An error occurred: ' + error.message);
  }
};

// Usage
try {
  const result = await createPost(postData);
  console.log('Post created:', result);
} catch (error) {
  handleApiError(error);
}
```

### Complete Examples

#### Create Post without Image
```javascript
const createPost = async (content, userId) => {
  const response = await fetch('http://localhost:8080/api/posts', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify({
      content: content,
      user_db_Id: userId,
      imageUrl: null
    })
  });

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return await response.json();
};
```

#### Create Post with Image
```javascript
const createPostWithImage = async (content, userId, imageFile) => {
  const formData = new FormData();
  formData.append('content', content);
  formData.append('user_db_Id', userId);
  formData.append('imageFile', imageFile);

  const response = await fetch('http://localhost:8080/api/posts/with-image', {
    method: 'POST',
    credentials: 'include',
    body: formData
  });

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return await response.json();
};
```

#### Like/Unlike Post
```javascript
const toggleLike = async (postId, currentUserId, isLiked) => {
  const method = isLiked ? 'DELETE' : 'POST';
  const response = await fetch(
    `http://localhost:8080/api/posts/${postId}/like?currentUserId=${currentUserId}`,
    {
      method: method,
      credentials: 'include'
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  // DELETE returns 204 No Content
  return method === 'POST' ? await response.json() : null;
};
```

#### Follow/Unfollow User
```javascript
const toggleFollow = async (followerId, followingId, isFollowing) => {
  const method = isFollowing ? 'DELETE' : 'POST';
  const endpoint = isFollowing ? 'unfollow' : 'follow';
  
  const response = await fetch(
    `http://localhost:8080/api/connections/${endpoint}?followerId=${followerId}&followingId=${followingId}`,
    {
      method: method,
      credentials: 'include'
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return method === 'POST' ? await response.json() : null;
};
```

#### Get Feed Posts
```javascript
const getFeed = async (currentUserId) => {
  const response = await fetch(
    `http://localhost:8080/api/posts/feed?currentUserId=${currentUserId}`,
    {
      method: 'GET',
      credentials: 'include'
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return await response.json();
};
```

#### Add Comment
```javascript
const addComment = async (postId, content, currentUserId) => {
  const response = await fetch(
    `http://localhost:8080/api/posts/${postId}/comments?currentUserId=${currentUserId}`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        postId: postId,
        content: content
      })
    }
  );

  if (!response.ok) {
    const error = await response.json();
    throw error;
  }

  return await response.json();
};
```

### Validation Requirements

When building forms, ensure the following validation on the frontend:

#### Registration Form
- **Username**: 3-50 characters, required
- **Email**: Valid email format, required
- **Password**: Minimum 6 characters, required
- **Full Name**: Required, not blank
- **Position**: Required, must be one of the Position enum values
- **Department**: Required, must be one of the Department enum values
- **Profile Picture**: **Optional** - image file if provided

#### Post Creation
- **Content**: Required, not blank
- **Image**: Optional, must be valid image format if provided

#### Comment Creation
- **Content**: Required, not blank

### Best Practices

1. **Always include `credentials: 'include'`** in fetch requests to send JWT cookies
2. **Handle 401 errors** by redirecting to login page
3. **Use the `errorCode` field** for programmatic error handling
4. **Don't store JWT tokens** in localStorage - they're in HTTP-only cookies
5. **Validate forms on the frontend** to match backend validation requirements
6. **Show user-friendly error messages** based on error codes
7. **Handle token expiration** gracefully by auto-redirecting to login

---

## Testing with cURL

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","password":"password123"}' \
  -c cookies.txt

# Response:
# {
#   "message": "Login successful",
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   "userId": 1
# }
```

### Create Post (using saved cookies)
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{"content":"Hello World!","user_db_Id":1,"imageUrl":null}'
```

### Get Feed
```bash
curl -X GET "http://localhost:8080/api/posts/feed?currentUserId=1" \
  -b cookies.txt
```

### Follow User
```bash
curl -X POST "http://localhost:8080/api/connections/follow?followerId=1&followingId=2" \
  -b cookies.txt
```

---

## Postman Configuration

### Setting up Postman

1. **Create a new collection** named "Corporate Social Media API"
2. **Set base URL** as a collection variable: `{{baseUrl}}` = `http://localhost:8080`
3. **Enable cookie jar** in Postman settings to automatically handle JWT cookies
4. **Create environment** with variables:
   - `baseUrl`: `http://localhost:8080`
   - `currentUserId`: (set after login)

### Test Sequence

1. Register User → Returns user data
2. Login → Cookies automatically saved
3. Create Post → Uses saved cookie
4. Get Feed → Uses saved cookie
5. Like Post → Uses saved cookie

---

**Last Updated:** January 13, 2026  
**API Version:** 1.0  
**Author:** Corporate Social Media Team

