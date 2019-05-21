package ru.nutscoon.mapsproject.Services;


import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.nutscoon.mapsproject.Models.AvaliableHours;
import ru.nutscoon.mapsproject.Models.BookingInformation;
import ru.nutscoon.mapsproject.Models.Comment;
import ru.nutscoon.mapsproject.Models.Login;
import ru.nutscoon.mapsproject.Models.OrganizationData;
import ru.nutscoon.mapsproject.Models.User;


public interface IApiService {
    @GET("/api/BookingPlace/GetOrganizationData/{name}/{latitude}/{longitude}")
    Observable<Response<OrganizationData>> getOrganizationInfo(@Path("name") String name,
                                                               @Path("latitude") double lat,
                                                               @Path("longitude") double lon);

    @POST("url")
    Observable<Response<Integer>> bookPlace(@Body BookingInformation bookingInformation);

    @POST("/api/BookingPlace/rate")
    Observable<Response<OrganizationData>> postComment(@Body Comment comment);

    @POST("/api/BookingPlace/RegisterUser")
    Observable<Response<User>> registerUser(@Body User user);

    @POST("/api/BookingPlace/LoginUser")
    Observable<Response<User>> login(@Body Login login);

    @POST("/api/BookingPlace/GetOrganizationBookingDetail/{organizationId}/{day}")
    Observable<Response<AvaliableHours>> getOrganizationBookingDetail(@Path("organizationId") int organizationId,
                                                                      @Path("day") String day);
}