import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        // JDBC 커넥션 생성
        Connection conn = service.createConn(
                "jdbc:mysql://localhost:3306/ETParse?serverTimezone=UTC&characterEncoding=UTF-8",
                "root",
                "gksthf0601");

        // 현재 강의 테이블 데이터 삽입
        //service.insertCurrentLecturesTable(conn, "/Users/dodosolsol/Downloads/previous_lecture/DataParser-main/2024subData.txt", "2024-1");


        // 이전 강의 테이블 데이터 삽입
        for (int year = 2018; year <= 2023; year++) {
            for (int semester = 1; semester <= 2; semester++) {
                String fileName = String.format("everytime%d_%d.txt", year, semester);
                service.insertEverytimeTable(conn, "/Users/dodosolsol/Downloads/previous_lecture/DataParser-main/src/everytime2018_1.txt");
            }
        }

        SubjectTypeExtractor.processJsonFile("/Users/dodosolsol/Downloads/previous_lecture/DataParser-main/input.json");
    }
}