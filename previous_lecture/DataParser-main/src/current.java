import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class current {

    public static void main(String[] args) {
        String jsonFilePath = "path/to/your/file.json"; // 파일 경로를 설정하세요
        String jdbcUrl = "jdbc:mysql://localhost:3306/ETParse?serverTimezone=UTC&characterEncoding=UTF-8";
        String username = "root";
        String password = "gksthf0601";

        try {
            // JSON 파일 읽기
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            JSONArray subjectsArray = new JSONArray(content);

            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            // SQL 쿼리 준비
            String insertSubjectSql = "INSERT INTO subjects (sbj_no, sbj_nm, lect_time_room, cmp_div_rcd, theo_time, attc_file_no, divcls, tlsn_rmk, cdt, ses_rcd, cmp_div_nm, cyber_yn, cyber_b_yn, sch_year, prac_time, cyber_s_yn, file_pby_yn, kind_rcd, sbj_divcls, staff_nm, dept_cd, rmk, cyber_e_yn, rep_staff_no, est_dept_info, smt_rcd, crs_shyr, kind_nm, bef_ctnt_02, bef_ctnt_01) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String insertSubjectTypeSql = "INSERT INTO subject_type (sbj_nm, type_name) VALUES (?, ?) ON DUPLICATE KEY UPDATE type_name = VALUES(type_name)";

            // subject_type 매핑
            Map<String, String> subjectTypeMapping = new HashMap<>();
            subjectTypeMapping.put("사고와표현", "thinkingAndExpression");

            for (int i = 0; i < subjectsArray.length(); i++) {
                JSONObject subject = subjectsArray.getJSONObject(i);

                // subjects 테이블에 데이터 삽입
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertSubjectSql)) {
                    preparedStatement.setString(1, subject.optString("SBJ_NO"));
                    preparedStatement.setString(2, subject.optString("SBJ_NM"));
                    preparedStatement.setString(3, subject.optString("LECT_TIME_ROOM"));
                    preparedStatement.setString(4, subject.optString("CMP_DIV_RCD"));
                    preparedStatement.setInt(5, subject.optInt("THEO_TIME"));
                    preparedStatement.setString(6, subject.optString("ATTC_FILE_NO", null));
                    preparedStatement.setInt(7, subject.optInt("DIVCLS"));
                    preparedStatement.setString(8, subject.optString("TLSN_RMK", null));
                    preparedStatement.setInt(9, subject.optInt("CDT"));
                    preparedStatement.setString(10, subject.optString("SES_RCD"));
                    preparedStatement.setString(11, subject.optString("CMP_DIV_NM"));
                    preparedStatement.setString(12, subject.optString("CYBER_YN"));
                    preparedStatement.setString(13, subject.optString("CYBER_B_YN"));
                    preparedStatement.setString(14, subject.optString("SCH_YEAR"));
                    preparedStatement.setInt(15, subject.optInt("PRAC_TIME"));
                    preparedStatement.setString(16, subject.optString("CYBER_S_YN"));
                    preparedStatement.setString(17, subject.optString("FILE_PBY_YN"));
                    preparedStatement.setString(18, subject.optString("KIND_RCD", null));
                    preparedStatement.setString(19, subject.optString("SBJ_DIVCLS"));
                    preparedStatement.setString(20, subject.optString("STAFF_NM"));
                    preparedStatement.setString(21, subject.optString("DEPT_CD"));
                    preparedStatement.setString(22, subject.optString("RMK", null));
                    preparedStatement.setString(23, subject.optString("CYBER_E_YN"));
                    preparedStatement.setString(24, subject.optString("REP_STAFF_NO"));
                    preparedStatement.setString(25, subject.optString("EST_DEPT_INFO"));
                    preparedStatement.setString(26, subject.optString("SMT_RCD"));
                    preparedStatement.setInt(27, subject.optInt("CRS_SHYR"));
                    preparedStatement.setString(28, subject.optString("KIND_NM"));
                    preparedStatement.setString(29, subject.optString("BEF_CTNT_02", null));
                    preparedStatement.setString(30, subject.optString("BEF_CTNT_01", null));

                    preparedStatement.executeUpdate();
                }

                // subject_type 테이블에 데이터 삽입
                if (subjectTypeMapping.containsKey(subject.optString("SBJ_NM"))) {
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSubjectTypeSql)) {
                        preparedStatement.setString(1, subject.optString("SBJ_NM"));
                        preparedStatement.setString(2, subjectTypeMapping.get(subject.optString("SBJ_NM")));

                        preparedStatement.executeUpdate();
                    }
                }
            }

            // 연결 닫기
            connection.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

