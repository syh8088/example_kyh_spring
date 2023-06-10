package hello.itemservice.domain.rfm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmrMemberDto {
/*    private String diagnosisId;
    private String emrClientId;
    private String emrClientName;
    private String paymentId;
    private String paymentId2;
    private int medicalPrice;
    private int visitedDate;
    private int visitedTime;
    private int visitedDate2;
    private int visitedTime2;*/

    private String emrClientId;
    private String emrClientName;
    private String emrClientCellPhone;
    private String diagnosisId;
    private String paymentId;
    private int medicalPrice;
    private int visitedDate;
    private int visitedTime;
    private int visitedDate2;
    private int visitedTime2;
}
