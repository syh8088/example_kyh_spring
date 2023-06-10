package hello.itemservice.domain.rfm.bitComputer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmrMemberBitComputerDto {
    private String emrClientName;
    private String diagnosisId;
    private int medicalPrice;
    private String visitedDate;
}
