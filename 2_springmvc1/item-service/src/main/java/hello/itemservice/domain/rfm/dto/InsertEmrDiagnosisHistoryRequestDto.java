package hello.itemservice.domain.rfm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InsertEmrDiagnosisHistoryRequestDto {

    private String emrClientId;
    private String emrClientName;
    private String emrClientCellPhone;
    private String diagnosisId;
    private String paymentId;
    private int medicalPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private String visitedDate;

    private Gender emrClientGender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate emrClientBirthDate;

    private String emrClientAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime emrClientFirstVisitedDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime emrClientRecentVisitedDateTime;

    private String emrClientTreatCurrent;
    private String emrClientTreatNext;
    private String emrClientVip;
    private String emrClientSuggest;
    private String emrClientMainDoctor;
    private String emrClientChartNumber;
    private EmrClientSmsYn emrClientSmsYn;
    private String medicalItem;
    private String diagnosticName;
    private String prescriptionName;

    @Builder
    public InsertEmrDiagnosisHistoryRequestDto(String emrClientId, String emrClientName, String emrClientCellPhone, String diagnosisId, String paymentId, int medicalPrice, String visitedDate, Gender emrClientGender, LocalDate emrClientBirthDate, String emrClientAddress, LocalDateTime emrClientFirstVisitedDateTime, LocalDateTime emrClientRecentVisitedDateTime, String emrClientTreatCurrent, String emrClientTreatNext, String emrClientVip, String emrClientSuggest, String emrClientMainDoctor, String emrClientChartNumber, EmrClientSmsYn emrClientSmsYn, String medicalItem, String diagnosticName, String prescriptionName) {
        this.emrClientId = emrClientId;
        this.emrClientName = emrClientName;
        this.emrClientCellPhone = emrClientCellPhone;
        this.diagnosisId = diagnosisId;
        this.paymentId = paymentId;
        this.medicalPrice = medicalPrice;
        this.visitedDate = visitedDate;
        this.emrClientGender = emrClientGender;
        this.emrClientBirthDate = emrClientBirthDate;
        this.emrClientAddress = emrClientAddress;
        this.emrClientFirstVisitedDateTime = emrClientFirstVisitedDateTime;
        this.emrClientRecentVisitedDateTime = emrClientRecentVisitedDateTime;
        this.emrClientTreatCurrent = emrClientTreatCurrent;
        this.emrClientTreatNext = emrClientTreatNext;
        this.emrClientVip = emrClientVip;
        this.emrClientSuggest = emrClientSuggest;
        this.emrClientMainDoctor = emrClientMainDoctor;
        this.emrClientChartNumber = emrClientChartNumber;
        this.emrClientSmsYn = emrClientSmsYn;
        this.medicalItem = medicalItem;
        this.diagnosticName = diagnosticName;
        this.prescriptionName = prescriptionName;
    }
}
