package unzip.test.com.net;




import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import unzip.test.com.application.MyApp;
import unzip.test.com.unzip.R;
import unzip.test.com.utils.L;
import unzip.test.com.utils.SPUtils;

//import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiRetrofit {

    public NetApi NetApiService;

     String HOST = "10.0.2.248:8080";

    // String BASE_URL = MyApp.getInstance().getString(R.string.base_url);
     String BASE_URL = SPUtils.getBaseUrlInfo();
     private final  long DEFAULT_TIMEOUT = 60;//默认超时时间30
     private final  long DEFAULT_TIMEOUT_READ = 60 * 5;//默认读取超时时间60

    public NetApi getNetApiService() {
        return NetApiService;
    }



    ApiRetrofit() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        //cache url
        File httpCacheDirectory = new File(MyApp.getInstance().getCacheDir(), " ");

        Cache cache = new Cache(httpCacheDirectory, cacheSize);

      //  OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder());

        getSSLSocketFactory(builder);//添加https单项验证,自定义证书


        OkHttpClient client = builder
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT_READ, TimeUnit.SECONDS)
                .addInterceptor(new NetInterceptor())
                .cache(cache).build();

      //  new Thread(() -> L.e("获取ip:" + NetworkUtils.getInetAddress("kxota.httc.com.cn"))).start();
        Retrofit retrofit_Net = new Retrofit.Builder()
                .baseUrl( BASE_URL)
                .client(client)
              //  .addConverterFactory(SimpleXmlConverterFactory.create())
              //  .addConverterFactory(ScalarsConverterFactory.create())//直接返回string类型需要引入,添加 String类型[ Scalars (primitives, boxed, and String)] 转换器
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();



        NetApiService = retrofit_Net.create(NetApi.class);

    }




    /**
     * 自定义bks证书验证
     * @return
     */
    public  void getSSLSocketFactory(OkHttpClient.Builder clientBuilder) {
        final String CLIENT_TRUST_PASSWORD = "passsword";//信任证书密码
        final String CLIENT_AGREEMENT = "TLS";//使用协议
        final String CLIENT_TRUST_KEYSTORE = "BKS";
        SSLContext sslContext = null;
        try {
            //取得SSL的SSLContext实例
            sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
            //取得TrustManagerFactory的X509密钥管理器实例
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //取得BKS密库实例
            KeyStore tks = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE);
          //  InputStream is = MyApp.getInstance().getResources().openRawResource(R.raw.httcandroid);//证书
            //todo 证书
            InputStream is = null;//证书
            try {
                tks.load(is, CLIENT_TRUST_PASSWORD.toCharArray());
            } finally {
                is.close();
            }
            //初始化密钥管理器
            trustManager.init(tks);
            TrustManager[] trustManagers = trustManager.getTrustManagers();
            //初始化SSLContext
            sslContext.init(null, trustManagers, null);
            X509TrustManager x509trustManager = (X509TrustManager) trustManagers[0];
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory(),x509trustManager)
                    .hostnameVerifier(new MyHostnameVerifier());
        } catch (Exception e) {
            e.printStackTrace();
           L.e( "SSLSocketFactory error:" + e.getMessage());
        }

    }



}
