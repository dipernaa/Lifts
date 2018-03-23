package connorhenke.com.lifts;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {MainModule.class})
interface ApplicationComponent {

    void inject(LiftApplication application);
    void inject(MainActivity activity);
    void inject(LiftActivity activity);
}