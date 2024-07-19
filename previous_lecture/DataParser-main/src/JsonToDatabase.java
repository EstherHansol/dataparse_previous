/*import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonToDatabase {
    public static void main(String[] args) {
        String previousFilePath = "/Users/dodosolsol/Downloads/previous_lecture/DataParser-main/previous.json";
        String currentFilePath = "/Users/dodosolsol/Downloads/previous_lecture/DataParser-main/2024subData.txt";
        processJsonFiles(previousFilePath, currentFilePath);
        updateSubjectType();
    }

    public static void processJsonFiles(String previousFilePath, String currentFilePath) {
        try {
            String previousJsonStr = new String(Files.readAllBytes(Paths.get(previousFilePath)));
            String currentJsonStr = new String(Files.readAllBytes(Paths.get(currentFilePath)));

            JSONArray previousLectures = new JSONArray(previousJsonStr);
            JSONArray currentLectures = new JSONArray(currentJsonStr);

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ETParse?serverTimezone=UTC&characterEncoding=UTF-8",
                    "root", "gksthf0601")) {
                insertPreviousLectures(conn, previousLectures);
                insertCurrentLectures(conn, currentLectures);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insertPreviousLectures(Connection conn, JSONArray lectures) throws SQLException {
        String sql = "INSERT INTO previous_lectures (code, name, professor, type, notice) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < lectures.length(); i++) {
                JSONObject lecture = lectures.getJSONObject(i);

                pstmt.setString(1, lecture.getString("code"));
                pstmt.setString(2, lecture.getString("name"));
                pstmt.setString(3, lecture.getString("professor"));
                pstmt.setString(4, lecture.getString("type"));
                pstmt.setString(5, lecture.getString("notice"));

                pstmt.executeUpdate();
            }
        }
    }

    private static void insertCurrentLectures(Connection conn, JSONArray lectures) throws SQLException {
        String sql = "INSERT INTO current_lecture (SBJ_NO, SBJ_NM, LECT_TIME_ROOM, CMP_DIV_RCD, THEO_TIME, ATTC_FILE_NO, DIVCLS, TLSN_RMK, CDT, SES_RCD, CMP_DIV_NM, CYBER_YN, CYBER_B_YN, SCH_YEAR, PRAC_TIME, CYBER_S_YN, FILE_PBY_YN, KIND_RCD, SBJ_DIVCLS, STAFF_NM, DEPT_CD, RMK, CYBER_E_YN, REP_STAFF_NO, EST_DEPT_INFO, SMT_RCD, CRS_SHYR, KIND_NM, BEF_CTNT_02, BEF_CTNT_01) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < lectures.length(); i++) {
                JSONObject lecture = lectures.getJSONObject(i);

                pstmt.setString(1, lecture.getString("SBJ_NO"));
                pstmt.setString(2, lecture.getString("SBJ_NM"));
                pstmt.setString(3, lecture.getString("LECT_TIME_ROOM"));
                pstmt.setString(4, lecture.getString("CMP_DIV_RCD"));
                pstmt.setInt(5, lecture.getInt("THEO_TIME"));
                pstmt.setObject(6, lecture.opt("ATTC_FILE_NO"));
                pstmt.setInt(7, lecture.getInt("DIVCLS"));
                pstmt.setString(8, lecture.optString("TLSN_RMK"));
                pstmt.setInt(9, lecture.getInt("CDT"));
                pstmt.setString(10, lecture.getString("SES_RCD"));
                pstmt.setString(11, lecture.getString("CMP_DIV_NM"));
                pstmt.setString(12, lecture.getString("CYBER_YN"));
                pstmt.setString(13, lecture.getString("CYBER_B_YN"));
                pstmt.setString(14, lecture.getString("SCH_YEAR"));
                pstmt.setInt(15, lecture.getInt("PRAC_TIME"));
                pstmt.setString(16, lecture.getString("CYBER_S_YN"));
                pstmt.setString(17, lecture.getString("FILE_PBY_YN"));
                pstmt.setString(18, lecture.optString("KIND_RCD"));
                pstmt.setString(19, lecture.getString("SBJ_DIVCLS"));
                pstmt.setString(20, lecture.getString("STAFF_NM"));
                pstmt.setString(21, lecture.getString("DEPT_CD"));
                pstmt.setString(22, lecture.optString("RMK"));
                pstmt.setString(23, lecture.getString("CYBER_E_YN"));
                pstmt.setString(24, lecture.getString("REP_STAFF_NO"));
                pstmt.setString(25, lecture.getString("EST_DEPT_INFO"));
                pstmt.setString(26, lecture.getString("SMT_RCD"));
                pstmt.setInt(27, lecture.getInt("CRS_SHYR"));
                pstmt.setString(28, lecture.getString("KIND_NM"));
                pstmt.setString(29, lecture.optString("BEF_CTNT_02"));
                pstmt.setString(30, lecture.optString("BEF_CTNT_01"));

                pstmt.executeUpdate();
            }
        }
    }

    public static void updateSubjectType() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ETParse?serverTimezone=UTC&characterEncoding=UTF-8",
                "root", "gksthf0601")) {

            String selectSQL = "SELECT code, type FROM previous_lectures";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
                while (rs.next()) {
                    String code = rs.getString("code");
                    String type = rs.getString("type");

                    String updateSQL = "UPDATE current_lecture SET subject_type = ? WHERE SBJ_NO = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                        pstmt.setString(1, type);
                        pstmt.setString(2, code);

                        pstmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
*/