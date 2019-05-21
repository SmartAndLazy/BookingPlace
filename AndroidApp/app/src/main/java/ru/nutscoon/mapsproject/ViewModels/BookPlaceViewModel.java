package ru.nutscoon.mapsproject.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.sql.Time;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.nutscoon.mapsproject.App;
import ru.nutscoon.mapsproject.Models.BookingInformation;
import ru.nutscoon.mapsproject.Models.OrganizationData;
import ru.nutscoon.mapsproject.Models.User;
import ru.nutscoon.mapsproject.Services.IApiService;
import ru.nutscoon.mapsproject.Services.ILocalRepository;

public class BookPlaceViewModel extends ViewModel {

    public BookPlaceViewModel(){
        App.getComponent().inject(this);
    }

    private MutableLiveData<Integer> bookPlaceResultLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> getBookPlaceResultLiveData() {
        return bookPlaceResultLiveData;
    }

    @Inject
    public IApiService apiService;

    @Inject
    public ILocalRepository localRepository;

    public void ready(){
        User user = localRepository.getValue("user", User.class);
        if(user != null){
            userLiveData.postValue(user);
        }
    }

    public void bookPlace(int orgId, Date date, String time, String clientName, String clientSurname, String clientPhone){
        apiService.bookPlace(new BookingInformation(orgId, date, time, clientName, clientSurname, clientPhone))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                })
                .subscribe(new Consumer<Response<Integer>>() {
                    @Override
                    public void accept(Response<Integer> integerResponse) throws Exception {
                        if(integerResponse.isSuccessful()){
                            bookPlaceResultLiveData.postValue(1);
                        }
                    }
                });
    }
}
