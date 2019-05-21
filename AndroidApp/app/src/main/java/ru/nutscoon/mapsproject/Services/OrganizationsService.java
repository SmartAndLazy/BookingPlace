package ru.nutscoon.mapsproject.Services;

import java.util.List;

import io.reactivex.Observable;
import ru.nutscoon.mapsproject.Models.OrganizationsOnMapData;

public class OrganizationsService implements IOrganizationsService {

    @Override
    public Observable<List<OrganizationsOnMapData>> getOrganizations() {
        return Observable.just(OrganizationsOnMapData.getOrganizationsOnMapDatas());
    }
}

