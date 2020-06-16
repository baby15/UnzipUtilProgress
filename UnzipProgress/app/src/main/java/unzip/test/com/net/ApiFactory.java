package unzip.test.com.net;


public class ApiFactory {

    protected static final Object monitor = new Object();
    static NetApi netApiSingleton = null;

    //return Singleton
    public static NetApi getNetApiSingleton() {
        synchronized (monitor) {
            if (netApiSingleton == null) {
                netApiSingleton = new ApiRetrofit().getNetApiService();
            }
            return netApiSingleton;
        }
    }

    //return Singleton


}
