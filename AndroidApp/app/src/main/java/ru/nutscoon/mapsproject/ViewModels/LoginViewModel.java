package ru.nutscoon.mapsproject.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.nutscoon.mapsproject.App;
import ru.nutscoon.mapsproject.Models.Login;
import ru.nutscoon.mapsproject.Models.User;
import ru.nutscoon.mapsproject.Services.IApiService;
import ru.nutscoon.mapsproject.Services.ILocalRepository;

public class LoginViewModel extends ViewModel {

    public LoginViewModel(){
        App.getComponent().inject(this);
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    @Inject
    public IApiService apiService;

    @Inject
    public ILocalRepository localRepository;

    public void login(String phone, String password){
        String hash = null;
        try {
            hash = getHash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        apiService.login(new Login(phone, hash))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                })
                .subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> integerResponse) throws Exception {
                        if(integerResponse.isSuccessful()){
                            localRepository.saveValue("user", integerResponse.body());
                            userLiveData.postValue(integerResponse.body());
                        }
                    }
                });
    }


    private String getHash(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(s.getBytes());
        byte[] digest = md.digest();
        return new String(digest);
    }
}
