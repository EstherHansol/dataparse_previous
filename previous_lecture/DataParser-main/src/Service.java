import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    // 교양 유형에 대한 매핑 정보를 필드로 정의
    private final Map<String, String> balanceGenEd = new HashMap<>();
    private final Map<String, String> coreGenEd = new HashMap<>();
    private final Map<String, String> basicGenEd = new HashMap<>();
    private final Map<String, String> normalGenEd = new HashMap<>();
    private Connection conn = null;





    public void insertEverytimeTable(Connection conn, String fileName) {
        // Everytime 이전 강의 정보 삽입
        try {
            // 파일명에서 확장자 제거하여 학년 학기 추출
            String semesterYear = extractSemesterYear(fileName);

            // Txt -> String
            String jsonString = readJsonTxt(fileName);

            // Xml -> JsonObjects
            List<JSONObject> jsonObjects = xmlToJsonObjects("subject", jsonString);

            // JsonObjects -> EverytimeTable
            for (JSONObject jsonObject : jsonObjects) {
                insertToEverytimeTable(conn, jsonObject, semesterYear);
            }

            System.out.println("<" + fileName + "> inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String readJsonTxt(String fileName) {
        // json 형식 Txt -> String 변환
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonContent.toString();
    }

    private List<JSONObject> xmlToJsonObjects(String key, String xml) {
        // XML -> json 변환
        JSONArray jsonArray = XML.toJSONObject(xml).getJSONArray(key);
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjects.add(jsonArray.getJSONObject(i));
        }

        return jsonObjects;
    }

    public Connection createConn(String url, String user, String password) {
        // 커넥션 생성
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("====== Connecting To Database ======");
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private void insertToEverytimeTable(Connection connection, JSONObject jsonObject, String semesterYear) throws SQLException {
        String query = "INSERT INTO previous_lecture (lect_name, cmp_div, is_cyber, credit, subject_type, semester_year, code, notice, professor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, jsonObject.optString("name", null));
            preparedStatement.setString(2, jsonObject.optString("type", null));
            preparedStatement.setInt(3, jsonObject.optInt("is_cyber", 0));
            preparedStatement.setInt(4, jsonObject.optInt("credit", 0));

            String subjectCode = jsonObject.optString("SBJ_CD", null);
            String subjectType;

            // 각 교양 유형에 따라 맵에서 subject_type을 가져옴
            if (balanceGenEd.containsKey(subjectCode)) {
                subjectType = balanceGenEd.get(subjectCode);
            } else if (coreGenEd.containsKey(subjectCode)) {
                subjectType = coreGenEd.get(subjectCode);
            } else if (basicGenEd.containsKey(subjectCode)) {
                subjectType = basicGenEd.get(subjectCode);
            } else if (normalGenEd.containsKey(subjectCode)) {
                subjectType = normalGenEd.get(subjectCode);
            } else {
                subjectType = "기타"; // 기본값 처리
            }

            preparedStatement.setString(5, subjectType);
            preparedStatement.setString(6, semesterYear);
            preparedStatement.setString(7, jsonObject.optString("code", null));
            preparedStatement.setString(8, jsonObject.optString("notice", null));
            preparedStatement.setString(9, jsonObject.optString("professor", null));

            preparedStatement.executeUpdate();
        }
    }

    private String extractSemesterYear(String fileName) {
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));

        String semesterYear = baseName.replaceAll("[^0-9_]", "");

        return semesterYear;
    }
}
