package hello.itemservice.domain.rfm.dto;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EmrClientSmsYn {

	Y("Y"),
	N("N");

	private final String emrClientSmsYn;

	EmrClientSmsYn(String emrClientSmsYn) {
		this.emrClientSmsYn = emrClientSmsYn;
	}

	public String getEmrClientSmsYn() {
		return emrClientSmsYn;
	}

	public static EmrClientSmsYn getByEmrClientSmsYn(String emrClientSmsYn) {
		return Arrays.stream(EmrClientSmsYn.values())
				.filter(data -> data.getEmrClientSmsYn().equals(emrClientSmsYn))
				.findFirst()
				.orElse(EmrClientSmsYn.N);
	}

	public static boolean isY(EmrClientSmsYn emrClientSmsYn) {
		return EmrClientSmsYn.Y.equals(emrClientSmsYn);
	}

	public static boolean isN(EmrClientSmsYn emrClientSmsYn) {
		return EmrClientSmsYn.N.equals(emrClientSmsYn);
	}
}
