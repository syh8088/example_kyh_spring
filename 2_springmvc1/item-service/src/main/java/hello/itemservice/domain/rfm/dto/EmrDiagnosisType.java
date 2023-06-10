package hello.itemservice.domain.rfm.dto;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EmrDiagnosisType {

    INITIALIZE("INITIALIZE"),
    DISPOSE("DISPOSE"),
    NONE("NONE");

    private final String emrDiagnosisType;

    EmrDiagnosisType(String emrDiagnosisType) {
        this.emrDiagnosisType = emrDiagnosisType;
    }

    public String getEmrDiagnosisType() {
        return this.emrDiagnosisType;
    }

    public static EmrDiagnosisType getByEmrDiagnosisType(String emrDiagnosisType) {
        return Arrays.stream(EmrDiagnosisType.values())
                .filter(EmrDiagnosisType -> EmrDiagnosisType.getEmrDiagnosisType().equals(emrDiagnosisType))
                .findFirst()
                .orElse(EmrDiagnosisType.NONE);
    }

}
