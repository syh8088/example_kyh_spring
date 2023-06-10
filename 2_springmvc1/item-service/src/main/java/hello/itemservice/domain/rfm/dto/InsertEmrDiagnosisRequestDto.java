package hello.itemservice.domain.rfm.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InsertEmrDiagnosisRequestDto {

    private String institutionNumber; // 필수

    private String businessNumber;

    private String companyName; // 필수

    private String companyTel; // 필수

    private EmrDiagnosisType type; // 필수
    private String emrClientId;
    private String emrKakaoSenderKey;

    private String blockCallNumber;

    private List<InsertEmrDiagnosisHistoryRequestDto> emrDiagnosisHistoryList;
}
