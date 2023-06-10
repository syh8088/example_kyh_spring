package hello.itemservice.domain.item;

import hello.itemservice.domain.rfm.DateRange;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {

    @Test
    public void coee() {

        List<Integer> doctalk = Arrays.asList(80773,
                80618,
                80771,
                80646,
                80610,
                80770,
                80677,
                80767,
                80766,
                80765,
                80764,
                80750,
                80694,
                80705,
                80763,
                80629,
                80585,
                80762,
                80633,
                80761,
                80742,
                80760,
                80579,
                80685,
                80759

        );

        List<Integer> okchart = Arrays.asList(80771,
                80770,
                80767,
                80766,
                80765,
                80764,
                80763,
                80762,
                80761,
                80760,
                80759,
                80750,
                80742,
                80705,
                80694,
                80685,
                80677,
                80646,
                80633,
                80629,
                80618,
                80610,
                80585,
                80579);

        List<Integer> integerStream = okchart.stream().filter(data -> {
            return doctalk.stream().noneMatch(doctalkData -> doctalkData.equals(data));
        }).collect(Collectors.toList());

        System.out.println("integerStream = " + integerStream);
    }

    /**
     * 기존 수납데이터 등록 API 초기 등록 이후 (DISPOSE) 호출 시
     *
     * - 기존
     * 2022-01-31 날짜에 호출하고
     * 이후 2022-02-28 에 호출하면 빈 공백이 생긴다.
     *
     * - 수정
     * 만약 빈 공백이 일주일 이라면 일주일 공백 배치 실행
     * 그 이상이면 공백 배치 실행 안해도 된다.
     *
     * * * * * * 보충 설명 * * * * * *
     *
     * 초기 수납데이터() API 호출 이후 (ex: 2022-03-12 까지 수납데이터 보냄)
     *
     * 수납 데이터 API 호출 했었을때
     *
     * ex:
     *
     * 2022-03-01
     * 2022-03-08
     * 2022-03-09
     * 2022-03-13
     * 2022-03-15
     * 2022-03-17
     * 2022-03-19
     * 2022-03-26
     * 2022-03-30
     * 2022-03-31
     *
     * 이렇게 해당 날짜(진료 날짜)의 수납데이터 호출시
     *
     * 진료 날짜 사이에  일주일 이상 공백이 있다면 공백 사이에 배치를 실행하지 않습니다.
     *
     * 무슨말이나면
     *
     * 2022-03-01 (초기 API 호출시 2022-03-12 날짜 까지 배치 실행했으니 배치 실행하지 않습니다.)
     * 2022-03-08 (초기 API 호출시 2022-03-12 날짜 까지 배치 실행했으니 배치 실행하지 않습니다.)
     * 2022-03-09 (초기 API 호출시 2022-03-12 날짜 까지 배치 실행했으니 배치 실행하지 않습니다.)
     * 2022-03-13 ~ 2022-03-15 ~ 2022-03-17 ~ 2022-03-19 (일주일 이하 간격으로 공백 날짜마다 배치 실행)
     *
     * <—  2022-03-19 ~  2022-03-26 사이는 일주일 이상 공백으로 공백 날짜 사이마다 배치 실행 되지 않습니다. —>
     *
     * 2022-03-26 ~ 2022-03-30 ~ 2022-03-31 (일주일 이하 간격으로 공백 날짜마다 배치 실행)
     */
    @Test
    public void disposeClassify() {

        LocalDate lastRfmDate = LocalDate.of(2022, 8, 16);
        LocalDate lastRfmDatePlusDays = lastRfmDate.plusDays(1);

        List<InsertEmrDiagnosisHistoryDto> emrDiagnosisHistoryList = InsertEmrDiagnosisHistoryDto.init();

        Map<LocalDate, List<InsertEmrDiagnosisHistoryDto>> classifiedInsertEmrDiagnosisHistoryDto = new LinkedHashMap<>();

        emrDiagnosisHistoryList.sort(Comparator.comparing((InsertEmrDiagnosisHistoryDto e) -> e.getVisitedDate().toLocalDate()));
        LocalDate endDate = emrDiagnosisHistoryList.get(emrDiagnosisHistoryList.size() - 1).getVisitedDate().toLocalDate();
        LocalDate startDate = emrDiagnosisHistoryList.get(0).getVisitedDate().toLocalDate();

        for (InsertEmrDiagnosisHistoryDto insertEmrDiagnosisHistoryDto : emrDiagnosisHistoryList) {
            LocalDate inquiryDate = insertEmrDiagnosisHistoryDto.getVisitedDate().toLocalDate();
            List<InsertEmrDiagnosisHistoryDto> list = classifiedInsertEmrDiagnosisHistoryDto.get(inquiryDate);

            if (list != null) {
                list.add(insertEmrDiagnosisHistoryDto);
            } else {
                classifiedInsertEmrDiagnosisHistoryDto.put(inquiryDate, new ArrayList<>(Collections.singletonList(insertEmrDiagnosisHistoryDto)));
            }
        }

        Stack<LocalDate> stack = new Stack<>();

        //int compareToDay = Period.between(lastRfmDatePlusDays, startDate).getDays();
        int compareToDay = (int) ChronoUnit.DAYS.between(startDate, lastRfmDatePlusDays);

        if (compareToDay >= 7) {
            stack.push(startDate);
        }
        else if (compareToDay <= 0) {
            stack.push(startDate);
        }
        else {
            stack.push(lastRfmDatePlusDays);
        }

        for (LocalDate localDate : classifiedInsertEmrDiagnosisHistoryDto.keySet()) {

            //int compareToDay2 = Period.between(stack.peek(), localDate).getDays();
            int compareToDay2 = (int) ChronoUnit.DAYS.between(stack.peek(), localDate);

            if (compareToDay2 >= 7) { // 일주일
                stack.push(localDate);
            }
            else {
                List<LocalDate> localDates = new DateRange(stack.peek().plusDays(1), localDate).toList();
                for (LocalDate date : localDates) {
                    stack.push(date);
                }
            }
        }

        List<LocalDate> resultLocalDateList = new ArrayList<>(stack);
        Map<LocalDate, List<InsertEmrDiagnosisHistoryDto>> resultClassifiedInsertEmrDiagnosisHistoryDto = new LinkedHashMap<>();
        for (LocalDate inquiryDate : resultLocalDateList) {
            List<InsertEmrDiagnosisHistoryDto> insertEmrDiagnosisHistoryList = classifiedInsertEmrDiagnosisHistoryDto.get(inquiryDate);
            if (insertEmrDiagnosisHistoryList != null) {
                resultClassifiedInsertEmrDiagnosisHistoryDto.put(inquiryDate, insertEmrDiagnosisHistoryList);
            }
            else {
                resultClassifiedInsertEmrDiagnosisHistoryDto.put(inquiryDate, new ArrayList<>());
            }
        }

        System.out.println("resultClassifiedInsertEmrDiagnosisHistoryDto = " + resultClassifiedInsertEmrDiagnosisHistoryDto);
    }

    @Test
    public void disposeClassifyDisPose() {

        List<LocalDate> localDateList = Arrays.asList(
                LocalDate.of(2022, 3, 1),
                LocalDate.of(2022, 3, 2),
                LocalDate.of(2022, 3, 7),
                LocalDate.of(2022, 3, 30)

//                LocalDate.of(2022, 3, 1),
//                LocalDate.of(2022, 3, 2)

//                LocalDate.of(2022, 3, 1)

//                LocalDate.of(2022, 3, 1),
//                LocalDate.of(2022, 3, 8),
//                LocalDate.of(2022, 3, 9),
//                LocalDate.of(2022, 3, 13),
//                LocalDate.of(2022, 3, 15),
//                LocalDate.of(2022, 3, 17),
//                LocalDate.of(2022, 3, 19),
//                LocalDate.of(2022, 3, 26),
//                LocalDate.of(2022, 3, 30),
//                LocalDate.of(2022, 3, 31)

//                LocalDate.of(2021, 11, 14),
//                LocalDate.of(2021, 11, 25),
//                LocalDate.of(2021, 11, 26),
//                LocalDate.of(2022, 7, 28)


//                LocalDate.of(2022, 8, 18),
//                LocalDate.of(2022, 8, 17),
//                LocalDate.of(2022, 8, 16),
//                LocalDate.of(2022, 8, 15)
        );

        LocalDate lastRfmDatePlusDays = localDateList.stream()
                .max(Comparator.comparing(LocalDate::toEpochDay))
                .get();

        LocalDate startDate = localDateList.stream()
                .min(Comparator.comparing(LocalDate::toEpochDay))
                .get();


        Stack<LocalDate> stack = new Stack<>();

        //int compareToDay = Period.between(lastRfmDatePlusDays, startDate).getDays();
        int compareToDay = (int) ChronoUnit.DAYS.between(startDate, lastRfmDatePlusDays);

        stack.push(startDate);
//        if (compareToDay >= 7) {
//            stack.push(startDate);
//        }
//        else if (compareToDay <= 0) {
//            stack.push(startDate);
//        }
//        else {
//            stack.push(lastRfmDatePlusDays);
//        }

        for (LocalDate localDate : localDateList) {

            //int compareToDay2 = Period.between(stack.peek(), localDate).getDays();
            int compareToDay2 = (int) ChronoUnit.DAYS.between(stack.peek(), localDate);

            if (compareToDay2 >= 7) { // 일주일
                stack.push(localDate);
            }
            else {
                List<LocalDate> localDates = new DateRange(stack.peek().plusDays(1), localDate).toList();
                for (LocalDate date : localDates) {
                    stack.push(date);
                }
            }
        }

        List<LocalDate> resultLocalDateList = new ArrayList<>(stack);
        System.out.println("resultLocalDateList = " + resultLocalDateList);
    }

    @Test
    public void convertStringDateToHyphenWithStringDate() {

        String stringDate = "20220808";
        StringBuilder stringBuilder = new StringBuilder();
        //char[] chars = stringDate.toCharArray();

        int month = 0;
        int day = 0;

        month = Integer.parseInt(stringDate.substring(4, 6));
        day = Integer.parseInt(stringDate.substring(6, 8));

        stringBuilder.append(month);
        stringBuilder.append("월");
        stringBuilder.append(" ");
        stringBuilder.append(day);
        stringBuilder.append("일");

        /*int i = 0;
        for (char aChar : chars) {


                if (i == 6) {
                    stringBuilder.append("-");
                }

                stringBuilder.append(aChar);


            i++;
        }*/

        System.out.println("     stringBuilder.toString(); = " + stringBuilder.toString());

    }

    // 핸드폰번호 정규식
    private final String validateRegExp1 = "\\d{3}\\d{3,4}\\d{4}";

    // 일반 전화번호 정규식1
    private final String validateRegExp2 = "\\d{2,3}\\d{3,4}\\d{4}";

    // 일반 전화번호 정규식3
    private final String validateRegExp3 = "\\d{4}\\d{4}";


    // 핸드폰번호 정규식
    private final String regExp1 = "(\\d{3})(\\d{3,4})(\\d{4})";

    // 일반 전화번호 정규식1
    private final String regExp2 = "(\\d{2,3})(\\d{3,4})(\\d{4})";

    // 일반 전화번호 정규식3
    private final String regExp3 = "(\\d{4})(\\d{4})";

    public String telNumberHyphenAdd(String telNumber) {

        if (Pattern.matches(validateRegExp1, telNumber)) {
            return telNumber.replaceAll(regExp1, "$1-$2-$3");
        }

        if (Pattern.matches(validateRegExp2, telNumber)) {
            return telNumber.replaceAll(regExp2, "$1-$2-$3");
        }

        if (Pattern.matches(validateRegExp3, telNumber)) {
            return telNumber.replaceAll(regExp3, "$1-$2");
        }

        return null;
    }
    private String generateLoginKey() {
        return UUID.randomUUID().toString().substring(0, 8);
    }



    @Getter
    @Setter
    public class Test222 {
        private int id;
        private String test;

        public Test222(int id, String test) {
            this.id = id;
            this.test = test;
        }
    }

    private final String CELL_PHONE_REG_EXP = "^01([0|1|6|7|8|9])?([0-9]{3,4})?([0-9]{4})$";

    private boolean isCellphone(String cellPhone) {
        return cellPhone.matches(CELL_PHONE_REG_EXP);
    }

    @Test
    @DisplayName("파일 Path 이용해서 파일명 추출 하기")
    public void 파일_Path_이용해서_파일명_추출_하기() {
        Path path = Paths.get("/dev/emr_sms/telecommunication_proof/t1est.png");

        // call getFileName() and get FileName path object
        Path fileName = path.getFileName();

        // print FileName
        System.out.println("FileName: " + fileName.toString());
    }


    @Test
    @DisplayName("해당 문자열에 특수 문자 존재시 슬러쉬 첨부하는 기능")
    public void 해당_문자열에_특수_문자_존재시_슬러쉬_첨부하는_기능() {
//        String message = "{mm월dd일(w) hh:mm} ㄷㅈㄹㅈㄷㄹㅈㄷㄹ";
//        String replace = "{mm월dd일(w) hh:mm}";

        String message = "{mm월dd일(w) hh:mm!@#$%%*%%^&}₩<>₩.+-=-₩ ㄷㅈㄹㅈㄷㄹㅈㄷㄹ";
        String replace = "{mm월dd일(w) hh:mm!@#$%%*%%^&}₩<>₩.+-=-₩";

        replace = this.addSlush(replace, "{", "}");
        replace = this.addSlush(replace, "(", ")");
        replace = this.addSlush(replace, "$", "$");
        replace = this.addSlush(replace, "^", "^");
        replace = this.addSlush(replace, "*", "*");
        replace = this.addSlush(replace, "|", "|");

//        str = str.replaceAll("[^a-zA-Z]+","");
//        str = str.replaceAll("[^a-zA-Z0-9]+","");
//
//
//
//        str = str.replaceAll("\\^([0-9]+)", "<sup>$1</sup>");

        message = message.replaceAll(replace, "치환해보자!!!");
        System.out.println("message = " + message);
    }


    public String addSlush(String str, final String open, final String close) {

        int INDEX_NOT_FOUND = -1;

        if (StringUtils.isBlank(str)) {
            return str;
        }

        String copyStr = str;

        while (copyStr.length() > 2) {

            final int start = copyStr.indexOf(open);
            if (start != INDEX_NOT_FOUND) {
                final int end = copyStr.indexOf(close, start + open.length());
                str = new StringBuffer(str).insert(start, "\\").toString();

                if (end != INDEX_NOT_FOUND) {

                    str = new StringBuffer(str).insert(end + 1, "\\").toString();
                    copyStr = copyStr.substring(end);
                }
                else {
                    copyStr = copyStr.substring(start + 1);
                }
            }
            else {
                copyStr = "";
            }
        }

        return str;
    }

    @Test
    @DisplayName("특정 문자열에서 {문자} 추출하기")
    public void 특정_문자열에서_문자_추출하기() {

        String text = "안녕하세요 ㅈㄷㅈㄷㅈㄷ {이름} ㅈㄷㄹㅈㄷㄹㄷㅈㄹㄷㅈ {소속}wdwdwdwvwfq";

        List<String> strings = substringBetween(text, "{", "}");
        System.out.println("strings = " + strings);
    }

    public List<String> substringBetween(String str, final String open, final String close) {

        List<String> strList = new ArrayList<>();

        int INDEX_NOT_FOUND = -1;

        if (str == null || open == null || close == null) {
            return strList;
        }

        while (str.length() > 2) {

            final int start = str.indexOf(open);
            if (start != INDEX_NOT_FOUND) {
                final int end = str.indexOf(close, start + open.length());
                if (end != INDEX_NOT_FOUND) {

                    String substring = str.substring(start + open.length(), end);
                    strList.add(substring);

                    str = str.substring(end);
                }
            }
            else {
                str = "";
            }
        }

        return strList;
    }

    @Test
    public void ampm23r23r() throws ScriptException {

        boolean cellphone = this.isCellphone("01089004488");
        boolean cellphone1 = this.isCellphone("01122221111");
        boolean cellphone2 = this.isCellphone("010890044882");
        boolean cellphone3 = this.isCellphone("01000488d2");



        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        String foo = "40+2";
        System.out.println(engine.eval(foo));


        List<Test222> employee = Arrays.asList(new Test222(1, "John"), new Test222(2, "John"), new Test222(3, "Alice"));

        employee =  employee.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(Test222::getTest))),
                        ArrayList::new));



        String loginKey = generateLoginKey();

        String test1 = telNumberHyphenAdd("01022223333");
        String test2 = telNumberHyphenAdd("021111222");
        String test3 = telNumberHyphenAdd("15883333");

        int t = 0243;
        StringBuilder stringBuilder = new StringBuilder();

        String minute = "";
        int hour = 0;
        String time = null;
        time = "1233";

        if (time.length() < 4) time = "0" + time;
        hour = Integer.parseInt(time.substring(0, 2));
        minute = time.substring(2, 4);

        if (hour >= 12 && hour <= 24) {
            hour = (hour == 12) ? 12 : hour - 12;

            stringBuilder.append("오후 ");
            stringBuilder.append(hour);
            stringBuilder.append(":");
            stringBuilder.append(minute);
        }
        else {
            stringBuilder.append("오전 ");
            stringBuilder.append(hour);
            stringBuilder.append(":");
            stringBuilder.append(minute);
        }


        System.out.println("stringBuilder = " + stringBuilder.toString());
      //  return stringBuilder.toString();
    }



    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        // given
        Item item = new Item("itemA", 10000, 10);

        // when
        Item savedItem = itemRepository.save(item);

        // then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    void findAll() {
        // given
        Item item1 = new Item("item1", 10000, 10);
        Item item2 = new Item("item2", 20000, 20);

        itemRepository.save(item1);
        itemRepository.save(item2);

        // when
        List<Item> result = itemRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(item1, item2);
    }

    @Test
    void updateItem() {
        // given
        Item item = new Item("item1", 10000, 10);

        Item savedItem = itemRepository.save(item);
        Long itemId = savedItem.getId();

        //when
        Item updateParam = new Item("item2", 20000, 30);
        itemRepository.update(itemId, updateParam);
        Item findItem = itemRepository.findById(itemId);

        //then
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());

        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }


    @Test
    void test() {

        int size = arrayY2.length;

        int[] arrayX = new int[size];
        for (int e = 1; e <= size; e++) {
            arrayX[e - 1] = e;
        }

        //Math.round

        Long[] arrayY = new Long[size];
        long totalSumCustomerTransaction = 0L;
        long averageCustomerTransaction = 0L;
        int i = 0;
        for (Double aDouble : arrayY2) {
            arrayY[i] = Math.round(aDouble);
            long customerTransaction = Math.round(aDouble);
            totalSumCustomerTransaction += customerTransaction;

            i++;
        }



        averageCustomerTransaction = totalSumCustomerTransaction / size;
        long avgX = average(arrayX);

        trendCalculate(arrayX, arrayY, avgX, averageCustomerTransaction);
    }

    public static Long[] trendCalculate(int[] arrayX, Long[] arrayY, long avgX, long avgY) {

        Long[] result = new Long[arrayX.length];

        long xy = 0;
        long x2 = 0;

        int rc = arrayY.length;

        for (int i = 0; i < rc; i++) {
            int tempX = arrayX[i];
            Long tempY = arrayY[i];

            long temp_xy = (tempX - avgX) * (tempY - avgY);
            long temp_x2 = (tempX - avgX) * (tempX - avgX);

            xy = (xy + temp_xy);
            x2 = (x2 + temp_x2);
        }

        // 기울기 m
        double m = (double) xy / x2;

        // 상수
        double b = avgY - (m * avgX);

        // 현재 레코드 x값
        //int x = x;

        for (int i = 1; i <= arrayX.length; i++) {
            double answer = trendCalculate(m, b, i);
            result[i - 1] = Math.round(answer);
        }

        return result;
    }

    public static double trendCalculate(double m, double b, int x) {
        // 선을 그리기 위한 y 값 = (m * x) + b
        return (m * x) + b;
    }

    public static Long sum(int[] array) {
        long result = 0L;

        for (int value : array) {
            result += value;
        }

        return result;
    }

    public static long average(int[] array) {

        long sum = sum(array);
        return sum / array.length;
    }


    int[] arrayX =  {
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10,
            11,
            12,
            13,
            14,
            15,
            16,
            17,
            18,
            19,
            20,
            21,
            22,
            23,
            24,
            25,
            26,
            27,
            28,
            29,
            30,
            31,
            32,
            33,
            34,
            35,
            36,
            37,
            38,
            39,
            40,
            41,
            42,
            43,
            44,
            45,
            46,
            47,
            48,
            49,
            50,
            51,
            52,
            53,
            54,
            55,
            56,
            57,
            58,
            59,
            60,
            61,
            62,
            63,
            64,
            65,
            66,
            67,
            68,
            69,
            70,
            71,
            72,
            73,
            74,
            75,
            76,
            77,
            78,
            79,
            80,
            81,
            82,
            83,
            84,
            85,
            86,
            87,
            88,
            89,
            90,
            91,
            92,
            93,
            94,
            95,
            96,
            97,
            98,
            99,
            100,
            101,
            102,
            103,
            104,
            105,
            106,
            107,
            108,
            109,
            110,
            111,
            112,
            113,
            114,
            115,
            116,
            117,
            118,
            119,
            120,
            121,
            122,
            123,
            124,
            125,
            126,
            127,
            128,
            129,
            130,
            131,
            132,
            133,
            134,
            135,
            136,
            137,
            138,
            139,
            140,
            141,
            142,
            143,
            144,
            145,
            146,
            147,
            148,
            149,
            150,
            151,
            152,
            153,
            154,
            155,
            156,
            157,
            158,
            159,
            160,
            161,
            162,
            163,
            164,
            165,
            166,
            167,
            168,
            169,
            170,
            171,
            172,
            173,
            174,
            175,
            176,
            177,
            178,
            179,
            180,
            181,
            182,
            183,
            184,
            185,
            186,
            187,
            188,
            189,
            190,
            191,
            192,
            193,
            194,
            195,
            196,
            197,
            198,
            199,
            200,
            201,
            202,
            203,
            204,
            205,
            206,
            207,
            208,
            209,
            210,
            211,
            212,
            213,
            214,
            215,
            216,
            217,
            218,
            219,
            220,
            221,
            222,
            223,
            224,
            225,
            226,
            227,
            228,
            229,
            230,
            231,
            232,
            233,
            234,
            235,
            236,
            237,
            238,
            239,
            240,
            241,
            242,
            243,
            244,
            245,
            246,
            247,
            248,
            249,
            250,
            251,
            252,
            253,
            254,
            255,
            256,
            257,
            258,
            259,
            260,
            261,
            262,
            263,
            264,
            265,
            266,
            267,
            268,
            269,
            270,
            271,
            272,
            273,
            274,
            275,
            276,
            277,
            278,
            279,
            280,
            281,
            282,
            283,
            284,
            285,
            286,
            287,
            288,
            289,
            290,
            291,
            292,
            293,
            294,
            295,
            296,
            297,
            298,
            299,
            300,
            301,
            302,
            303,
            304,
            305,
            306,
            307,
            308,
            309,
            310,
            311,
            312,
            313,
            314,
            315,
            316,
            317,
            318,
            319,
            320,
            321,
            322,
            323,
            324,
            325,
            326,
            327,
            328,
            329,
            330,
            331,
            332,
            333,
            334,
            335,
            336,
            337,
            338,
            339,
            340,
            341,
            342,
            343,
            344,
            345,
            346,
            347,
            348,
            349,
            350,
            351,
            352,
            353,
            354,
            355,
            356,
            357,
            358,
            359,
            360,
            361,
            362,
            363,
            364,
            365,
            366
    };

    Double[] arrayY2 =  {
            1634783.8235,
            1645848.5714,
            1680273.2394,
            1682023.9437,
            1618025.9740,
            1618025.9740,
            1618935.0649,
            1618935.0649,
            1653875.6410,
            1724166.6667,
            1775000.0000,
            1768963.8554,
            1768963.8554,
            1796803.5294,
            1796803.5294,
            1797627.0588,
            1797221.8391,
            1842115.7303,
            1826922.5806,
            1863019.1489,
            1863853.6082,
            1863853.6082,
            1876907.0707,
            1797181.7308,
            1779862.0370,
            1751500.9091,
            1799030.0000,
            1809658.9286,
            1810283.9286,
            1838700.0000,
            1845943.5897,
            1845943.5897,
            1832257.3770,
            1889673.3871,
            1918275.2000,
            1918275.2000,
            1935346.8750,
            1926954.5455,
            1888725.9259,
            1910383.0882,
            1917218.8406,
            1904468.3453,
            1904468.3453,
            1924714.1844,
            1899060.4167,
            1919217.2414,
            1948207.4830,
            1968179.7297,
            1989260.8108,
            1989260.8108,
            1995055.3333,
            2023366.2252,
            2037609.1503,
            2037609.1503,
            2037609.1503,
            2037609.1503,
            2037609.1503,
            2051575.4839,
            2054318.9873,
            2043617.3913,
            2127756.1728,
            2156604.2945,
            2142388.5542,
            2142388.5542,
            2142990.9639,
            2187128.4024,
            2207829.2398,
            2237623.1214,
            2235250.2857,
            2213289.3855,
            2213289.3855,
            2213289.3855,
            2207488.4615,
            2207873.0769,
            2205527.1739,
            2192818.1818,
            2210515.9574,
            2210515.9574,
            2206078.3069,
            2204989.0052,
            2226964.5833,
            2216151.2953,
            2236975.5102,
            2226026.3959,
            2226026.3959,
            2248622.2222,
            2248927.0000,
            2267551.4851,
            2287662.0690,
            2291440.9756,
            2282807.6923,
            2282807.6923,
            2326909.0909,
            2314244.1315,
            2306655.5556,
            2310976.4977,
            2293342.0091,
            2304693.2432,
            2304693.2432,
            2306794.1964,
            2307185.3982,
            2310465.7895,
            2348488.6463,
            2351835.0649,
            2361906.8966,
            2361906.8966,
            2362155.6034,
            2385726.1803,
            2449785.5319,
            2463684.7458,
            2453753.5865,
            2450404.1841,
            2450404.1841,
            2440458.2645,
            2437913.5246,
            2452115.9184,
            2468123.5772,
            2477038.0567,
            2485033.8710,
            2485033.8710,
            2484170.2811,
            2485666.2651,
            2485666.2651,
            2485884.3373,
            2466156.1753,
            2463690.5138,
            2463690.5138,
            2478597.2441,
            2485943.5294,
            2486247.4510,
            2488608.5603,
            2470433.9768,
            2488036.0153,
            2488036.0153,
            2506707.6336,
            2507799.2424,
            2507799.2424,
            2522184.9057,
            2536063.6704,
            2535291.0781,
            2535291.0781,
            2545419.6296,
            2537923.1618,
            2557372.1612,
            2565337.9562,
            2573558.1818,
            2591407.6087,
            2591407.6087,
            2595337.5451,
            2571298.2143,
            2571298.2143,
            2581130.9609,
            2586579.0780,
            2591145.4225,
            2591145.4225,
            2606790.5263,
            2609652.6132,
            2617052.4306,
            2627996.8858,
            2635294.4828,
            2647956.7010,
            2647956.7010,
            2648197.2509,
            2648248.7973,
            2660694.8630,
            2657212.2449,
            2665526.7797,
            2672462.8378,
            2672462.8378,
            2680299.6633,
            2689484.8993,
            2672780.0000,
            2664498.3389,
            2675592.7152,
            2676120.1987,
            2676120.1987,
            2670783.8816,
            2671121.8954,
            2665893.1818,
            2678010.0324,
            2685080.9677,
            2699437.6206,
            2699662.7010,
            2691463.0573,
            2691712.1019,
            2701891.1392,
            2693998.7382,
            2705760.5016,
            2683036.0248,
            2683036.0248,
            2682122.2222,
            2705580.9816,
            2699050.1529,
            2696309.1463,
            2704832.0242,
            2698016.2162,
            2698016.2162,
            2708224.2515,
            2720498.2090,
            2713704.1667,
            2716611.2426,
            2725660.5882,
            2712859.0116,
            2712859.0116,
            2707234.7826,
            2692431.0345,
            2694452.8736,
            2695670.2857,
            2676863.7394,
            2677008.1690,
            2677008.1690,
            2678346.0894,
            2685725.3482,
            2697042.7778,
            2700429.2818,
            2711360.9890,
            2702876.0218,
            2703066.7575,
            2680784.6774,
            2684147.0588,
            2680109.0426,
            2701642.8571,
            2715484.1689,
            2697432.8982,
            2697432.8982,
            2711599.7396,
            2705874.8052,
            2699382.9016,
            2700868.4755,
            2688858.8689,
            2683459.4388,
            2683459.4388,
            2688654.1985,
            2687642.0253,
            2687364.9123,
            2694221.5000,
            2702191.0224,
            2690440.9877,
            2690440.9877,
            2690440.9877,
            2694585.0123,
            2702907.1078,
            2704990.4878,
            2696392.2330,
            2706400.9685,
            2706570.4600,
            2714259.0361,
            2711596.8900,
            2710849.1686,
            2720065.1659,
            2726428.1324,
            2720186.5566,
            2720186.5566,
            2709061.0329,
            2704263.1702,
            2707946.1538,
            2715509.0698,
            2715671.8605,
            2712203.7037,
            2712203.7037,
            2716041.4747,
            2721797.7011,
            2709569.7941,
            2714662.1005,
            2715342.4658,
            2734519.5900,
            2734519.5900,
            2737512.4434,
            2745080.1354,
            2748875.0000,
            2753484.2697,
            2762451.0022,
            2769376.4967,
            2769686.9180,
            2769686.9180,
            2769686.9180,
            2769686.9180,
            2771982.9646,
            2773019.3407,
            2772821.1790,
            2772821.1790,
            2774374.1304,
            2775561.9048

    };
}