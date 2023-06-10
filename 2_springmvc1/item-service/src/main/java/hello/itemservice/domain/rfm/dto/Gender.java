package hello.itemservice.domain.rfm.dto;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    M("M"),  // 남성
    F("F"),  // 여성
    NONE("NONE");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public static Gender getByGender(String gender) {
        return Arrays.stream(Gender.values())
                .filter(data -> data.getGender().equals(gender))
                .findFirst()
                .orElse(Gender.NONE);
    }
}
