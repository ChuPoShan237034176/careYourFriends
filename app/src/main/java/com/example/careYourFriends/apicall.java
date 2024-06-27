package com.example.careYourFriends;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class apicall {

    private String webApiUrl="https://data.weather.gov.hk/weatherAPI/opendata/";
    public class HKO_Data<T>{
        String generalSituation;
        String tcInfo;
        String fireDangerWarning;
        String forecastPeriod;
        String outlook;
        String updateTime;
        public HKO_Data(String generalSituation, String tcInfo, String fireDangerWarning, String forecastPeriod, String outlook,String  updateTime) {
            this.generalSituation=generalSituation;
            this.tcInfo=tcInfo;
            this.fireDangerWarning=fireDangerWarning;
            this.forecastPeriod=fireDangerWarning;
            this.outlook=fireDangerWarning;
            this.updateTime=fireDangerWarning;
        }
    }


    public interface iWebApis {
        @GET("weather.php?dataType=flw&lang=tc")
        Call<ResponseBody> I_getHKO();
    }

    public  void HKO_Data_Req(Callback<ResponseBody> callback){

        Retrofit retrofit;
        try{
            retrofit = new Retrofit.Builder()
                    .baseUrl(webApiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            iWebApis api = retrofit.create(iWebApis.class);

            Call<ResponseBody> call = api.I_getHKO();
            call.enqueue(callback);

        }catch (Exception e){
            Log.i(TAG,e.toString());
        }

    }

    public  HKO_Data HKO_Data_Res(String HKO_Data_Json){
        return new Gson().fromJson(HKO_Data_Json,HKO_Data.class);
    }

}
