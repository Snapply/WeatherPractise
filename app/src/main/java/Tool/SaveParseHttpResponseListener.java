package Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public interface SaveParseHttpResponseListener {

    void onBasicComplete(ArrayList<String> list);

    void onStatusComplete(ArrayList<String> list);

    void onWeatherComplete(ArrayList<String> list);

    void onError(Exception e);
}
