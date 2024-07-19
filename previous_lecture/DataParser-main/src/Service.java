import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    private final Map<String, String> balanceGenEd = new HashMap<>();
    private final Map<String, String> coreGenEd = new HashMap<>();
    private final Map<String, String> basicGenEd = new HashMap<>();
    private final Map<String, String> normalGenEd = new HashMap<>();
    private static final Map<String, String> subjectTypeMap = new HashMap<>();

    static {
        subjectTypeMap.put("사고와표현", "thinkingAndExpression");
        // 필요한 매핑 정보를 여기에 추가
    }

    private Connection conn = null;

    public void insertEverytimeTable(Connection conn, String fileName) {
        try {
            String semesterYear = extractSemesterYear(fileName);
            String jsonString = readJsonTxt(fileName);
            List<JSONObject> jsonObjects = xmlToJsonObjects("subject", jsonString);

            for (JSONObject jsonObject : jsonObjects) {
                insertToEverytimeTable(conn, jsonObject, semesterYear);
            }

            System.out.println(fileName + " inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<JSONObject> xmlToJsonObjects(String key, String xml) {
        JSONArray jsonArray = XML.toJSONObject(xml).getJSONArray(key);
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjects.add(jsonArray.getJSONObject(i));
        }
        return jsonObjects;
    }

    public Connection createConn(String url, String user, String password) {
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
        return baseName.replaceAll("[^0-9_]", "");
    }

    public void insertCurrentLecturesTable(Connection conn, String fileName, String semester) {
        String jsonString = readJsonTxt("/Users/dodosolsol/Downloads/previous_lecture/DataParser-main/2024subData.txt");

        if (jsonString != null && !jsonString.isEmpty()) {
            System.out.println("File read successfully: " + jsonString);
        } else {
            System.out.println("Failed to read the file or file is empty.");
            return;
        }

        JSONArray jsonArray = new JSONArray(jsonString);

        if (jsonArray != null && jsonArray.length() > 0) {
            System.out.println("JSON array parsed successfully. Number of items: " + jsonArray.length());
        } else {
            System.out.println("Failed to parse JSON array or JSON array is empty.");
            return;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            try {
                insertToCurrentLecturesTable(conn, jsonObject, semester);
                System.out.println("Inserted item: " + jsonObject.toString());
            } catch (SQLException e) {
                System.out.println("Failed to insert item: " + jsonObject.toString());
                e.printStackTrace();
            }
        }

        System.out.println(fileName + " inserted successfully.");
    }

    private void insertToCurrentLecturesTable(Connection connection, JSONObject jsonObject, String semester) throws SQLException {
        String query = "INSERT INTO current_lecture (SBJ_NO, SBJ_NM, LECT_TIME_ROOM, CMP_DIV_RCD, THEO_TIME, " +
                "ATTC_FILE_NO, DIVCLS, TLSN_RMK, CDT, SES_RCD, CMP_DIV_NM, CYBER_YN, CYBER_B_YN, SCH_YEAR, " +
                "PRAC_TIME, CYBER_S_YN, FILE_PBY_YN, KIND_RCD, SBJ_DIVCLS, STAFF_NM, DEPT_CD, RMK, " +
                "CYBER_E_YN, REP_STAFF_NO, EST_DEPT_INFO, SMT_RCD, CRS_SHYR, KIND_NM, BEF_CTNT_02, BEF_CTNT_01, semester_year) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, jsonObject.optString("SBJ_NO", null));
            preparedStatement.setString(2, jsonObject.optString("SBJ_NM", null));
            preparedStatement.setString(3, jsonObject.optString("LECT_TIME_ROOM", null));
            preparedStatement.setString(4, jsonObject.optString("CMP_DIV_RCD", null));
            preparedStatement.setInt(5, jsonObject.optInt("THEO_TIME", 0));
            preparedStatement.setString(6, jsonObject.optString("ATTC_FILE_NO", null));
            preparedStatement.setInt(7, jsonObject.optInt("DIVCLS", 0));
            preparedStatement.setString(8, jsonObject.optString("TLSN_RMK", null));
            preparedStatement.setInt(9, jsonObject.optInt("CDT", 0));
            preparedStatement.setString(10, jsonObject.optString("SES_RCD", null));
            preparedStatement.setString(11, jsonObject.optString("CMP_DIV_NM", null));
            preparedStatement.setString(12, jsonObject.optString("CYBER_YN", null));
            preparedStatement.setString(13, jsonObject.optString("CYBER_B_YN", null));
            preparedStatement.setInt(14, jsonObject.optInt("SCH_YEAR", 0));
            preparedStatement.setInt(15, jsonObject.optInt("PRAC_TIME", 0));
            preparedStatement.setString(16, jsonObject.optString("CYBER_S_YN", null));
            preparedStatement.setString(17, jsonObject.optString("FILE_PBY_YN", null));
            preparedStatement.setString(18, jsonObject.optString("KIND_RCD", null));
            preparedStatement.setString(19, jsonObject.optString("SBJ_DIVCLS", null));
            preparedStatement.setString(20, jsonObject.optString("STAFF_NM", null));
            preparedStatement.setString(21, jsonObject.optString("DEPT_CD", null));
            preparedStatement.setString(22, jsonObject.optString("RMK", null));
            preparedStatement.setString(23, jsonObject.optString("CYBER_E_YN", null));
            preparedStatement.setString(24, jsonObject.optString("REP_STAFF_NO", null));
            preparedStatement.setString(25, jsonObject.optString("EST_DEPT_INFO", null));
            preparedStatement.setString(26, jsonObject.optString("SMT_RCD", null));
            preparedStatement.setInt(27, jsonObject.optInt("CRS_SHYR", 0));
            preparedStatement.setString(28, jsonObject.optString("KIND_NM", null));
            preparedStatement.setString(29, jsonObject.optString("BEF_CTNT_02", null));
            preparedStatement.setString(30, jsonObject.optString("BEF_CTNT_01", null));
            preparedStatement.setString(31, semester);

            preparedStatement.executeUpdate();
        }
    }

    public static String readJsonTxt(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}