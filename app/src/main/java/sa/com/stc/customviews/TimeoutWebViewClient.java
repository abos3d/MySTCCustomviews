package sa.com.stc.customviews;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public abstract class TimeoutWebViewClient extends WebViewClient {
    public static final int DEFAULT_TIMEOUT = 60;
    private final int timeoutLimit;
    private boolean timeout;
    private Thread timeoutThread;

    protected TimeoutWebViewClient(int timeoutInSeconds) {
        this.timeoutLimit = timeoutInSeconds * 1000;
        timeout = true;
    }

    @Override
    public void onPageStarted(final WebView view, final String url, Bitmap favicon) {
        timeout = true;
        timeoutThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeoutLimit);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }
                if (timeout && view != null)
                    onPageTimeOut(view, url);
            }
        });
        timeoutThread.start();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        timeout = false;
        interruptTimeoutThread();
    }

    protected void onPageTimeOut(WebView view, String url) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            onReceivedError(view, 0, null, url);
        else
            onReceivedError(view, null, null);
    }

    public void onClientDestroy() {
        interruptTimeoutThread();
    }

    protected void interruptTimeoutThread() {
        if (timeoutThread != null && timeoutThread.isAlive() && !timeoutThread.isInterrupted())
            timeoutThread.interrupt();
    }
}