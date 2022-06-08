import org.json.JSONObject;

public class Helper {

    public static String parseAuthorisationToken(String body) {
        JSONObject obj = new JSONObject(body);
        return obj.getString("accessToken");
    }
}
