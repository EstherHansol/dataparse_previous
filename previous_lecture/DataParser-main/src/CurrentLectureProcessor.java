import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CurrentLectureProcessor {

    private static final Map<String, Map<String, String>> subjectTypeMap = SubjectTypeMapper.subjectTypeMap();

    public static void main(String[] args) {
        String filePath = "/Users/dodosolsol/Downloads/previous_lecture/DataParser-main/current.json";
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

                    // code로부터 subject_type 매핑
                    String subjectType = mapSubjectTypeFromCode(code);

                    // previous_lecture 테이블에서 previous_lect_id 가져오기
                    int previousLectId = getPreviousLectureId(conn, code);

                    // current_lecture 테이블에 데이터 삽입
                    insertToCurrentLecturesTable(conn, previousLectId, code, name, professor, type, notice, subjectType);

                    // 각 강의 코드와 매핑 결과를 로그로 출력
                    System.out.println("Processed lecture with code: " + code + ", name: " + name + ", mapped subject type: " + subjectType);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getPreviousLectureId(Connection conn, String code) {
        String query = "SELECT id FROM previous_lecture WHERE code = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 예: 해당하는 previous_lecture가 없는 경우 -1 반환
    }

    private static void insertToCurrentLecturesTable(Connection conn, int previousLectId, String code, String name, String professor, String type, String notice, String subjectType) {
        String query = "INSERT INTO current_lecture (previous_lect_id, code, name, professor, type, notice, subject_type) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, previousLectId);
            preparedStatement.setString(2, code);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, professor);
            preparedStatement.setString(5, type);
            preparedStatement.setString(6, notice);
            preparedStatement.setString(7, subjectType);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String mapSubjectTypeFromCode(String code) {
        for (Map<String, String> map : subjectTypeMap.values()) {
            if (map.containsKey(code)) {
                return map.get(code);
            }
        }
        return null; // 기본 매핑이 없을 경우 null 반환
    }
}
