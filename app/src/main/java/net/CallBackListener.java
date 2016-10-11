package net;

/**
 * Created by luweiling on 2016/9/30 0030.
 */
public interface CallBackListener {

    public void onComplete(String Response);

    public void onError(Exception e);
}
