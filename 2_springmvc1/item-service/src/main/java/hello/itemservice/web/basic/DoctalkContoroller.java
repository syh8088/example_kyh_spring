package hello.itemservice.web.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.itemservice.domain.rfm.dto.EmrClientSmsYn;
import hello.itemservice.domain.rfm.dto.EmrDiagnosisType;
import hello.itemservice.domain.rfm.dto.EmrMemberDto;
import hello.itemservice.domain.rfm.dto.InsertEmrDiagnosisHistoryRequestDto;
import hello.itemservice.domain.rfm.dto.InsertEmrDiagnosisRequestDto;
import hello.itemservice.domain.rfm.bitComputer.EmrMemberBitComputerDto;
import hello.itemservice.utill.CsvUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctalks")
@RequiredArgsConstructor
public class DoctalkContoroller {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /*    @GetMapping("/batch/export" , produces="text/csv")
    public ResponseEntity<?> getBatchDetails(HttpServletRequest request) {

        ApplicationRequest appRequest = ApplicationServiceMapper.mapRequestFromHttpRequest(request);
        ApplicationResponse response = appService.getDBDetails(appRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }*/
    private Long sum(int[] array) {
        long result = 0L;

        for (int value : array) {
            result += value;
        }

        return result;
    }

    private double doubleSum(Double[] array) {
        int result = 0;

        for (double value : array) {
            result += value;
        }

        return result;
    }

    private long average(int[] array) {

        long sum = sum(array);
        return sum / array.length;
    }

    @GetMapping("trend")
    public String trend() {

        //List<Integer> integers = Arrays.asList(19, 22, 26, 34, 36, 38, 39, 45, 45, 52, 60, 69, 69, 78, 85, 94, 100, 103, 112, 115);
        // int[] arrayX = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
        //  int[] arrayY = { 19, 22, 26, 34, 36, 38, 39, 45, 45, 52, 60, 69, 69, 78, 85, 94, 100, 103, 112, 115 };
        //int[] arrayY = { 19, 22, 26, 34, 36, 38, 39, 45, 45, 52, 34, 32, 69, 12, 85, 22, 76, 32, 35, 54 };


        //int[] arrayX = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
/*        int[] arrayY =  { 1592398,
        1597197,
        1666080,
        2484340,
        2669994,
        5081937,
        3360840,
        6989238,
        7729650,
        6038549,
        5484312,
        8551452,
        8238174,
        8831025,
        6924096,
        13085376,
        8230572,
        12352014,
        8246180,
        12531645,
        11636328,
        14015464,
        19252800,
        19362,
        12495600,
        3244,
        174422,
        222,
        1480,
        171 };*/


       /* int[] arrayY =  { 1592398,
        1597197,
        1666080,
        2484340,
        2669994,
        5081937,
        3360840,
        6989238,
        7729650,
        6038549,
        5484312,
        8551452,
        8238174,
        8831025,
        6924096,
        13085376,
        8230572,
        12352014,
        8246180,
        12531645,
        11636328,
        14015464,
        19252800,
        19362323,
        12495600,
        3244444,
        174422,
        222222,
        14803434,
        17143535 };*/

        Long[] result = new Long[arrayX.length];

        long avgX = average(arrayX);
        long avgY = average(arrayY);

        double v = Arrays.stream(arrayX).average().orElse(0.0);
        double v1 = Arrays.stream(arrayY).average().orElse(0.0);


        int xy = 0;
        int x2 = 0;

        int rc = arrayY.length;

        for (int i = 0; i < rc; i++) {
            int tempX = arrayX[i];
            int tempY = arrayY[i];

            long temp_xy = (tempX - avgX) * (tempY - avgY);
            long temp_x2 = (tempX - avgX) * (tempX - avgX);

            xy = (int) (xy + temp_xy);
            x2 = (int) (x2 + temp_x2);
        }

        System.out.println("xy = " + xy);
        System.out.println("x2 = " + x2);

        // 기울기 m
        double m = (double) xy / x2;
        System.out.println("m = " + m);

        // 상수
        double b = avgY - (m * avgX);
        System.out.println("b = " + b);

        // 현재 레코드 x값
        //int x = x;

        for (int i = 1; i <= arrayX.length; i++) {
            double answer = trendCalculate(m, b, i);
            result[i - 1] = Math.round(answer);
        }

        System.out.println("result = " + result);

        // 기울기
        //double x = arrayX[arrayX.length - 1] - arrayX[0];

        //double sum = doubleSum(result);
        //double average = sum / result.length;
        //double x = sum - average;
        long x = result[result.length - 1] - 0;

        long y = result[result.length - 1] - result[0];

        long slope = y / x;
        System.out.println("slope = " + slope);

        // 각도
        double radian = Math.atan2(y, x);
        double degree = radian * 180 / Math.PI;

        System.out.println("degree = " + degree);

        // 성장률 계산
        double growthRate = growthRateCalculate(result[result.length - 1], result[0], result.length);
        System.out.println("growthRate = " + growthRate);


        double growthRate1 = growthRateCalculate(373714L, 257021L, 366);

        return null;
    }

    private double growthRateCalculate(Long presentValue, Long pastValue, int size) {
        double v2 = (double) presentValue / pastValue;
        double v3 = Math.pow(v2, ((double) 1 / size)) - 1;
        return v3 * 100;
    }

    private double trendCalculate(double m, double b, int x) {
//
//        BigDecimal bigDecimalB = new BigDecimal(String.valueOf(b));
//        BigDecimal bigDecimal = new BigDecimal(String.valueOf(m * x));
//        BigDecimal add = bigDecimal.add(bigDecimalB);
//        double result = add.doubleValue();
        // 선을 그리기 위한 y 값 = (m * x) + b
        double result = ((double) m * x) + b;
        System.out.println("result = " + result);

        return result;
    }


    private void insertEmrDiagnoses(List<InsertEmrDiagnosisHistoryRequestDto> emrDiagnosisHistoryList) {

        InsertEmrDiagnosisRequestDto insertEmrDiagnosisRequestDto = new InsertEmrDiagnosisRequestDto();
        //insertEmrDiagnosisRequestDto.setInstitutionNumber("132133"); // 실서버 전용
        //insertEmrDiagnosisRequestDto.setCompanyName("닥톡병원"); // 실서버 전용

        insertEmrDiagnosisRequestDto.setInstitutionNumber("888888885"); // 개발 전용
        insertEmrDiagnosisRequestDto.setCompanyName("okchart_test"); // 개발 전용

        insertEmrDiagnosisRequestDto.setEmrClientId("37952315");
        insertEmrDiagnosisRequestDto.setBusinessNumber("8888802888899999");
        insertEmrDiagnosisRequestDto.setCompanyTel("07074304151");
        insertEmrDiagnosisRequestDto.setBlockCallNumber("0805705575");





        /*insertEmrDiagnosisRequestDto.setCompanyTel("02888899999");
        insertEmrDiagnosisRequestDto.setBusinessNumber("8888802888899999");
        insertEmrDiagnosisRequestDto.setEmrClientId("test132133");
        insertEmrDiagnosisRequestDto.setEmrKakaoSenderKey("test132133");
        insertEmrDiagnosisRequestDto.setType(EmrDiagnosisType.DISPOSE);
        insertEmrDiagnosisRequestDto.setBlockCallNumber("080-001-7361");*/
        insertEmrDiagnosisRequestDto.setEmrDiagnosisHistoryList(emrDiagnosisHistoryList);

        //String uri = "https://api-v2.doctalk.co.kr/api/v1/emr-apis/diagnoses";
        //String doctalkApiAccessKey = "3HFACXqV-ebod-4owT-8PqL-Uo0hY0A0staK";

        String uri = "http://localhost:9201/api/v1/emr-apis/diagnoses";
        //String uri = "https://dev-api.doctalk.co.kr/api/v1/emr-apis/diagnoses";

        //String doctalkApiAccessKey = "4dyr74tL-4iaJ-4obt-9let-zqT6j8Dabva0"; // 개발 서버 전용 전능
        //String doctalkApiAccessKey = "3NKFVHT3-16PC-5abX-cKyX-9kXlnTpPajJk"; //  개발 서버 전용 비트 컴퓨터
        String doctalkApiAccessKey = "BO8h5rV7-dGcU-4pUk-9tx1-Ymb5K4FZEGJa"; //  개발 서버 전용 OK 차트

        Mono<ResponseEntity<String>> result =
                WebClient.create()
                        .post()
                        .uri(uri)
                        .header("Doctalk-API-Access-Key", doctalkApiAccessKey)
                        .bodyValue(insertEmrDiagnosisRequestDto)
                        .exchange()
                        .flatMap(responseResult -> responseResult.toEntity(String.class))
                        .flatMap(
                                entity -> {
                                    return Mono.just(entity);
                                });

        result.block();


        System.out.println("result = " + result);
    }

    @PostMapping(value = "upload-bete-computer", consumes = "multipart/form-data")
    public void uploadMultipartByBeteComputer(@RequestParam("file") MultipartFile file) throws IOException {

        List<EmrMemberBitComputerDto> emrMembers = CsvUtils.read(EmrMemberBitComputerDto.class, file.getInputStream());
        System.out.println("emrMembers = " + emrMembers);

        List<InsertEmrDiagnosisHistoryRequestDto> collect = emrMembers.stream()
                .map(data -> {
                    InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto = new InsertEmrDiagnosisHistoryRequestDto();

                    insertEmrDiagnosisHistoryRequestDto.setDiagnosisId(data.getDiagnosisId());
                    insertEmrDiagnosisHistoryRequestDto.setEmrClientId(data.getEmrClientName());
                    insertEmrDiagnosisHistoryRequestDto.setEmrClientName(data.getEmrClientName());
                    insertEmrDiagnosisHistoryRequestDto.setMedicalPrice(data.getMedicalPrice());
                    insertEmrDiagnosisHistoryRequestDto.setEmrClientCellPhone("01011111111");
                    insertEmrDiagnosisHistoryRequestDto.setPaymentId("1");
                    insertEmrDiagnosisHistoryRequestDto.setVisitedDate(data.getVisitedDate().substring(0, 19));

                    return insertEmrDiagnosisHistoryRequestDto;
                }).collect(Collectors.toList());

        // result.json 파일로 저장
        objectMapper.writeValue(new File("result.json"), collect);
    }

    @PostMapping(value = "upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadMultipartByJeonNeung(@RequestParam("file") MultipartFile file) throws IOException {
        List<EmrMemberDto> emrMembers = CsvUtils.read(EmrMemberDto.class, file.getInputStream());

        List<InsertEmrDiagnosisHistoryRequestDto> collect = emrMembers
                .stream()
                .filter(data -> {
                    String visitedDate = String.valueOf(data.getVisitedDate());
                    if (visitedDate.length() == 8) return true;
                    else return false;
                })
                .map(data -> {
                    InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto = new InsertEmrDiagnosisHistoryRequestDto();

                    insertEmrDiagnosisHistoryRequestDto.setEmrClientId("clientIdV1_" + data.getEmrClientId());
                    insertEmrDiagnosisHistoryRequestDto.setEmrClientName("clientIdV1_" + data.getEmrClientId() + "_name");
                    //insertEmrDiagnosisHistoryRequestDto.setEmrClientCellPhone("clientIdV1_" + data.getEmrClientId() + "_cellPhone");
                    insertEmrDiagnosisHistoryRequestDto.setEmrClientCellPhone("01011112222");

                    insertEmrDiagnosisHistoryRequestDto.setDiagnosisId(data.getDiagnosisId());
                    insertEmrDiagnosisHistoryRequestDto.setPaymentId(data.getPaymentId());
                    insertEmrDiagnosisHistoryRequestDto.setMedicalPrice(data.getMedicalPrice());

                    String visitedDate = String.valueOf(data.getVisitedDate());
                    String visitedTime = String.valueOf(data.getVisitedTime());

                    int visitedYear = Integer.parseInt(visitedDate.substring(0, 4));
                    int visitedMonth = Integer.parseInt(visitedDate.substring(4, 6));
                    int visitedDay = Integer.parseInt(visitedDate.substring(6, 8));

                    int visitedTime1 = 0;
                    int visitedTime2 = 0;

                    if (visitedTime.length() == 4) {
                        visitedTime1 = Integer.parseInt(visitedTime.substring(0, 2));
                        visitedTime2 = Integer.parseInt(visitedTime.substring(2, 4));
                    } else if (visitedTime.length() == 3) {
                        visitedTime1 = Integer.parseInt(visitedTime.substring(0, 1));
                        visitedTime2 = Integer.parseInt(visitedTime.substring(1, 3));
                    } else if (visitedTime.length() == 2) {
                        visitedTime1 = Integer.parseInt(visitedTime.substring(0, 1));
                        visitedTime2 = Integer.parseInt(visitedTime.substring(1, 2));
                    }

                    insertEmrDiagnosisHistoryRequestDto.setVisitedDate(LocalDateTime.of(visitedYear, visitedMonth, visitedDay, visitedTime1, visitedTime2, 22).toString().replace("T", " "));

                    return insertEmrDiagnosisHistoryRequestDto;
                }).collect(Collectors.toList());

        insertEmrDiagnoses(collect);
        System.out.println("emrMembers = ");

        //DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //objectMapper.setDateFormat(df);

        // result.json 파일로 저장
        //objectMapper.writeValue(new File("result.json"), collect);

        // byte[] 로 저장
        //byte[] jsonBytes = objectMapper.writeValueAsBytes(emrMembers);
        // string 으로 저장
        //String jsonString = objectMapper.writeValueAsString(emrMembers);

        //System.out.println("jsonString = " + jsonString);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "upload2")
    public ResponseEntity<?> uploadRFM() throws IOException {

        List<InsertEmrDiagnosisHistoryRequestDto> insertEmrDiagnosisHistoryRequestList = new ArrayList<>();

        for (int i = 0; i < 71; i++) {

            String emrClientId = "testClientId" + i;
            String emrClientName = "testName" + i;
            String emrClientCellPhone = "01089004488";
            EmrClientSmsYn emrClientSmsYn = EmrClientSmsYn.Y;
            int medicalPrice = i * 2;
            String diagnosisId = "testDiagnosisId" + i;

            String medicalItem = "보험치료" + i;
            String diagnosticName = "습진 NOS" + i;
            String prescriptionName = "경혈이제" + i;

            String emrClientVip = "1개월" + i % 2;
            String emrClientSuggest = "소개" + i % 2;
            String emrClientMainDoctor = "정사원" + i % 2;

            String chartNumber = "차트 넘버" + i;

            int plusDay = 0;
            if (i >= 50 && i <= 60) {
                plusDay = 30;
            }
            else {
                plusDay = i;
            }

            LocalDateTime localDateTime = LocalDateTime.of(2022, 1, 1, 10, 10, 10).plusDays(plusDay);
            String visitedDate = localDateTime.toString().replace("T", " ");

            InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto = InsertEmrDiagnosisHistoryRequestDto.builder()
                    .emrClientId(emrClientId)
                    .emrClientName(emrClientName)
                    .emrClientCellPhone(emrClientCellPhone)
                    .emrClientSmsYn(emrClientSmsYn)
                    .medicalPrice(medicalPrice)
                    .diagnosisId(diagnosisId)
                    .visitedDate(visitedDate)
                    .emrClientVip(emrClientVip)
                    .emrClientSuggest(emrClientSuggest)
                    .emrClientMainDoctor(emrClientMainDoctor)
                    .medicalItem(medicalItem)
                    .diagnosticName(diagnosticName)
                    .prescriptionName(prescriptionName)
                    .emrClientChartNumber(chartNumber)
                    .build();

            insertEmrDiagnosisHistoryRequestList.add(insertEmrDiagnosisHistoryRequestDto);
        }


        insertEmrDiagnoses(insertEmrDiagnosisHistoryRequestList);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "upload3")
    public ResponseEntity<?> uploadRFM3() throws IOException {

        List<InsertEmrDiagnosisHistoryRequestDto> insertEmrDiagnosisHistoryRequestList = new ArrayList<>();

        InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto1 = InsertEmrDiagnosisHistoryRequestDto.builder()
                .emrClientId("dumid1")
                .emrClientName("dumname1")
                .emrClientCellPhone("01089004488")
                .emrClientSmsYn(EmrClientSmsYn.Y)
                .medicalPrice(1000)
                .diagnosisId("dum-1")
                .visitedDate(LocalDateTime.of(2022, 3, 1, 10, 10, 10).toString().replace("T", " "))
                .emrClientVip("6개월_ADD_ON")
                .emrClientSuggest("온라인")
                .emrClientMainDoctor("최현정")
                .medicalItem("보험치료")
                .diagnosticName("습진 NOS")
                .prescriptionName("경혈이제")
                .emrClientChartNumber("차트 넘버")
                .build();

        InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto2 = InsertEmrDiagnosisHistoryRequestDto.builder()
                .emrClientId("dumid2")
                .emrClientName("dumname2")
                .emrClientCellPhone("01089004488")
                .emrClientSmsYn(EmrClientSmsYn.Y)
                .medicalPrice(1000)
                .diagnosisId("dum-2")
                .visitedDate(LocalDateTime.of(2022, 3, 1, 10, 10, 10).toString().replace("T", " "))
                .emrClientVip("1개월")
                .emrClientSuggest("소개")
                .emrClientMainDoctor("정사원")
                .medicalItem("보험치료")
                .diagnosticName("습진 NOS")
                .prescriptionName("경혈이제")
                .emrClientChartNumber("차트 넘버")
                .build();

        InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto3 = InsertEmrDiagnosisHistoryRequestDto.builder()
                .emrClientId("dumid3")
                .emrClientName("dumname3")
                .emrClientCellPhone("01089004488")
                .emrClientSmsYn(EmrClientSmsYn.Y)
                .medicalPrice(1000)
                .diagnosisId("dum-3")
                .visitedDate(LocalDateTime.of(2022, 3, 8, 10, 10, 10).toString().replace("T", " "))
                .emrClientVip("6개월")
                .emrClientSuggest("온라인")
                .emrClientMainDoctor("김탁구")
                .medicalItem("보험치료")
                .diagnosticName("습진 NOS")
                .prescriptionName("경혈이제")
                .emrClientChartNumber("차트 넘버")
                .build();

        InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto4 = InsertEmrDiagnosisHistoryRequestDto.builder()
                .emrClientId("dumid")
                .emrClientName("dumname")
                .emrClientCellPhone("01089004488")
                .emrClientSmsYn(EmrClientSmsYn.Y)
                .medicalPrice(1000)
                .diagnosisId("dum-4")
                .visitedDate(LocalDateTime.of(2022, 3, 8, 10, 10, 10).toString().replace("T", " "))
                .emrClientVip("치료종결")
                .emrClientSuggest("NEW_ITEM")
                .emrClientMainDoctor("박연경")
                .medicalItem("보험치료")
                .diagnosticName("습진 NOS")
                .prescriptionName("경혈이제")
                .emrClientChartNumber("차트 넘버")
                .build();

        InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto5 = InsertEmrDiagnosisHistoryRequestDto.builder()
                .emrClientId("dumid")
                .emrClientName("dumname")
                .emrClientCellPhone("01089004488")
                .emrClientSmsYn(EmrClientSmsYn.Y)
                .medicalPrice(1000)
                .diagnosisId("dum-5")
                .visitedDate(LocalDateTime.of(2022, 3, 9, 10, 10, 10).toString().replace("T", " "))
                .emrClientVip("6개월_ADD_ON_ITEM")
                .emrClientSuggest("NEW_ITEM")
                .emrClientMainDoctor("박연경")
                .medicalItem("보험치료")
                .diagnosticName("습진 NOS")
                .prescriptionName("경혈이제")
                .emrClientChartNumber("차트 넘버")
                .build();

        insertEmrDiagnosisHistoryRequestList.add(insertEmrDiagnosisHistoryRequestDto1);
        insertEmrDiagnosisHistoryRequestList.add(insertEmrDiagnosisHistoryRequestDto2);
        insertEmrDiagnosisHistoryRequestList.add(insertEmrDiagnosisHistoryRequestDto3);
        insertEmrDiagnosisHistoryRequestList.add(insertEmrDiagnosisHistoryRequestDto4);
        insertEmrDiagnosisHistoryRequestList.add(insertEmrDiagnosisHistoryRequestDto5);

        int plusDay = 0;
        for (int i = 0; i < 50; i++) {

            String emrClientId = "testClientId_DISPOSE" + i;
            String emrClientName = "testName" + i;
            String emrClientCellPhone = "01089004488";
            EmrClientSmsYn emrClientSmsYn = EmrClientSmsYn.Y;
            int medicalPrice = i * 2;
            String diagnosisId = "testDiagnosisId_DISPOSE" + i;
            String medicalItem = "보험치료" + i;
            String diagnosticName = "습진 NOS" + i;
            String prescriptionName = "경혈이제" + i;
            String chartNumber = "차트넘버" + i;

            if (i >= 1 && i <= 10) {
                plusDay = 5;
            }
            else if (i >= 12 && i <= 22) {
                plusDay = 14;
            }
            else if (i >= 23 && i <= 39) {
                plusDay = 7;
            }
            else if (i >= 40 && i <= 43) {
                plusDay = 3;
            }
            else if (i >= 44 && i <= 48) {
                plusDay = 18;
            }
            else {
                plusDay = plusDay + 1;
            }

            LocalDateTime localDateTime = LocalDateTime.of(2022, 3, 12, 10, 10, 10).plusDays(plusDay);
            String visitedDate = localDateTime.toString().replace("T", " ");

            InsertEmrDiagnosisHistoryRequestDto insertEmrDiagnosisHistoryRequestDto = InsertEmrDiagnosisHistoryRequestDto.builder()
                    .emrClientId(emrClientId)
                    .emrClientName(emrClientName)
                    .emrClientCellPhone(emrClientCellPhone)
                    .emrClientSmsYn(emrClientSmsYn)
                    .medicalPrice(medicalPrice)
                    .diagnosisId(diagnosisId)
                    .visitedDate(visitedDate)
                    .medicalItem(medicalItem)
                    .diagnosticName(diagnosticName)
                    .prescriptionName(prescriptionName)
                    .emrClientChartNumber(chartNumber)
                    .build();

            insertEmrDiagnosisHistoryRequestList.add(insertEmrDiagnosisHistoryRequestDto);
        }

        insertEmrDiagnoses(insertEmrDiagnosisHistoryRequestList);
        return ResponseEntity.noContent().build();
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

    int[] arrayY =  {
            92,
            88,
            79,
            114,
            86,
            86,
            109,
            91,
            108,
            106,
            104,
            86,
            77,
            100,
            97,
            91,
            102,
            77,
            63,
            63,
            85,
            76,
            73,
            84,
            73,
            76,
            72,
            95,
            94,
            91,
            96,
            90,
            75,
            75,
            107,
            85,
            85,
            96,
            96,
            84,
            111,
            120,
            95,
            100,
            108,
            103,
            96,
            97,
            108,
            123,
            92,
            106,
            89,
            78,
            78,
            90,
            91,
            78,
            89,
            77,
            70,
            65,
            84,
            81,
            80,
            82,
            78,
            80,
            81,
            92,
            99,
            86,
            87,
            77,
            77,
            77,
            92,
            94,
            82,
            92,
            74,
            62,
            62,
            81,
            75,
            75,
            88,
            74,
            67,
            59,
            83,
            85,
            75,
            86,
            77,
            77,
            81,
            100,
            94,
            103,
            94,
            95,
            79,
            68,
            91,
            85,
            66,
            71,
            65,
            70,
            81,
            110,
            87,
            75,
            67,
            65,
            57,
            42,
            72,
            62,
            72,
            65,
            68,
            60,
            53,
            78,
            61,
            71,
            59,
            58,
            53,
            53,
            68,
            61,
            64,
            69,
            55,
            48,
            48,
            68,
            54,
            50,
            52,
            51,
            46,
            37,
            46,
            36,
            36,
            40,
            31,
            34,
            34,
            44,
            32,
            32,
            35,
            41,
            43,
            45,
            42,
            40,
            28,
            30,
            19,
            34,
            20,
            40,
            29,
            33,
            35,
            28,
            25,
            25,
            30,
            29,
            24,
            28,
            27,
            26,
            24,
            29,
            20,
            25,
            26,
            27,
            24,
            24,
            31,
            29,
            24,
            26,
            25,
            22,
            22,
            25,
            25,
            32,
            32,
            35,
            35,
            33,
            37,
            37,
            36,
            40,
            42,
            35,
            35,
            47,
            35,
            37,
            43,
            45,
            45,
            41,
            53,
            41,
            42,
            37,
            39,
            34,
            34,
            49,
            42,
            47,
            44,
            52,
            45,
            45,
            59,
            46,
            49,
            49,
            48,
            40,
            37,
            41,
            32,
            38,
            34,
            35,
            46,
            33,
            46,
            43,
            47,
            44,
            44,
            47,
            43,
            48,
            29,
            39,
            37,
            45,
            39,
            39,
            53,
            45,
            47,
            46,
            53,
            45,
            35,
            56,
            43,
            49,
            40,
            47,
            41,
            41,
            55,
            50,
            45,
            41,
            44,
            40,
            40,
            50,
            51,
            56,
            43,
            49,
            45,
            45,
            57,
            57,
            48,
            45,
            55,
            61,
            65,
            65,
            41,
            37,
            20,
            24,
            34,
            34,
            39,
            40,
            41,
            41,
            44,
            49,
            42,
            54,
            56,
            53,
            39,
            43,
            37,
            29,
            50,
            52,
            56,
            48,
            57,
            57,
            57,
            65,
            60,
            57,
            43,
            50,
            42,
            42,
            50,
            42,
            42,
            35,
            32,
            28,
            28,
            40,
            31,
            32,
            33,
            36,
            35,
            33,
            43,
            29,
            37,
            41,
            39,
            40,
            40,
            49,
            48,
            44,
            38,
            31,
            21,
            21,
            40,
            37,
            37,
            39,
            41,
            39,
            39,
            47,
            32,
            26
    };

}
