package hello.itemservice.domain.rfm.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RfmBatchDate {

	private LocalDate nowDate;
	private LocalDate startDate;
	private LocalDate middleDate;
	private LocalDate batchStartDate;
	private LocalDate endDate;

	@Builder
	public RfmBatchDate(LocalDate nowDate, LocalDate startDate, LocalDate middleDate, LocalDate batchStartDate, LocalDate endDate) {
		this.nowDate = nowDate;
		this.startDate = startDate;
		this.middleDate = middleDate;
		this.batchStartDate = batchStartDate;
		this.endDate = endDate;
	}

	public static RfmBatchDate calculateRfmBatchDate(
			LocalDate nowDate,
			LocalDate startDate,
			LocalDate endDate
	) {

		LocalDate middleDate = endDate.minusYears(1);
		LocalDate batchStartDate = middleDate.plusDays(1);

		if (startDate.isBefore(middleDate)) {

			int betweenDay = (int) ChronoUnit.DAYS.between(startDate, middleDate);
			if (betweenDay < 30) {
				int plusDay = 30 - betweenDay;
				middleDate = middleDate.plusDays(plusDay);
				batchStartDate = middleDate.plusDays(1);
			}
			else if (betweenDay > 366) {
				startDate = middleDate.minusYears(1);
			}
		}
		else {
			middleDate = startDate.plusDays(30);
			batchStartDate = middleDate.plusDays(1);

			if (endDate.isBefore(batchStartDate)) {
				batchStartDate = endDate;
				middleDate = batchStartDate.minusDays(1);

			}
		}

		return RfmBatchDate.builder()
				.nowDate(nowDate)
				.startDate(startDate)
				.middleDate(middleDate)
				.batchStartDate(batchStartDate)
				.endDate(endDate)
				.build();
	}
}
