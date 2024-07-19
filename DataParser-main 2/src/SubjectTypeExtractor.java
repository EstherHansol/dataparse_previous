import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SubjectTypeExtractor {

    public static final Map<String, Map<String, String>> subjectTypeMap = new HashMap<>();

    public static void processJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONObject json = new JSONObject(reader);

            JSONObject subjects = json.getJSONObject("subjects");
            for (String subjectCode : subjects.keySet()) {
                JSONObject types = subjects.getJSONObject(subjectCode);
                Map<String, String> typeMap = new HashMap<>();
                for (String typeCode : types.keySet()) {
                    String typeName = types.getString(typeCode);
                    typeMap.put(typeCode, typeName);
                }
                subjectTypeMap.put(subjectCode, typeMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
