package deleva.com.requester.api;

import deleva.com.requester.Config;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface JobListService {

    @GET(Config.MY_JOBS_URL)
    public void getMyJobList(@Query("access_token") String s, Callback<String> callback);




}
