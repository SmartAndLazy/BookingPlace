package ru.nutscoon.mapsproject.di;

import android.content.Context;
import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nutscoon.mapsproject.Services.IApiService;
import ru.nutscoon.mapsproject.Services.ILocalRepository;
import ru.nutscoon.mapsproject.Services.IOrganizationsService;
import ru.nutscoon.mapsproject.Services.LocalRepository;
import ru.nutscoon.mapsproject.Services.OrganizationsService;

@Module
public class ApplicationModule {

    private Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    public IOrganizationsService getOrganizationService(){
        //return getRetrofitForOrganizationService().create(IOrganizationsService.class);
        return new OrganizationsService();
    }

    @Provides
    public IApiService getApiService(){
        return getRetrofitForApiService().create(IApiService.class);
    }

    @Provides
    public Context getContext(){
        return context;
    }

    @Provides
    public ILocalRepository getLocalRepository(Context context){
        return new LocalRepository(context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE));
    }

    private Retrofit getRetrofitForOrganizationService(){
        return getRetrofit("here_url");
    }

    private Retrofit getRetrofitForApiService(){
        return getRetrofit("http://bookingplace.azurewebsites.net");
    }

    private Retrofit getRetrofit(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
