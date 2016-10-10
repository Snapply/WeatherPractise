package Tool;

import android.app.Application;
import android.content.Context;

/**
 * Created by luweiling on 2016/10/10 0010.
 */
public class GetContext extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
