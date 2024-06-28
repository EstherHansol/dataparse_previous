import java.util.HashMap;
import java.util.Map;

public class SubjectTypeMapper {

    public static Map<String, Map<String, String>> subjectTypeMap() {
        Map<String, Map<String, String>> subjectTypeMap = new HashMap<>();

        // 핵심교양 매핑
        Map<String, String> coreGenEd = new HashMap<>();
        coreGenEd.put("윤리실천역량", "ethicalPracticeSkill"); //18.
        coreGenEd.put("창의적문제해결역량", "creativeProblemSolvingSkill"); //18.
        coreGenEd.put("창의적문제해결영역", "creativeProblemSolvingSkill");
        coreGenEd.put("외국인전용반/상명핵심역량(창의적문제해결)/일반학생수강불가", "creativeProblemSolvingSkill"); //18특이사항

        coreGenEd.put("융복합역량", "interdisciplinarySkill"); //18.
        coreGenEd.put("다양성존중역량", "diversityRespectSkill"); //18.
        //coreGenEd.put("인문학의이해", "humanity"); //제거
        // 추가적인 매핑
        coreGenEd.put("HBLB5056", "expertiseResearchSkill");
        coreGenEd.put("HBLB5070", "expertiseResearchSkill");
        coreGenEd.put("HBLB5071", "expertiseResearchSkill");
        coreGenEd.put("HBLB5072", "expertiseResearchSkill");
        coreGenEd.put("HBLB5060", "creativeProblemSolvingSkill");
        coreGenEd.put("HBLB5031", "creativeProblemSolvingSkill");
        coreGenEd.put("HBLB5075", "creativeProblemSolvingSkill");
        coreGenEd.put("HBLB5076", "creativeProblemSolvingSkill");
        coreGenEd.put("HBLD5009", "interdisciplinarySkill");
        coreGenEd.put("HBLD0051", "interdisciplinarySkill");
        coreGenEd.put("HBLA5052", "interdisciplinarySkill");
        coreGenEd.put("HBLB5080", "interdisciplinarySkill");
        coreGenEd.put("HBLA5060", "diversityRespectSkill");
        coreGenEd.put("HBLB5058", "diversityRespectSkill");
        coreGenEd.put("HBLA0272", "diversityRespectSkill");
        coreGenEd.put("HBLB5085", "diversityRespectSkill");
        coreGenEd.put("HBLA5061", "ethicalPracticeSkill");
        coreGenEd.put("HBLA5063", "ethicalPracticeSkill");
        coreGenEd.put("HBLG2017", "ethicalPracticeSkill");
        coreGenEd.put("HBLB5090", "ethicalPracticeSkill");
        subjectTypeMap.put("핵심교양", coreGenEd);

        // 균형교양 매핑
        Map<String, String> balanceGenEd = new HashMap<>();
        balanceGenEd.put("인문학의이해", "humanity"); //18.
        balanceGenEd.put("사회과학의이해", "socialScience"); //18(일부수정필요)
        balanceGenEd.put("예술의이해", "art");
        balanceGenEd.put("예술과문화의이해", "art"); //18.
        balanceGenEd.put("자연과공학의이해", "natureandengineering");
        balanceGenEd.put("자연과학의이해", "natureandengineering"); //18.

        balanceGenEd.put("연계", "bridge"); //18없음? /

        subjectTypeMap.put("균형교양", balanceGenEd);

        // 기초교양 매핑 - 아직 보류.
        Map<String, String> basicGenEd = new HashMap<>();
        basicGenEd.put("사고와표현", "thinkingAndExpression");
        basicGenEd.put("EFAP", "EFAP");
        basicGenEd.put("기초수학", "basicMath");
        basicGenEd.put("컴퓨팅과데이터", "computingdata");
        basicGenEd.put("알고리즘과게임콘텐츠", "algorithmgamecontents");

        subjectTypeMap.put("기초교양", basicGenEd);

        return subjectTypeMap;
    }
}
