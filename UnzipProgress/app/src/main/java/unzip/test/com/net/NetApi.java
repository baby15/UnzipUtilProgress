package unzip.test.com.net;


import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by huangwenjun
 */
public interface NetApi {



  //  @Headers({"Domain-Name: "+ AppConfig.HRADER_NAME})
   // @POST("httc")
    //Observable<CheckInfos> getCheckInfo(@Body ApkInfos apkInfo);
    ///httc/appCheck/getRiskInfoListByAppId
   // @Headers({"Domain-Name: "+ AppConfig.HRADER_NAME})
   // @POST("httc")
   // Observable<HoleDetailInfoBean> getHoleDetail(@Body HoleDetailIdBean holeDetailIdBean);

    //   @POST("appCheck/batchAppDetection")
    @GET("callback?val=abc")
    Observable<JsonObject> getCheckInfo();//test





}
