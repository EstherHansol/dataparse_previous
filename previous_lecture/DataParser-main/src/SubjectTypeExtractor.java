/*import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SubjectTypeExtractor {

    // 핵심교양 매핑
    private static final Map<String, String> coreGenEd = new HashMap<>();
    static {
        coreGenEd.put("균형교양", "균형교양");
        coreGenEd.put("핵심교양", "핵심교양");
        coreGenEd.put("기초교양", "기초교양");
        // 추가적인 매핑 필요시 여기에 추가
    }

    public static void main(String[] args) {
        String filePath = "/Users/dodosolsol/Downloads/DataParser-main/input.json";
        processJsonFile(filePath);
    }

    public static void processJsonFile(String filePath) {
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray subjects = new JSONArray(jsonStr);

            // 데이터베이스 연결
            try (Connection conn = DriverManager.getConnection(                "jdbc:mysql://localhost:3306/ETParse?serverTimezone=UTC&characterEncoding=UTF-8", "root", "gksthf0601")) {
                for (int i = 0; i < subjects.length(); i++) {
                    JSONObject subject = subjects.getJSONObject(i);
                    String code = subject.getString("code");
                    String name = subject.getString("name");
                    String professor = subject.getString("professor");
                    String type = subject.getString("type");
                    String notice = subject.getString("notice");

                    // notice로부터 subject_type 매핑
                    String subjectType = mapSubjectTypeFromNotice(notice);

                    // 기존 데이터베이스의 subject_type 업데이트
                    updateSubjectType(conn, code, subjectType);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String mapSubjectTypeFromNotice(String notice) {
        // notice에서 특정 키워드를 찾아 subject_type으로 매핑
        for (String keyword : coreGenEd.keySet()) {
            if (notice.contains(keyword)) {
                return coreGenEd.get(keyword);
            }
        }
        // 매핑할 수 있는 키워드가 없는 경우 기본값 설정 등을 여기에 추가
        return "기타"; // 예시로 기타로 설정
    }

    private static void updateSubjectType(Connection conn, String code, String subjectType) throws SQLException {
        String sql = "UPDATE previous_lecture SET subject_type = ? WHERE code = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subjectType);
            pstmt.setString(2, code);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " row(s) for code " + code + " with subject_type " + subjectType);
        }
    }
}

*/


import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SubjectTypeExtractor {

    // 교양 유형 매핑
    private static final Map<String, Map<String, String>> subjectTypeMap = SubjectTypeMapper.subjectTypeMap();

    public static void main(String[] args) {
        String filePath = "/Users/dodosolsol/Downloads/DataParser-main/input.json";
        processJsonFile(filePath);
    }

    public static void processJsonFile(String filePath) {
        try {
            String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray subjects = new JSONArray(jsonStr);

            // 데이터베이스 연결
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ETParse?serverTimezone=UTC&characterEncoding=UTF-8",
                    "root", "gksthf0601")) {
                for (int i = 0; i < subjects.length(); i++) {
                    JSONObject subject = subjects.getJSONObject(i);
                    String code = subject.getString("code");
                    String name = subject.getString("name");
                    String professor = subject.getString("professor");
                    String type = subject.getString("type");
                    String notice = subject.getString("notice");

                    // notice로부터 subject_type 매핑
                    String subjectType = mapSubjectTypeFromNotice(notice);

                    // 기존 데이터베이스의 subject_type 업데이트
                    updateSubjectType(conn, code, subjectType);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String mapSubjectTypeFromNotice(String notice) {
        // 각 교양 유형에 대해 매핑
        for (String genEdType : subjectTypeMap.keySet()) {
            Map<String, String> typeMap = subjectTypeMap.get(genEdType);
            for (String keyword : typeMap.keySet()) {
                if (notice.contains(keyword)) {
                    return typeMap.get(keyword);
                }
            }
        }
        // 매핑할 수 있는 키워드가 없는 경우 기본값 설정 등을 여기에 추가
        return "기타"; // 예시로 기타로 설정
    }

    private static void updateSubjectType(Connection conn, String code, String subjectType) throws SQLException {
        String sql = "UPDATE previous_lecture SET subject_type = ? WHERE code = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, subjectType);
            pstmt.setString(2, code);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Updated " + rowsAffected + " row(s) for code " + code + " with subject_type " + subjectType);
        }
    }
}

