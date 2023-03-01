package hello.aop.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 내부 호출을 해결하는 가장 간단한 방법은 자기 자신을 의존관계 주입 받는 것이다.
 *
 *  callServiceV1 를 수정자를 통해서 주입 받는 것을 확인할 수 있다. 스프링에서 AOP가 적용된 대상을
 * 의존관계 주입 받으면 주입 받은 대상은 실제 자신이 아니라 프록시 객체이다.
 * external() 을 호출하면 callServiceV1.internal() 를 호출하게 된다. 주입받은 callServiceV1 은
 * 프록시이다. 따라서 프록시를 통해서 AOP를 적용할 수 있다.
 *
 */
@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    /**
     * * 참고: 생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
     *  참고로 이 경우 생성자 주입시 오류가 발생한다. 본인을 생성하면서 주입해야 하기 때문에 순환 사이클이
     *  만들어진다. 반면에 수정자 주입은 스프링이 생성된 이후에 주입할 수 있기 때문에 오류가 발생하지 않는다.
     */
//    @Autowired
//    public void CallServiceV1(CallServiceV1 callServiceV1) {
//        this.callServiceV1 = callServiceV1;
//    }

    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        log.info("callServiceV1 setter={}", callServiceV1);
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // 외부 메서드 호출
    }

    public void internal() {
        log.info("call internalcall");
    }
}
