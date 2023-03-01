package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import hello.typeconverter.type.TypeAndCellPhoneRequestDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {

        String data = request.getParameter("data"); // 문자 타입 조회
        Integer intValue = Integer.valueOf(data); // 숫자 타입으로 변경
        System.out.println("intValue = " + intValue);

        return "ok";
    }

    @GetMapping("/hello-v2")
    public String hellov2(@RequestParam Integer data) {
        System.out.println("data = " + data);

        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort IP = " + ipPort.getIp());
        System.out.println("ipPort PORT = " + ipPort.getPort());

        return "ok";
    }

    @PostMapping("/enum")
    public String enumTest(@ModelAttribute TypeAndCellPhoneRequestDto typeAndCellPhoneRequestDto) {

        System.out.println("typeAndCellPhoneRequestDto.getCellPhone() = " + typeAndCellPhoneRequestDto.getCellPhone());
        System.out.println("typeAndCellPhoneRequestDto.getType() = " + typeAndCellPhoneRequestDto.getType1());

        return "ok";
    }
}
