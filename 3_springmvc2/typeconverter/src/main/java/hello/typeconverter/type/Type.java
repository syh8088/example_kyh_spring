package hello.typeconverter.type;

import lombok.Getter;

@Getter
public enum Type {
    ONE_TEST("ONE-TEST"),
    TWO_TEST("TWO-TEST");

    private String type;

    Type(String type) {
        this.type = type;
    }
}
