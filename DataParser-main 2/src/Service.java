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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {
    private Connection conn = null;

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

    public void insertCurrentLecturesTable(Connection conn, String fileName, String semesterYear) {
        try {
            String jsonString = readJsonTxt(fileName);

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                insertToCurrentLecturesTable(conn, jsonArray.getJSONObject(i), semesterYear);
            }
            System.out.println("<" + fileName + "> inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertToCurrentLecturesTable(Connection connection, JSONObject jsonObject, String semesterYear) throws SQLException {
        String query = "INSERT INTO current_lecture (lect_name, lect_time, lect_room, cmp_div, credit, is_cyber, " +
                "grade, semester_year, department, professor, code_section, code, notice) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, jsonObject.getString("SBJ_NM"));
            preparedStatement.setString(2, jsonObject.optString("LECT_TIME_ROOM", null));
            preparedStatement.setString(3, extractUniqueLectureRooms(jsonObject.optString("LECT_TIME_ROOM", null)));
            preparedStatement.setString(4, jsonObject.optString("CMP_DIV_NM", null));
            preparedStatement.setInt(5, jsonObject.optInt("CDT", 0));
            preparedStatement.setString(6, jsonObject.optString("CYBER_YN", null));
            preparedStatement.setInt(7, jsonObject.optInt("CRS_SHYR", 0));
            preparedStatement.setString(8, semesterYear);
            preparedStatement.setString(9, jsonObject.optString("EST_DEPT_INFO", null));
            preparedStatement.setString(10, jsonObject.optString("STAFF_NM", null));
            preparedStatement.setString(11, jsonObject.optString("SBJ_DIVCLS", null));
            preparedStatement.setString(12, jsonObject.optString("SBJ_DIVCLS", null).split("-")[0]);
            preparedStatement.setString(13, jsonObject.optString("TLSN_RMK", null));

            preparedStatement.executeUpdate();
        }
    }

    public void insertEverytimeTable(Connection conn, String fileName) {
        try {
            String semesterYear = extractSemesterYear(fileName);
            String jsonString = readJsonTxt(fileName);
            List<JSONObject> jsonObjects = xmlToJsonObjects("subject", jsonString);
            for (JSONObject jsonObject : jsonObjects) {
                insertToEverytimeTable(conn, jsonObject, semesterYear);
            }
            System.out.println("<" + fileName + "> inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String readJsonTxt(String fileName) {
        StringBuilder jsonContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonContent.toString();
    }

    private List<JSONObject> xmlToJsonObjects(String key, String xml) {
        JSONArray jsonArray = XML.toJSONObject(xml).getJSONArray(key);
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObjects.add(jsonArray.getJSONObject(i));
        }
        return jsonObjects;
    }

    private void insertToEverytimeTable(Connection connection, JSONObject jsonObject, String semesterYear) throws SQLException {
        String query = "INSERT INTO previous_lecture (lect_name, cmp_div, is_cyber, credit, subject_type, semester_year, code, notice, professor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, jsonObject.optString("name", null));
            preparedStatement.setString(2, jsonObject.optString("type", null));
            preparedStatement.setInt(3, jsonObject.optInt("is_cyber", 0));
            preparedStatement.setInt(4, jsonObject.optInt("credit", 0));
            preparedStatement.setString(5, jsonObject.optString("subject_type", null));
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

    private String extractUniqueLectureRooms(String lectTimeRoom) {
        if (lectTimeRoom == null || lectTimeRoom.isEmpty()) {
            return "";
        }
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(lectTimeRoom);
        Set<String> uniqueRooms = new HashSet<>();
        while (matcher.find()) {
            uniqueRooms.add(matcher.group(1));
        }
        return String.join(" ", uniqueRooms);
    }
}
