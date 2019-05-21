package ru.nutscoon.mapsproject.di;

import dagger.Component;
import ru.nutscoon.mapsproject.ViewModels.BookPlaceViewModel;
import ru.nutscoon.mapsproject.ViewModels.HoursViewModel;
import ru.nutscoon.mapsproject.ViewModels.InfoViewModel;
import ru.nutscoon.mapsproject.ViewModels.LoginViewModel;
import ru.nutscoon.mapsproject.ViewModels.MainViewModel;
import ru.nutscoon.mapsproject.ViewModels.RegistrationViewModel;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainViewModel mainViewModel);

    void inject(InfoViewModel infoViewModel);

    void inject(RegistrationViewModel registrationViewModel);

    void inject(LoginViewModel loginViewModel);

    void inject(BookPlaceViewModel bookPlaceViewModel);

    void inject(HoursViewModel hoursViewModel);
}
