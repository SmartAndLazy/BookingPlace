package ru.nutscoon.mapsproject.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.nutscoon.mapsproject.App;
import ru.nutscoon.mapsproject.Models.AvaliableHours;
import ru.nutscoon.mapsproject.Services.IApiService;

public class HoursViewModel extends ViewModel {

    public HoursViewModel() {
        App.getComponent().inject(this);
    }


    public MutableLiveData<AvaliableHours> getAvaliableHoursMutableLiveData() {
        return avaliableHoursMutableLiveData;
    }

    private MutableLiveData<AvaliableHours> avaliableHoursMutableLiveData = new MutableLiveData<>();

    MutableLiveData<AvaliableHours> getavaliableHoursMutableLiveData(){ return avaliableHoursMutableLiveData; }
    @Inject
    public IApiService apiService;

    public void getavaliableHoursData(String date, int id){
        apiService.getOrganizationBookingDetail(id, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        getavaliableHoursMutableLiveData().postValue(null);
                    }
                })
                .subscribe(new io.reactivex.functions.Consumer<Response<AvaliableHours>>() {
                    @Override
                    public void accept(Response<AvaliableHours> organizationDataResponse) {
                        if(organizationDataResponse.isSuccessful()){
                            getavaliableHoursMutableLiveData().postValue(organizationDataResponse.body());
                        }else {
                            getavaliableHoursMutableLiveData().postValue(null);
                        }
                    }
                });
    }

}
