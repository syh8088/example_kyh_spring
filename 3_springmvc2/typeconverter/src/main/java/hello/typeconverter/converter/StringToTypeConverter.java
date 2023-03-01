package hello.typeconverter.converter;

import hello.typeconverter.type.Type;
import org.springframework.core.convert.converter.Converter;

public class StringToTypeConverter implements Converter<String, Type> {
    @Override
    public Type convert(String source) {

        System.out.println("source = " + source);

        return null;
    }
}
