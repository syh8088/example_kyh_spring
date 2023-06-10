package hello.itemservice.domain.item;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InsertEmrDiagnosisHistoryDto {

    public LocalDateTime getVisitedDate() {
        return visitedDate;
    }

    public void setVisitedDate(LocalDateTime visitedDate) {
        this.visitedDate = visitedDate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime visitedDate;

    public static List<InsertEmrDiagnosisHistoryDto> init() {

        List<InsertEmrDiagnosisHistoryDto> insertEmrDiagnosisHistoryDtoList = new ArrayList<>();
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2021, 12, 1, 1, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2021, 12, 5, 9, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2021, 12, 12, 11, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 1, 3, 11, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 1, 5, 11, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 1, 15, 11, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 1, 16, 11, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 1, 18, 11, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 1, 31, 11, 11, 11)));
//        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 2, 3, 11, 11, 11)));

        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 8, 15, 1, 11, 11)));
        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 8, 16, 1, 11, 11)));
        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 8, 17, 1, 11, 11)));
        insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(LocalDateTime.of(2022, 8, 18, 1, 11, 11)));

        return insertEmrDiagnosisHistoryDtoList;
    }

    public static InsertEmrDiagnosisHistoryDto createInsertEmrDiagnosisHistoryDto(LocalDateTime localDateTime) {
        InsertEmrDiagnosisHistoryDto insertEmrDiagnosisHistoryDto = new InsertEmrDiagnosisHistoryDto();
        insertEmrDiagnosisHistoryDto.setVisitedDate(localDateTime);

        return insertEmrDiagnosisHistoryDto;
    }

    public static List<InsertEmrDiagnosisHistoryDto> init2() {

        List<InsertEmrDiagnosisHistoryDto> insertEmrDiagnosisHistoryDtoList = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.of(2019, 4, 20, 9, 11, 11);

        int total = 15100;
        for (int i = 1; i <= total; i++) {
            insertEmrDiagnosisHistoryDtoList.add(InsertEmrDiagnosisHistoryDto.createInsertEmrDiagnosisHistoryDto(localDateTime.plusDays(i)));
        }

        return insertEmrDiagnosisHistoryDtoList;
    }
}
