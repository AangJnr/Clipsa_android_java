package com.clipsa.data.network;



import com.cowtribe.cowtribeapps.data.db.entity.Campaign;
import com.cowtribe.cowtribeapps.data.db.entity.Country;
import com.cowtribe.cowtribeapps.data.db.entity.Farmer;
import com.cowtribe.cowtribeapps.data.db.entity.Form;
import com.cowtribe.cowtribeapps.data.db.entity.User;
import com.cowtribe.cowtribeapps.data.network.model.Data;
import com.cowtribe.cowtribeapps.data.network.model.LoginRequest;
import com.cowtribe.cowtribeapps.data.network.model.RegistrationResponse;
import com.cowtribe.cowtribeapps.data.network.model.Response;
import com.cowtribe.cowtribeapps.data.network.model.SearchResponse;
import com.cowtribe.cowtribeapps.data.network.model.UploadResponse;
import com.cowtribe.cowtribeapps.data.network.model.UserAndData;
import com.cowtribe.cowtribeapps.utilities.AppConstants;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by AangJnr on 05, December, 2018 @ 4:13 PM
 * Work Mail cibrahim@grameenfoundation.org
 * Personal mail aang.jnr@gmail.com
 */

public interface FdpApi {

    @POST(AppConstants.API_VERSION + "login")
    Single<Response> makeLoginCall(@Body LoginRequest.ServerLoginRequest loginRequest);

    @GET(AppConstants.API_VERSION + "data")
    Single<UserAndData> getUserAndData(@Query("token") String token);

    @GET(AppConstants.API_VERSION + "country")
    Single<Country> getCountryData(@Query("token") String token);

    @GET(AppConstants.API_VERSION + "surveys")
    Single<List<Form>> getSurveyData(@Query("token") String token);

    @GET(AppConstants.API_VERSION + "products")
    Single<Data> getProductsData(@Query("token") String token);

    @GET(AppConstants.API_VERSION + "farmers")
    Single<List<Farmer>> getFarmersData(@Query("token") String token);

    @GET(AppConstants.API_VERSION + "campaigns")
    Single<List<Campaign>> getCampaignsData(@Query("token") String token);

    @Headers("Content-Type: application/json")
    @POST(AppConstants.API_VERSION + "sync/down")
    Single<Response> getData(@Body String data);

    @Headers("Content-Type: application/json")
    @POST(AppConstants.API_VERSION + "sync/down")
    Single<SearchResponse> searchData(@Body String data);

    @Headers("Content-Type: application/json")
    @POST(AppConstants.API_VERSION + "sync/up")
    Single<Response> sendData(@Body String object);

    @Headers("Content-Type: application/json")
    @POST(AppConstants.API_VERSION + "sync/up")
    Single<UploadResponse> sendDataOnDemand(@Body String object);

    @POST(AppConstants.API_VERSION + "register")
    Single<RegistrationResponse> register(@Body User user);

}
