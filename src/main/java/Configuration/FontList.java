package Configuration;
import java.util.LinkedHashMap;
import java.util.Map;

public class FontList {
    public static final Map<String, String> FONTS = new LinkedHashMap<>() {{
        put("Arial", "Arial");
        put("Verdana", "Verdana");
        put("Tahoma", "Tahoma");
        put("Trebuchet MS", "Trebuchet MS");
        put("Times New Roman", "Times New Roman");
        put("Georgia", "Georgia");
        put("Garamond", "Garamond");
        put("Courier New", "Courier New");
        put("Brush Script MT", "Brush Script MT");
    }};
}
