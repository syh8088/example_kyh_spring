package hello.proxy.jdkdynamic.code;

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Object proxy : 프록시 자신
 * Method method : 호출한 메서드
 * Object[] args : 메서드를 호출할 때 전달한 인수
 *
 * TimeInvocationHandler 은 InvocationHandler 인터페이스를 구현한다. 이렇게해서 JDK 동적
 * 프록시에 적용할 공통 로직을 개발할 수 있다.
 *
 * Object target : 동적 프록시가 호출할 대상
 *
 * method.invoke(target, args) : 리플렉션을 사용해서 target 인스턴스의 메서드를 실행한다. args
 * 는 메서드 호출시 넘겨줄 인수이다.
 */
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

    private final Object target;

    public TimeInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("TimeProxy 종료 resultTime={}", resultTime);
        return result;
    }
}