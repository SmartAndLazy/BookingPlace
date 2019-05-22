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
import ru.nutscoon.mapsproject.Models.User;
import ru.nutscoon.mapsproject.Services.IApiService;
import ru.nutscoon.mapsproject.Services.ILocalRepository;

public class RegistrationViewModel extends ViewModel {

    public RegistrationViewModel() {
        App.getComponent().inject(this);
    }

    private MutableLiveData<User> registratinResultLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getRegistratinResultLiveData() {
        return registratinResultLiveData;
    }

    @Inject
    public IApiService apiService;

    @Inject
    public ILocalRepository localRepository;

    public void registerUser(String name, String surname, String phone, String password){
        String passwordHash;
        try {
            passwordHash = getHash(password);
        } catch (NoSuchAlgorithmException e) {
            return;
        }

        final User user = new User(name, surname, phone, passwordHash);

        apiService.registerUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        registratinResultLiveData.postValue(null);
                    }
                })
                .subscribe(new Consumer<Response<User>>() {
                    @Override
                    public void accept(Response<User> integerResponse) {
                        if(integerResponse.isSuccessful()){
                            registratinResultLiveData.postValue(integerResponse.body());
                        }else {
                            registratinResultLiveData.postValue(null);
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
