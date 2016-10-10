package Tool;

import android.util.Log;

/**
 * Created by luweiling on 2016/10/10 0010.
 */
public class LogUtil {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int current = VERBOSE;

    public static void v(String message) {
        if (current <= VERBOSE)
            Log.v("TAG",message);
    }

    public static void d(String message) {
        if (current <= DEBUG)
            Log.d("TAG",message);
    }

    public static void i(String message) {
        if (current <= INFO)
            Log.i("TAG",message);
    }

    public static void w(String message) {
        if (current <= WARN)
            Log.w("TAG",message);
    }

    public static void e(String message) {
        if (current <= ERROR)
            Log.e("TAG",message);
    }

}
