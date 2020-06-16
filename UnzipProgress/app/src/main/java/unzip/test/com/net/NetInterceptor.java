package unzip.test.com.net;




import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import unzip.test.com.application.MyApp;
import unzip.test.com.utils.L;
import unzip.test.com.utils.NetworkUtils;

/**
 * 网络框架拦截器
 */
public class NetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);
        cacheBuilder.maxStale(365, TimeUnit.DAYS);
        CacheControl cacheControl = cacheBuilder.build();

        Request request = chain.request();
        request = request.newBuilder()//添加统一请求头
             .addHeader("Content-type","application/json")
             .addHeader("Accept","application/json")
             .build();

        L.e("网络是否存在:" + NetworkUtils.isNetworkAvailable(MyApp.getInstance()) + "...url = " + request.url());
        if (!NetworkUtils.isNetworkAvailable(MyApp.getInstance())) {//网络不存在
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();

        }
        Response originalResponse = chain.proceed(request);
        if (NetworkUtils.isNetworkAvailable(MyApp.getInstance())) {
            int maxAge = 0; // read from cache
            return originalResponse.newBuilder()
                    .addHeader("Content-type","application/json")
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    }
}
