package com.project.boostcamp.publiclibrary.api;

import com.project.boostcamp.publiclibrary.domain.AdminDTO;
import com.project.boostcamp.publiclibrary.domain.AdminDetailDTO;
import com.project.boostcamp.publiclibrary.domain.ClientApplicationDTO;
import com.project.boostcamp.publiclibrary.domain.ClientDTO;
import com.project.boostcamp.publiclibrary.domain.ClientEstimateDTO;
import com.project.boostcamp.publiclibrary.domain.ClientJoinDTO;
import com.project.boostcamp.publiclibrary.domain.ContactAddDTO;
import com.project.boostcamp.publiclibrary.domain.ContactDTO;
import com.project.boostcamp.publiclibrary.domain.EventDTO;
import com.project.boostcamp.publiclibrary.domain.LoginDTO;
import com.project.boostcamp.publiclibrary.domain.NearAdminDTO;
import com.project.boostcamp.publiclibrary.domain.NewAdminDTO;
import com.project.boostcamp.publiclibrary.domain.ResultIntDTO;
import com.project.boostcamp.publiclibrary.domain.ResultStringDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewAddDTO;
import com.project.boostcamp.publiclibrary.domain.ReviewDTO;
import com.project.boostcamp.publiclibrary.domain.TokenRefreshDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Hong Tae Joon on 2017-08-01.
 * 고객용 앱에서 사용하는 서버 통신 인터페이스
 */

public interface ClientService {
    /**
     * 로그인 요청
     * @param loginDTO 로그인 Id와 로그인 Type
     * @return 성공시 로그인 정보, 실패시 빈 객체
     */
    @POST("/client/login")
    Call<LoginDTO> login(@Body LoginDTO loginDTO);

    /**
     * 회원가입 요청
     * @param clientJoinDTO 로그인 Id와 고객 정보
     * @return 성공시 로그인 정보, 실패시 빈 객체
     */
    @POST("/client/join")
    Call<LoginDTO> join(@Body ClientJoinDTO clientJoinDTO);

    @GET("/client/{id}/information")
    Call<ClientDTO> getUserInformation(@Path("id") String id, @Query("type") int type);

    /**
     * 현재 작성된 신청서 요청
     * @param id 로그인 Id
     * @return 신청서 데이터
     */
    @GET("/client/{id}/application")
    Call<ClientApplicationDTO> getApplication(@Path("id") String id);

    /**
     * 현재 작성한 신청서 등록 요청
     * @param id 로그인 id
     * @param ClientApplicationDTO 신청서 데이터
     * @return 성공시 신청서 id 반환
     */
    @POST("/client/{id}/application")
    Call<ResultStringDTO> setApplication(@Path("id") String id, @Body ClientApplicationDTO ClientApplicationDTO);

    /**
     * 신청서 취소 요청
     * @param id 신청서 id
     * @return 성공시 1 반환
     */
    @POST("/application/{id}/cancel")
    Call<ResultIntDTO> cancelApplication(@Path("id") String id);

    /**
     * 토큰 최신화 요청
     * @param id 로그인 id
     * @param tokenRefreshDTO 전달 인자값
     * @return 성공시 1 반환
     */
    @PUT("/client/{id}/token")
    Call<ResultIntDTO> updateToken(@Path("id") String id, @Body TokenRefreshDTO tokenRefreshDTO);

    /**
     * 견적서 목록 요청
     * @param id 신청서 아이디
     * @return 견적서 목록
     */
    @GET("/client/{id}/estimate")
    Call<ArrayList<ClientEstimateDTO>> getEstimateList(@Path("id") String id);

    /**
     * 계약 요청
     * @param contactAddDTO 신청서 아이디와 견적서 아이디를 포함한다
     * @return 성공시 1 반환
     */
    @POST("/contact/add")
    Call<ResultIntDTO> addContact(@Body ContactAddDTO contactAddDTO);

    /**
     * 계약 목록 요청
     * @param id 고객 로그인 아이디
     * @return 계약 목록
     */
    @GET("/client/{id}/contact")
    Call<ArrayList<ContactDTO>> getContacts(@Path("id") String id);

    /**
     * 이벤트 목록 요청
     * @return
     */
    @GET("/event")
    Call<ArrayList<EventDTO>> getEvents();

    /**
     * 근접 식당 목록 요청
     */
    @GET("/nearAdmin/{lat}/{lng}")
    Call<ArrayList<NearAdminDTO>> getNearAdmins(@Path("lat") double lat, @Path("lng") double lng);

    /**
     * 근접 식당 리뷰 목록 요청
     */
    @GET("/nearReview/{lat}/{lng}")
    Call<ArrayList<ReviewDTO>> getNearReviews(@Path("lat") double lat, @Path("lng") double lng);

    /**
     * 식당 리뷰 추가 요청
     */
    @POST("/admin/{id}/review")
    Call<ResultIntDTO> addReview(@Path("id") String adminId, @Body ReviewAddDTO reviewAddDTO);

    /**
     * 새로운 식당 목록 요청
     */
    @GET("/admin/new")
    Call<ArrayList<NewAdminDTO>> getNewAdmins();

    /**
     * 식당 정보 요청
     * @param id 식당 아이디
     * @param type 식당 아이디 타입
     * @return 식당 정보
     */
    @GET("/admin/{id}/information")
    Call<AdminDTO> getAdminInformation(@Path("id") String id, @Query("type") int type);

    /**
     *
     */
    @GET("/admin/{id}/review")
    Call<ArrayList<ReviewDTO>> getAdminReviews(@Path("id") String id, @Query("type") int type);
}
