package com.project.boostcamp.publiclibrary.api;

import com.project.boostcamp.publiclibrary.domain.AdminApplicationDTO;
import com.project.boostcamp.publiclibrary.domain.AdminEstimateDTO;
import com.project.boostcamp.publiclibrary.domain.AdminJoinDTO;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.domain.EstimateAddDTO;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.domain.TokenRefreshDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Hong Tae Joon on 2017-08-01.
 * 식당용 앱이 사용하는 통신 인터페이스
 */

public interface AdminService {
    /**
     * 로그인 요청
     * @param loginDTO 로그인 Id와 로그인 Type
     * @return 로그인 성공시 그대로 반환, 실패시 빈 객체
     */
    @POST("/admin/login")
    Call<LoginDTO> login(@Body LoginDTO loginDTO);

    /**
     * 회원가입 요청
     * @param adminJoinDTO 로그인 Id와 식당의 모든 정보를 포함
     * @return 회원가입 성공시 로그인 정보 반환, 실패시 빈 객체
     */
    @POST("/admin/join")
    Call<LoginDTO> join(@Body AdminJoinDTO adminJoinDTO);

    /**
     * 위치기반 근처 신청서 요청
     * @param latitude 위도
     * @param longitude 경도
     * @param distance 최대 거리
     * @return 근접 신청서 배열
     */
    @GET("/application/location")
    Call<List<AdminApplicationDTO>> get(@Query("latitude") double latitude, @Query("longitude") double longitude, @Query("distance") float distance);

    /**
     * 토큰 최신화 요청
     * @param id 로그인 Id
     * @param tokenRefreshDTO 토큰 정보
     * @return 성공시 1, 실패시 0
     */
    @PUT("/admin/{id}/token")
    Call<ResultIntDTO> updateToken(@Path("id") String id, @Body TokenRefreshDTO tokenRefreshDTO);

    /**
     * 신청서에 견적서를 추가 교청
     * @param id 신청서 아이디
     * @param estimateAddDTO 견적서 정보
     * @return 성공시 1 반환
     */
    @POST("/application/{id}/estimate")
    Call<ResultIntDTO> addEstimate(@Path("id") String id, @Body EstimateAddDTO estimateAddDTO);

    /**
     * 견적서 목록 요청
     * @param id 식당 로그인 아이디
     * @return 견적서 목록 반환
     */
    @GET("/admin/{id}/estimate")
    Call<ArrayList<AdminEstimateDTO>> getEstimate(@Path("id") String id);

    /**
     * 계약서 목록 요청
     * @param id 식당 로그인 아이디
     * @return 계약서 목록 반환
     */
    @GET("/admin/{id}/contact")
    Call<ArrayList<ContactDTO>> getContacts(@Path("id") String id);

    /**
     * 사진 업로드 요청
     * @param id 식당 로그인 아이디
     * @param type 식당 로그인 타입
     * @param file 이미지 파일
     * @return 성공시 1 반환
     */
    @Multipart
    @POST("/admin/{id}/image/{type}")
    Call<ResultIntDTO> setImage(@Path("id") String id, @Path("type") int type, @Part MultipartBody.Part file);

    /**
     * 신청서에 작성한 견적서 존재 유무 확인 요청
     * @param id 식당 로그인 아이디
     * @param appId 신청서 아이디
     * @return 존재할 경우 1 반환
     */
    @GET("/admin/{id}/estimate/{appId}")
    Call<ResultIntDTO> existEstimate(@Path("id") String id, @Path("appId") String appId);
}
