package com.clipsa.data.network;


import com.cowtribe.cowtribeapps.data.db.entity.User;
import com.cowtribe.cowtribeapps.data.network.model.LoginRequest;
import com.cowtribe.cowtribeapps.data.network.model.RegistrationResponse;
import com.cowtribe.cowtribeapps.data.network.model.Response;
import com.cowtribe.cowtribeapps.data.network.model.SearchResponse;
import com.cowtribe.cowtribeapps.data.network.model.UploadResponse;

import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AangJnr on 05, December, 2018 @ 4:03 PM
 * Work Mail cibrahim@grameenfoundation.org
 * Personal mail aang.jnr@gmail.com
 */

@Singleton
public class ApiService {

    private FdpApi fdpApi;

    @Inject
    public ApiService(FdpApi _fdpApi) {
        this.fdpApi = _fdpApi;
    }

    public Single<Response> makeLoginCall(String email, String password) {
        LoginRequest.ServerLoginRequest loginRequest = new LoginRequest.ServerLoginRequest(email, password);

        return fdpApi.makeLoginCall(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread());
    }

    public Single<RegistrationResponse> register(User user) {
        return fdpApi.register(user).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public Single<UploadResponse> pushDataOnDemand(JSONObject object) {
        return fdpApi.sendDataOnDemand(object.toString()).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());



    }


    public Single<Response> fetchData(JSONObject object) {
        return fdpApi.getData(object.toString()).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());

    }


    public Single<SearchResponse> searchData(JSONObject object) {
        return fdpApi.searchData(object.toString()).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());

    }

    public Single<Response> pushData(JSONObject object) {
        return fdpApi.sendData(object.toString()).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }



}
