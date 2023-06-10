package hello.itemservice.domain.autorecommendmessage;

import hello.itemservice.domain.autorecommendmessage.enums.HealthInformationAge;
import hello.itemservice.domain.autorecommendmessage.enums.HealthInformationGender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ComplexHealthInformationMessageDto {

	List<String> healthInformationKeywordList;
	List<Month> monthList;
	HealthInformationGender healthInformationGender;
	HealthInformationAge healthInformationAge;
	String message;

	@Override
	public boolean equals(Object o) {

		if (o instanceof ComplexHealthInformationMessageDto) {

			ComplexHealthInformationMessageDto data = (ComplexHealthInformationMessageDto) o;

			for (String keyword : healthInformationKeywordList) {
				if (this.healthInformationKeywordList.contains(keyword)) {
					return true;
				}
			}

		}



		return false;
	}

	public static ComplexHealthInformationMessageDto createComplexHealthInformationMessageDto(
			String healthInformationKeyword,
			Month month,
			HealthInformationGender healthInformationGender,
			HealthInformationAge healthInformationAge
	) {
		ComplexHealthInformationMessageDto complexHealthInformationMessageDto = new ComplexHealthInformationMessageDto();
		complexHealthInformationMessageDto.setHealthInformationKeywordList(Collections.singletonList(healthInformationKeyword));
		complexHealthInformationMessageDto.setMonthList(Collections.singletonList(month));
		complexHealthInformationMessageDto.setHealthInformationGender(healthInformationGender);
		complexHealthInformationMessageDto.setHealthInformationAge(healthInformationAge);

		return complexHealthInformationMessageDto;
	}

	public static ComplexHealthInformationMessageDto createComplexHealthInformationMessageDto(
			List<String> healthInformationKeywords,
			List<Month> months,
			HealthInformationGender healthInformationGender,
			HealthInformationAge healthInformationAge
	) {
		ComplexHealthInformationMessageDto complexHealthInformationMessageDto = new ComplexHealthInformationMessageDto();
		complexHealthInformationMessageDto.setHealthInformationKeywordList(healthInformationKeywords);
		complexHealthInformationMessageDto.setMonthList(months);
		complexHealthInformationMessageDto.setHealthInformationGender(healthInformationGender);
		complexHealthInformationMessageDto.setHealthInformationAge(healthInformationAge);

		return complexHealthInformationMessageDto;
	}
}
