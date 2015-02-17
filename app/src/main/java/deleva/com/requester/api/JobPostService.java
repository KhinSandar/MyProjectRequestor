package deleva.com.requester.api;

import deleva.com.requester.Config;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.mime.TypedInput;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface JobPostService {

    @FormUrlEncoded
    @POST(Config.REQUESTER_JOB_POST)
    public void requestorJobPost(@Query("access_token") String token,
                                  @Field("type") String type,
                                  @Field("address") String address,
                                  @Field("address_ll") String address_ll,
                                  @Field("price") String price,
                                  @Field("receiver_name") String receiver_name,
                                  @Field("receiver_contact") String receiver_contact,
                                  @Field("post_code") String post_code,
                                  Callback<String> callback);




}
