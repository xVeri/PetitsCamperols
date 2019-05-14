package stucom.com.petitscamperols.BonkThings;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiFlxService {

    @POST("1/2")
    @FormUrlEncoded
    Call<Void> scoreUpdate(@Field("token") String token, @Field("score") int score);
}
