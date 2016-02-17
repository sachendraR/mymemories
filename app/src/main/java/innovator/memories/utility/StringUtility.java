package innovator.memories.utility;

/**
 * Created by SachendraSingh on 2/13/16.
 */
public class StringUtility {

    public static String firebaseFormatKey(String key){
        return key.replace(".","_");
    }

}
