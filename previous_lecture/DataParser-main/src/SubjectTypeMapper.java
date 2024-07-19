import java.util.HashMap;
import java.util.Map;

public class SubjectTypeMapper {

    public static Map<String, Map<String, String>> subjectTypeMap() {
        Map<String, Map<String, String>> subjectTypeMap = new HashMap<>();

        // 핵심교양 매핑
        Map<String, String> coreGenEd = new HashMap<>();
        coreGenEd.put("윤리실천역량", "ethicalPracticeSkill");
        coreGenEd.put("창의적문제해결역량", "creativeProblemSolvingSkill");
        coreGenEd.put("창의적문제해결영역", "creativeProblemSolvingSkill");
        coreGenEd.put("외국인전용반/상명핵심역량(창의적문제해결)/일반학생수강불가", "creativeProblemSolvingSkill");
        coreGenEd.put("융복합역량", "interdisciplinarySkill");
        coreGenEd.put("다양성존중역량", "diversityRespectSkill");
        coreGenEd.put("전문지식탐구역량", "expertiseResearchSkill");


        subjectTypeMap.put("핵심교양", coreGenEd);

        // 균형교양 매핑
        Map<String, String> balanceGenEd = new HashMap<>();
        balanceGenEd.put("인문학의이해", "humanity");
        balanceGenEd.put("사회과학의이해", "society");
        balanceGenEd.put("예술의이해", "art");
        balanceGenEd.put("예술과문화의이해", "art");
        balanceGenEd.put("미와예술의이해", "art");

        balanceGenEd.put("자연과공학의이해", "natureandengineering");
        balanceGenEd.put("자연과학의이해", "natureandengineering");
        balanceGenEd.put("자연과학의세계", "natureandengineering");

        balanceGenEd.put("연계", "bridge");

        subjectTypeMap.put("균형교양", balanceGenEd);

        // 기초교양 매핑 - 이거 계속 오류 떠서
        Map<String, String> basicGenEd = new HashMap<>();
        basicGenEd.put("사고와표현", "thinkingAndExpression");
        basicGenEd.put("잉파", "EFAP");
        basicGenEd.put("기초수학", "basicMath");
        basicGenEd.put("컴퓨팅사고와데이터", "computingdata");
        basicGenEd.put("알고리즘과게임콘텐츠", "algorithmgamecontents");
        basicGenEd.put("교양과인성", "RAP");
        basicGenEd.put("사회봉사", "service");


        subjectTypeMap.put("기초교양", basicGenEd);

        // 일반교양 매핑
        Map<String, String> normalGenEd = new HashMap<>();
        // 여기에 일반교양 매핑 정보를 추가

        subjectTypeMap.put("일반교양", normalGenEd);

        return subjectTypeMap;
    }
}
