package deleva.com.requester.api;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import deleva.com.requester.Config;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by khinsandar on 2/3/15.
 */
public class RegisterApi {

    private static RegisterApi mInstance;
    private RegisterService mService;

    public  RegisterApi(){
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(7000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(7000, TimeUnit.MILLISECONDS);
        final RestAdapter restAdapter = new
                RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Config.BASE_URL)

                .setClient(new OkClient(okHttpClient))

                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Log.e("//////////////Register API////////////////////////", msg);
                    }
                })
                .setConverter(new StringConverter()) //Reply String result
                .build();

        //.setLog(new AndroidLog("YOUR_LOG_TAG"))

        //RestAdapter restAdapterII = new RestAdapter.Builder().setClient(new OkClient(okHttpClient)).build();

        /*new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.BASIC)
            .setEndpoint(Config.BASE_URL)
            .build();*/
        //.setClient(new OkClient(okHttpClient))



        /*final RestAdapter restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setConverter(new StringConverter())
                .setEndpoint(Config.PRODUCTS_BASE_URL).build();
*/
        mService = restAdapter.create(RegisterService.class);





    }

    public static RegisterApi getInstance(){
        if(mInstance == null){
            mInstance = new RegisterApi();
        }
        return  mInstance;
    }
    public RegisterService getService(){
        return mService;
    }

    public static class StringConverter implements Converter {


        @Override
        public Object fromBody(TypedInput body, Type type) throws ConversionException {
            String text = null;
            try {
                text = fromStream(body.in());
            } catch (IOException ignored) {/*NOP*/ }

            return text;
        }

        @Override
        public TypedOutput toBody(Object o) {
            return null;
        }

        public static String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }
    }
}
