package ru.nutscoon.mapsproject.Services;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.nutscoon.mapsproject.Models.OrganizationsOnMapData;

public interface IOrganizationsService {
    @GET("API_URL")
    Observable<List<OrganizationsOnMapData>> getOrganizations();
}
