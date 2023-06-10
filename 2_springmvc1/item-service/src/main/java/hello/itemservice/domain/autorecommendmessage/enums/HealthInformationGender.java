package hello.itemservice.domain.autorecommendmessage.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum HealthInformationGender {
    M("M"),  // 남성
    F("F"),  // 여성
    ALL("ALL");

    private final String gender;

    HealthInformationGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public static HealthInformationGender getByGender(String gender) {
        return Arrays.stream(HealthInformationGender.values())
                .filter(data -> data.getGender().equals(gender))
                .findFirst()
                .orElse(HealthInformationGender.ALL);
    }

    public static boolean isAll(HealthInformationGender gender) {
        return HealthInformationGender.ALL == gender;
    }
}
