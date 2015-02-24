
/*
 * Copyright 2014 MMAUG (Myanmar Android User Group)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package deleva.com.requester;

/**
 * Created by Ye Lin Aung on 14/06/07.
 */
public class Config {


    public static final String BASE_URL = "http://kny.co:3001/api/v1.1/requester";

    public static final String REQUESTER_REGISTER = "/register";

    //http://kny.com:3001/api/v1.1/requester/jobs?access_token=fa45b840385ff4efa217fefb893ea4d4e552e2b50fcc5bae0b3c0556d2eedd53
    public static final String REQUESTER_JOB_POST = "/jobs";

    //http://kny.co:3001/api/v1.1/requester/jobs/me?access_token=8f6b790c5d1fa7d080b7060df8e69d3caaf713aa148f7a17d9a7808b3c0395ed
    public static final String MY_JOBS_URL = "/jobs/me";

    //http://kny.co:3001/api/v1.1/requester/jobs/54d3688d341828ca18100f22?access_token=8f6b790c5d1fa7d080b7060df8e69d3caaf713aa148f7a17d9a7808b3c0395ed
    public static final String JOB_Detail = "/jobs/{id}";






    public static final String GET_TOKEN = "/getToken";

    public static String TOKEN_PREF = "requestertokenpref";
    public static String TOKEN = "requestertoken";

    public static String PWD_REMEMBER_PREF = "rememberpwdpref";
    public static String PWD_REMEMBER_USER = "user";
    public static String PWD_REMEMBER = "pwd";






}
