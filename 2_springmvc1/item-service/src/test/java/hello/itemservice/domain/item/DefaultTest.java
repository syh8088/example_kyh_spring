package hello.itemservice.domain.item;

import hello.itemservice.domain.item.LeakyBucket.LeakyBucket;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DefaultTest {

	@Test
	void testtest() {
		double tmpFcstValue = -0.9;

		if (tmpFcstValue >= 2) {
			System.out.println(" 5555");
		} else if (isBetween(tmpFcstValue, 1.0, 1.9)) {
			System.out.println(" 4444");
		} else if (isBetween(tmpFcstValue, 0.0, 0.9)) {
			System.out.println(" 3333");
		} else if (isBetween(tmpFcstValue, -1.0, -0.9)) {
			System.out.println(" 2222");
		} else { // -3.2 이하
			System.out.println(" 1111");
		}
	}

	public static boolean isBetween(
			Double value,
			Double fromValue,
			Double untilValue
	) {

		if (fromValue.compareTo(untilValue) <= 0) {
			return fromValue.compareTo(value) <= 0 &&
					value.compareTo(untilValue) <= 0;
		} else {
			return value.compareTo(untilValue) <= 0 ||
					value.compareTo(fromValue) >= 0;
		}
	}
	public static boolean ldInRangeInclusive(LocalDate check, LocalDate start, LocalDate end) {
		return !check.isBefore(start) && !check.isAfter(end);
	}



	@Test
	public void LeakyBucket() throws InterruptedException {
		LeakyBucket leakyBucket = new LeakyBucket(3);

		// this.leakInterval x 2  ????
		long millis = 59000; // 1분

		System.out.println("leakyBucket.allow() = " + leakyBucket.allow());
		System.out.println("leakyBucket.allow() = " + leakyBucket.allow());

		System.out.println("leakyBucket.getUsed() = " + leakyBucket.getUsed());

		Thread.sleep(millis);


		System.out.println("leakyBucket.getUsed() = " + leakyBucket.getUsed());


		System.out.println("leakyBucket.allow() = " + leakyBucket.allow());
		Thread.sleep(millis);
		System.out.println("leakyBucket.allow() = " + leakyBucket.allow());

		Thread.sleep(millis);
		System.out.println("leakyBucket.allow() = " + leakyBucket.allow());
		Thread.sleep(millis);
		System.out.println("leakyBucket.allow() = " + leakyBucket.allow());


	}


	@Test
	void test() {

		String date = "20210805";




		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate parse = LocalDate.parse(date, formatter);
		String s = parse.toString();
		System.out.println("s = " + s);

		StringBuilder stringBuilder = new StringBuilder();
		String test = "홍길동님이 내원할 무렵의 날씨를 알려드려요.. \"구름이 하늘을 뒤덮었어요.\"\n" + "\n" + "내일 흐리고 기온은 오늘과 비슷해요. \n" + "\n" + "아직은 영하는 아니라고! 스타일을 포기할 수 없다면 짧은 기장 패딩에 편안한 청바지 여기에 발끝체온에 신경써주세요. 발끝이 따듯하면 요정도 추위는 가뿐!";
		StringBuilder result = addNewLine(stringBuilder.append(test), 2);

		System.out.println("result = " + result);

	}

	public static StringBuilder addNewLine(StringBuilder message, int addCountNewLine) {
		int newLineCount = addNewLineCalculate(message);

		int i = addCountNewLine - newLineCount;
		if (i > 0) {
			message.append("\n".repeat(i));
		}

		return message;
	}

	/**
	 * 메세지 가장 끝부분 부터 글자가 존재 할때까지 '\n' Count 구하는 로직
	 * @param message
	 * @return
	 */
	private static int addNewLineCalculate(StringBuilder message) {
		char[] chars = message.toString().toCharArray();
		int length = chars.length;
		int ascCount = 0;

		for (int i = length - 1; length > 0; i--) {
			char aChar = chars[i];
			if (aChar == 10) {
				ascCount++;
			} else {
				return ascCount;
			}

			System.out.println("i = " + i);
			System.out.println("aChar = " + aChar);
		}

		return ascCount;
	}
}
