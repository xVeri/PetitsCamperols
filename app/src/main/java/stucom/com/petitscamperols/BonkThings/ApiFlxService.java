package stucom.com.petitscamperols.BonkThings;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiFlxService {

    @POST("1/2")
    @FormUrlEncoded
    Call<Void> scoreUpdate(@Field("key") String key, @Field("score") int score);
}
