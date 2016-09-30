package activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.luweiling.weatherpractise.R;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public class Weather_Activity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weatherpage_layout);
    }
}
