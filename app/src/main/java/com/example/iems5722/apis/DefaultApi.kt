package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Call
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.Result
import org.openapitools.client.models.User
import org.openapitools.client.models.UserVO

import okhttp3.MultipartBody

interface DefaultApi {
    /**
     * add
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param userId  (optional)
     * @param friendId  (optional)
     * @param createdAt  (optional)
     * @param friendName  (optional)
     * @return [Call]<[Result]>
     */
    @POST("friend/add")
    fun friendAddPost(@Query("id") id: kotlin.Int? = null, @Query("userId") userId: kotlin.Int? = null, @Query("friendId") friendId: kotlin.Int? = null, @Query("createdAt") createdAt: kotlin.String? = null, @Query("friendName") friendName: kotlin.String? = null): Call<Result>

    /**
     * all
     * 
     * Responses:
     *  - 200: 
     *
     * @param userId  (optional)
     * @return [Call]<[Result]>
     */
    @POST("friend/all")
    fun friendAllPost(@Query("userId") userId: kotlin.Int? = null): Call<Result>

    /**
     * delete
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @POST("friend/delete")
    fun friendDeletePost(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * detail
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @POST("friend/detail")
    fun friendDetailPost(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * list
     * 
     * Responses:
     *  - 200: 
     *
     * @param page 
     * @param size 
     * @return [Call]<[Result]>
     */
    @POST("friend/list")
    fun friendListPost(@Query("page") page: kotlin.Int, @Query("size") size: kotlin.Int): Call<Result>

    /**
     * update
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param userId  (optional)
     * @param friendId  (optional)
     * @param createdAt  (optional)
     * @param friendName  (optional)
     * @return [Call]<[Result]>
     */
    @POST("friend/update")
    fun friendUpdatePost(@Query("id") id: kotlin.Int? = null, @Query("userId") userId: kotlin.Int? = null, @Query("friendId") friendId: kotlin.Int? = null, @Query("createdAt") createdAt: kotlin.String? = null, @Query("friendName") friendName: kotlin.String? = null): Call<Result>

    /**
     * add
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param groupName  (optional)
     * @param groupAvatar  (optional)
     * @param createdAt  (optional)
     * @return [Call]<[Result]>
     */
    @POST("group/add")
    fun groupAddPost(@Query("id") id: kotlin.Int? = null, @Query("groupName") groupName: kotlin.String? = null, @Query("groupAvatar") groupAvatar: kotlin.String? = null, @Query("createdAt") createdAt: kotlin.String? = null): Call<Result>

    /**
     * delete
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @POST("group/delete")
    fun groupDeletePost(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * detail
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @POST("group/detail")
    fun groupDetailPost(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * getIncrementalMessages
     * 
     * Responses:
     *  - 200: 
     *
     * @param fromUserId 
     * @param groupId 
     * @param date 
     * @return [Call]<[Result]>
     */
    @POST("group_message/get_incrementally")
    fun groupMessageGetIncrementallyPost(@Query("fromUserId") fromUserId: kotlin.Int, @Query("groupId") groupId: kotlin.Int, @Query("date") date: kotlin.String): Call<Result>

    /**
     * send
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param fromUserId  (optional)
     * @param toGroupId  (optional)
     * @param messageContent  (optional)
     * @param sendTime  (optional)
     * @param messageType  (optional)
     * @return [Call]<[Result]>
     */
    @POST("group_message/send")
    fun groupMessageSendPost(@Query("_id") id: kotlin.String? = null, @Query("fromUserId") fromUserId: kotlin.Int? = null, @Query("toGroupId") toGroupId: kotlin.Int? = null, @Query("messageContent") messageContent: kotlin.String? = null, @Query("sendTime") sendTime: kotlin.String? = null, @Query("messageType") messageType: kotlin.Int? = null): Call<Result>

    /**
     * update
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param groupName  (optional)
     * @param groupAvatar  (optional)
     * @param createdAt  (optional)
     * @return [Call]<[Result]>
     */
    @POST("group/update")
    fun groupUpdatePost(@Query("id") id: kotlin.Int? = null, @Query("groupName") groupName: kotlin.String? = null, @Query("groupAvatar") groupAvatar: kotlin.String? = null, @Query("createdAt") createdAt: kotlin.String? = null): Call<Result>

    /**
     * policy
     * 
     * Responses:
     *  - 200: 
     *
     * @return [Call]<[Result]>
     */
    @POST("image/policy")
    fun imagePolicyPost(): Call<Result>

    /**
     * upload_avatar
     * 
     * Responses:
     *  - 200: 
     *
     * @param file 
     * @return [Call]<[Result]>
     */
    @Multipart
    @POST("image/upload_avatar")
    fun imageUploadAvatarPost(@Part file: MultipartBody.Part): Call<Result>

    /**
     * upload
     * 
     * Responses:
     *  - 200: 
     *
     * @param file 
     * @return [Call]<[Result]>
     */
    @Multipart
    @POST("image/upload")
    fun imageUploadPost(@Part file: MultipartBody.Part): Call<Result>

    /**
     * getIncrementalMessages
     * 
     * Responses:
     *  - 200: 
     *
     * @param fromUserId 
     * @param toUserId 
     * @param dateString 
     * @return [Call]<[Result]>
     */
    @POST("private_message/get_incrementally")
    fun privateMessageGetIncrementallyPost(@Query("fromUserId") fromUserId: kotlin.Int, @Query("toUserId") toUserId: kotlin.Int, @Query("dateString") dateString: kotlin.String): Call<Result>

    /**
     * send
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param fromUserId  (optional)
     * @param toUserId  (optional)
     * @param messageContent  (optional)
     * @param sendTime  (optional)
     * @param messageType  (optional)
     * @return [Call]<[Result]>
     */
    @POST("private_message/send")
    fun privateMessageSendPost(@Query("_id") id: kotlin.String? = null, @Query("fromUserId") fromUserId: kotlin.Int? = null, @Query("toUserId") toUserId: kotlin.Int? = null, @Query("messageContent") messageContent: kotlin.String? = null, @Query("sendTime") sendTime: kotlin.String? = null, @Query("messageType") messageType: kotlin.Int? = null): Call<Result>

    /**
     * delete
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @POST("user/delete")
    fun userDeletePost(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * detail
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @GET("user/detail")
    fun userDetailGet(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * getCurrentUser
     * 
     * Responses:
     *  - 200: 
     *
     * @return [Call]<[Result]>
     */
    @GET("user/get_current_user")
    fun userGetCurrentUserGet(): Call<Result>

    /**
     * addNewUser
     * 
     * Responses:
     *  - 200: 
     *
     * @param groupId 
     * @param requestBody  (optional)
     * @return [Call]<[Result]>
     */
    @POST("user/group/add_new_user")
    fun userGroupAddNewUserPost(@Query("groupId") groupId: kotlin.Int, @Body requestBody: kotlin.collections.List<kotlin.Int>? = null): Call<Result>

    /**
     * add
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param userId  (optional)
     * @param groupId  (optional)
     * @param createdAt  (optional)
     * @return [Call]<[Result]>
     */
    @POST("user/group/add")
    fun userGroupAddPost(@Query("id") id: kotlin.Int? = null, @Query("userId") userId: kotlin.Int? = null, @Query("groupId") groupId: kotlin.Int? = null, @Query("createdAt") createdAt: kotlin.String? = null): Call<Result>

    /**
     * createGroup
     * 
     * Responses:
     *  - 200: 
     *
     * @param requestBody  (optional)
     * @return [Call]<[Result]>
     */
    @POST("user/group/create")
    fun userGroupCreatePost(@Body requestBody: kotlin.collections.List<kotlin.Int>? = null): Call<Result>

    /**
     * delete
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @POST("user/group/delete")
    fun userGroupDeletePost(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * detail
     * 
     * Responses:
     *  - 200: 
     *
     * @param id 
     * @return [Call]<[Result]>
     */
    @POST("user/group/detail")
    fun userGroupDetailPost(@Query("id") id: kotlin.Int): Call<Result>

    /**
     * getGroupsByUser
     * 
     * Responses:
     *  - 200: 
     *
     * @param userId 
     * @return [Call]<[Result]>
     */
    @POST("user/group/get_groups_by_user")
    fun userGroupGetGroupsByUserPost(@Query("userId") userId: kotlin.Int): Call<Result>

    /**
     * getUsersByGroup
     * 
     * Responses:
     *  - 200: 
     *
     * @param groupId 
     * @return [Call]<[Result]>
     */
    @POST("user/group/get_users_by_group")
    fun userGroupGetUsersByGroupPost(@Query("groupId") groupId: kotlin.Int): Call<Result>

    /**
     * update
     * 
     * Responses:
     *  - 200: 
     *
     * @param id  (optional)
     * @param userId  (optional)
     * @param groupId  (optional)
     * @param createdAt  (optional)
     * @return [Call]<[Result]>
     */
    @POST("user/group/update")
    fun userGroupUpdatePost(@Query("id") id: kotlin.Int? = null, @Query("userId") userId: kotlin.Int? = null, @Query("groupId") groupId: kotlin.Int? = null, @Query("createdAt") createdAt: kotlin.String? = null): Call<Result>

    /**
     * list
     * 
     * Responses:
     *  - 200: 
     *
     * @param page 
     * @param size 
     * @return [Call]<[Result]>
     */
    @POST("user/list")
    fun userListPost(@Query("page") page: kotlin.Int, @Query("size") size: kotlin.Int): Call<Result>

    /**
     * register
     * 
     * Responses:
     *  - 200: 
     *
     * @param userVO  (optional)
     * @return [Call]<[Result]>
     */
    @POST("user/register")
    fun userRegisterPost(@Body userVO: UserVO? = null): Call<Result>

    /**
     * signIn
     * 
     * Responses:
     *  - 200: 
     *
     * @param userVO  (optional)
     * @return [Call]<[Result]>
     */
    @POST("user/sign_in")
    fun userSignInPost(@Body userVO: UserVO? = null): Call<Result>

    /**
     * signOut
     * 
     * Responses:
     *  - 200: 
     *
     * @return [Call]<[Result]>
     */
    @GET("user/sign_out")
    fun userSignOutGet(): Call<Result>

    /**
     * update
     * 
     * Responses:
     *  - 200: 
     *
     * @param user  (optional)
     * @return [Call]<[Result]>
     */
    @POST("user/update")
    fun userUpdatePost(@Body user: User? = null): Call<Result>

    /**
     * submit
     *
     * Responses:
     *  - 200:
     *
     * @param token
     * @return [Call]<[Result]>
     */
    @POST("token/submit")
    fun tokenSubmitPost(@Query("token") token: kotlin.String): Call<Result>
}
