package ru.nutscoon.mapsproject.ViewModels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.nutscoon.mapsproject.App;
import ru.nutscoon.mapsproject.Models.OrganizationsOnMapData;
import ru.nutscoon.mapsproject.Services.IOrganizationsService;

public class MainViewModel extends ViewModel {

    private MutableLiveData<List<OrganizationsOnMapData>> organizationsLiveData = new MutableLiveData<>();

    @Inject
    public IOrganizationsService organizationsService;

    public MainViewModel() {
        App.getComponent().inject(this);
    }

    public MutableLiveData<List<OrganizationsOnMapData>> getOrganizationsLiveData() {
        return organizationsLiveData;
    }

    public void ready(){
        organizationsService
                .getOrganizations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        // TODO: 31.03.2019 Implement error handler
                    }
                })
                .subscribe(new Consumer<List<OrganizationsOnMapData>>() {
                    @Override
                    public void accept(List<OrganizationsOnMapData> organizations) {
                        organizationsLiveData.postValue(organizations);
                    }
                });

    }
}
