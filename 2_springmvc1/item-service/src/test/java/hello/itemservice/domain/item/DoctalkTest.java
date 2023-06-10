package hello.itemservice.domain.item;

import hello.itemservice.domain.rfm.DateRange;
import hello.itemservice.domain.rfm.dto.RfmBatchDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class DoctalkTest {



    @Test
    public void ldInRangeInclusiveTest() {

        LocalDateTime updateAt = LocalDateTime.of(2022, 3, 10, 10, 54, 59);
        LocalDateTime updatedDateTime = LocalDateTime.of(2022, 3, 10, 10, 54, 58, 30000);
        LocalDateTime nowDateTime5MinutesMinus = updateAt.minusMinutes(5);
        boolean b = ldInRangeInclusive(updatedDateTime, nowDateTime5MinutesMinus, updateAt);

        System.out.println("b = " + b);
    }

    private static boolean ldInRangeInclusive(LocalDateTime check, LocalDateTime start, LocalDateTime end) {
        return !check.isBefore(start) && !check.isAfter(end);
    }

    @Test
    public void insertEmrDiagnoses() {

        List<InsertEmrDiagnosisHistoryDto> emrDiagnosisHistoryList = InsertEmrDiagnosisHistoryDto.init2();

        int totalSize = emrDiagnosisHistoryList.size();

        int startIndex = 0;
        int batchSize = 5000;
        int endIndex = 5000;
        int distribution = (int) Math.ceil(totalSize / endIndex);

        if (endIndex > totalSize) {

        } else {
            for (int i = 0; i < distribution + 1; i++) {
                List<InsertEmrDiagnosisHistoryDto> insertEmrDiagnosisHistoryDtoList = emrDiagnosisHistoryList.subList(startIndex, endIndex);
                log.info("insertEmrDiagnosisHistoryDtoList={}", insertEmrDiagnosisHistoryDtoList);

                startIndex = endIndex;
                endIndex = Math.min(totalSize, endIndex + batchSize);
            }
        }
    }

    @Test
    public void RFM_배치_START_END_DATE_계산_테스트() {

        //        EmrDiagnosisHistoryMaxVisitedAtAndMinVisitedAtDto emrDiagnosisHistoryMaxVisitedAtAndMinVisitedAtDto = repositories.emrDiagnosisRepository
        //                .selectEmrDiagnosisHistoryMaxVisitedAtAndMinVisitedAt("39932222");
        //
        //        LocalDate minVisitedAt = emrDiagnosisHistoryMaxVisitedAtAndMinVisitedAtDto.getMinVisitedAt();
        //        LocalDate maxVisitedAt = emrDiagnosisHistoryMaxVisitedAtAndMinVisitedAtDto.getMaxVisitedAt();

        LocalDate minVisitedAt = LocalDate.of(2022, 5, 16);
        LocalDate maxVisitedAt = LocalDate.of(2022, 5, 25);

        RfmBatchDate rfmBatchDate = RfmBatchDate.calculateRfmBatchDate(
                LocalDate.now(),
                minVisitedAt,
                maxVisitedAt
        );

        minVisitedAt = rfmBatchDate.getStartDate();
        maxVisitedAt = rfmBatchDate.getEndDate();
        LocalDate middleDate = rfmBatchDate.getMiddleDate();
        LocalDate batchStartDate = rfmBatchDate.getBatchStartDate();

        List<LocalDate> localDates = new DateRange(middleDate, maxVisitedAt).toList();
        log.info("localDates={}", localDates);
    }

    @Test
    @DisplayName("요청된 URL 특정 Path 그 다음 Path 데이터 가져오기")
    public void URLMatchTest() {

        String uri = "/api/centers/AAA/eeeeeewf";
        String strUrlTemplate = "{centerId}";

        UriTemplate template = new UriTemplate(strUrlTemplate);
        Map<String, String> parameters = new HashMap<>();
        Map<String, String> match = template.match(uri);
        System.out.println("match = " + match);

        String centerId = getPathParametersFromUrl(uri);
        System.out.println("centerId = " + centerId);
    }

    private String getPathParametersFromUrl(String url) {

        if (StringUtils.isNotBlank(url)) {
            String[] param = url.split("\\/");
            List<String> paths = Arrays.asList(param);

            Stack<String> stack = new Stack<>();

            for (String path : paths) {
                if (!stack.isEmpty() && StringUtils.isNotBlank(stack.peek()) && stack.peek().equals("centers")) {
                    if (StringUtils.isNotBlank(path)) {
                        return path;
                    }
                    else {
                        stack.push(path);
                    }
                }
                else {
                    stack.push(path);
                }
            }

        }
        return null;
    }


    @Test
    public void NEXT_YYYYMMDD() {

        String requestStringYYYYMMDDHHMM = "202303090100";
        List<String> stringyyyyMMddHHmmList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            requestStringYYYYMMDDHHMM = this.convert1minusHourStringDateTimeOfyyyyMMddHHmm(requestStringYYYYMMDDHHMM);
            stringyyyyMMddHHmmList.add(requestStringYYYYMMDDHHMM);
        }

        System.out.println("stringyyyyMMddHHmmList = " + stringyyyyMMddHHmmList);


        LocalDateTime localDateTime = convertStringDateTimeOfyyyyMMddHHmmToLocalDateTime(requestStringYYYYMMDDHHMM);
        String resultYYYYMMDDHHMM = convertLocalDateTimeToStringDateTimeOfyyyyMMddHHmm(localDateTime);



        LocalDateTime minus1HourLocalDateTime1 = localDateTime.minusHours(1);
        String resultMinus1HourYYYYMMDDHHMM = convertLocalDateTimeToStringDateTimeOfyyyyMMddHHmm(minus1HourLocalDateTime1);

        assertThat(requestStringYYYYMMDDHHMM).isEqualTo(resultYYYYMMDDHHMM);
    }

    public String convertLocalDateTimeToStringDateTimeOfyyyyMMddHHmm(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return localDateTime.format(formatter);
    }

    public LocalDateTime convertStringDateTimeOfyyyyMMddHHmmToLocalDateTime(String yyyyMMddHHmm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return LocalDateTime.parse(yyyyMMddHHmm, formatter);
    }

    public String convert1minusHourStringDateTimeOfyyyyMMddHHmm(String yyyyMMddHHmm) {
        LocalDateTime localDateTime = this.convertStringDateTimeOfyyyyMMddHHmmToLocalDateTime(yyyyMMddHHmm);
        LocalDateTime minus1HourLocalDateTime = localDateTime.minusHours(1);
        return this.convertLocalDateTimeToStringDateTimeOfyyyyMMddHHmm(minus1HourLocalDateTime);
    }

    /**
     * PriorityQueue TEST
     */
    @Test
    public void majority() {

        LocalDate localDate1 = LocalDate.of(2022, 5, 16);
        LocalDate localDate2 = LocalDate.of(2022, 5, 16);
        LocalDate localDate3 = LocalDate.of(2022, 5, 16);
        LocalDate localDate4 = LocalDate.of(2022, 5, 17);
        LocalDate localDate5 = LocalDate.of(2022, 5, 17);
        LocalDate localDate6 = LocalDate.of(2022, 5, 17);
        LocalDate localDate7 = LocalDate.of(2022, 5, 17);

        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(localDate1);
        localDateList.add(localDate2);
        localDateList.add(localDate3);
        localDateList.add(localDate4);
        localDateList.add(localDate5);
        localDateList.add(localDate6);
        localDateList.add(localDate7);

        Map<LocalDate, Integer> majorityMap = new HashMap<>();
        for (LocalDate localDate : localDateList) {
            majorityMap.merge(localDate, 1, Integer::sum);
        }

        System.out.println("majorityMap = " + majorityMap);

        PriorityQueue<MajorityVisitReservationDate> priorityQueueMajorityVisitReservationDate
                = MajorityVisitReservationDate.createMajorityVisitReservationDateList(majorityMap);

        System.out.println("priorityQueueMajorityVisitReservationDate.poll() = " + priorityQueueMajorityVisitReservationDate.poll());
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MajorityVisitReservationDate implements Comparable<MajorityVisitReservationDate> {
        private int count;
        private LocalDate visitReservationDate;

        public static PriorityQueue<MajorityVisitReservationDate> createMajorityVisitReservationDateList(Map<LocalDate, Integer> majorityMap) {

            PriorityQueue<MajorityVisitReservationDate> priorityQueueMajorityVisitReservationDate = new PriorityQueue<>(Collections.reverseOrder());

            for (LocalDate visitReservationDate : majorityMap.keySet()) {
                Integer countMajorityVisitReservationDate = majorityMap.get(visitReservationDate);
                MajorityVisitReservationDate majorityVisitReservationDate = new MajorityVisitReservationDate();

                majorityVisitReservationDate.setCount(countMajorityVisitReservationDate);
                majorityVisitReservationDate.setVisitReservationDate(visitReservationDate);

                priorityQueueMajorityVisitReservationDate.add(majorityVisitReservationDate);
            }

            return priorityQueueMajorityVisitReservationDate;
        }

        @Override
        public int compareTo(MajorityVisitReservationDate majorityVisitReservationDate) {
            return Integer.compare(this.count, majorityVisitReservationDate.getCount());
        }
    }

    /**
     * Collectors TEST
     */
    @Test
    public void collectors_test() {
        List<String> givenList = Arrays.asList("a", "bb", "cc", "bb");
        List<String> result = givenList.stream().collect(toList()); // a, bb, ccc, dd
        Set<String> result2 = givenList.stream().collect(Collectors.toSet()); // a, bb, ccc



        LinkedList<String> collect = givenList.stream().collect(Collectors.toCollection(LinkedList::new));
//        Map<String, Integer> result3 = givenList.stream().collect(Collectors.toMap("key", String::length));

        String collect1 = givenList.stream().collect(collectingAndThen(toList(), Collection::toString));

        ArrayList<String> collect33 = givenList.stream().collect(Collectors.toCollection(ArrayList::new));


        TreeSet<String> collect3 = givenList.stream().collect(toCollection(() -> new TreeSet<>(comparing(String::toString))));
        TreeSet<String> collect111 = givenList.stream().collect(toCollection(TreeSet::new));


        String collect2 = givenList.stream().collect(collectingAndThen(Collectors.toSet(), Collection::toString));
        System.out.println("result2 = " + result2);

        ArrayList<Integer> integers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Stream<Integer> integerStream = Stream.of(6, 7);
        ArrayList<Integer> collect4 = integerStream.collect(toCollection(() -> integers));
        System.out.println("integers = " + integers);

        ArrayList<String> collect5 = givenList.stream().collect(collectingAndThen(toList(), ArrayList::new));
        System.out.println("collect5 = " + collect5);
    }
}