package deleva.com.requester.api;

import deleva.com.requester.Config;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import retrofit.mime.TypedInput;

/**
 * Created by khinsandar on 2/3/15.
 */
public interface RegisterService {

    @FormUrlEncoded
    @POST(Config.REQUESTER_REGISTER)
    public void requestorRegister(@Field("name") String name,
                               @Field("email") String email,
                               @Field("password") String password,
                               @Field("mobile_number") String mobileno,
                               @Field("address") String address,
                               @Field("business_type") String business_type,
                               @Field("business_address") String business_add,
                               @Field("business_address_ll") String business_add_ll,
                               Callback<String> callback);

    @POST(Config.GET_TOKEN)
    public void getToken(@Body TypedInput input, Callback<String> callback);


}
