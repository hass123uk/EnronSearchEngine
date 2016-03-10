package BusinessLogicLayer;
import java.util.UUID;
/**
 * Created by HassanMahmud on 09/03/2016.
 */
public class UUIDGenerator {

    public static String generateID() {
        UUID randomUUID = UUID.randomUUID();
        String randomUUIDString = randomUUID.toString().replaceAll("-", "");

        return randomUUIDString;
    }
}