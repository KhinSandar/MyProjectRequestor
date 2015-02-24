package deleva.com.requester.api;

import deleva.com.requester.Config;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface JobDetailService {





    @GET(Config.JOB_Detail)
    public void getJobDetail(@Path("id") String id, @Query("access_token") String s, Callback<String> callback);




}
