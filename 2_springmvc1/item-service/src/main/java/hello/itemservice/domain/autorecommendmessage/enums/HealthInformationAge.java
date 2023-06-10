package hello.itemservice.domain.autorecommendmessage.enums;

import lombok.Getter;

@Getter
public enum HealthInformationAge {

	AGES_ALL("AGES_ALL"), // 모든연령
	AGES_UNDER_6("AGES_UNDER_6"), // ~ 6세미만
	AGES_OF_6_AND_12("AGES_OF_6_AND_12"), // 6  ~12세
	AGES_OF_13_AND_19("AGES_OF_13_AND_19"), // 13~19세
	AGES_OF_20_AND_29("AGES_OF_20_AND_29"), // 20~29세
	AGES_OF_30_AND_39("AGES_OF_30_AND_39"), // 30~39세
	AGES_OF_40_AND_49("AGES_OF_40_AND_49"), // 40~49세
	AGES_OF_50_AND_64("AGES_OF_50_AND_64"), // 50~64세
	AGES_OF_65_AND_75("AGES_OF_65_AND_75"), // 65~75세
	AGES_OLDER_76("AGES_OLDER_76"),
	; // 76세 이상

	private final String healthInformationAge;

	HealthInformationAge(String healthInformationAge) {
		this.healthInformationAge = healthInformationAge;
	}


}
