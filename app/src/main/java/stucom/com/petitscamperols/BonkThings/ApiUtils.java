package stucom.com.petitscamperols.BonkThings;

public class ApiUtils {

    public ApiUtils() {
    }

    public static ApiFlxService getApiFlxService() {

        return RetrofitClient.getClient("http://stucom.flx.cat/game/api/score/").create(ApiFlxService.class);
    }
}
