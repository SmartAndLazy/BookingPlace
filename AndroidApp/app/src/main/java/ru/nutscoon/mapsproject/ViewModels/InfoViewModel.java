package ru.nutscoon.mapsproject.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.nutscoon.mapsproject.App;
import ru.nutscoon.mapsproject.Models.Comment;
import ru.nutscoon.mapsproject.Models.OrganizationData;
import ru.nutscoon.mapsproject.Models.User;
import ru.nutscoon.mapsproject.Services.IApiService;
import ru.nutscoon.mapsproject.Services.ILocalRepository;

public class InfoViewModel extends ViewModel {

    public InfoViewModel(){
        App.getComponent().inject(this);
    }

    private MutableLiveData<OrganizationData> organizationDataLiveData = new MutableLiveData<>();
    private MutableLiveData<GetInfoResult> infoResultLiveData = new MutableLiveData<>();
    private MutableLiveData<OrganizationData> commentSendResult = new MutableLiveData<>();

    public MutableLiveData<OrganizationData> getCommentSendResult() {
        return commentSendResult;
    }

    public MutableLiveData<String> getLoginStatus() {
        return loginStatus;
    }

    private MutableLiveData<String> loginStatus = new MutableLiveData<>();

    public MutableLiveData<OrganizationData> getOrganizationDataLiveData() {
        return organizationDataLiveData;
    }

    public MutableLiveData<GetInfoResult> getInfoResultLiveData() {
        return infoResultLiveData;
    }

    @Inject
    public IApiService apiService;

    @Inject
    public ILocalRepository localRepository;

    public void getOrganizationInfo(double lat, double lon, String orgName){
        apiService.getOrganizationInfo(orgName, lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        infoResultLiveData.postValue(GetInfoResult.ERROR);
                    }
                })
                .subscribe(new Consumer<Response<OrganizationData>>() {
                    @Override
                    public void accept(Response<OrganizationData> organizationDataApiResult) {
                        if(organizationDataApiResult.isSuccessful()){
                            organizationDataLiveData.postValue(organizationDataApiResult.body());
                        }else {
                            infoResultLiveData.postValue(GetInfoResult.ERROR);
                        }
                    }
                });
    }

    public void postComment(String text, double rate){
        User user = getUser();
        apiService.postComment(new Comment(user.getId(), organizationDataLiveData.getValue().getId(), text, rate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        commentSendResult.postValue(null);
                    }
                })
                .subscribe(new Consumer<Response<OrganizationData>>() {
                    @Override
                    public void accept(Response<OrganizationData> integerResponse) {
                        if(integerResponse.isSuccessful()){
                            commentSendResult.postValue(integerResponse.body());
                        }else {
                            commentSendResult.postValue(null);
                        }
                    }
                });
    }

    public boolean isUserLoggedIn(){
        return getUser() != null;
    }

    public void loadUserName() {
        User user = getUser();
        if(user == null){
            loginStatus.postValue(null);
        }else {
            loginStatus.postValue(user.getName() + " " + user.getSurname());
        }
    }

    public void logout(){
        localRepository.remove("user");
    }

    private User getUser(){
        return localRepository.getValue("user", User.class);
    }

    public enum GetInfoResult {
        SUCCESS,
        ERROR
    }

}
