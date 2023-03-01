package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AspectJExpressionPointcut 이 바로 포인트컷 표현식을 처리해주는 클래스다. 여기에 포인트컷
 * 표현식을 지정하면 된다. AspectJExpressionPointcut 는 상위에 Pointcut 인터페이스를 가진다.
 *
 * printMethod() 테스트는 MemberServiceImpl.hello(String) 메서드의 정보를 출력해준다.
 */
@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    /**
     * 매칭 조건
     * 접근제어자?: public
     * 반환타입: String
     * 선언타입?: hello.aop.member.MemberServiceImpl
     * 메서드이름: hello
     * 파라미터: (String)
     * 예외?: 생략
     */
    @Test
    void exactMatch() {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 매칭 조건
     * 접근제어자?: 생략
     * 반환타입: *
     * 선언타입?: 생략
     * 메서드이름: *
     * 파라미터: (..)
     * 예외?: 없음
     *
     *  * 은 아무 값이 들어와도 된다는 뜻이다.
     *  파라미터에서 .. 은 파라미터의 타입과 파라미터 수가 상관없다는 뜻이다. ( 0..* ) 파라미터는 뒤에 자세히 정리하겠다.
     */
    @Test
    void allMatch() {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatchFalse() {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * hello.aop.member.*(1).*(2)
     * (1): 타입
     * (2): 메서드 이름
     *
     *
     * 패키지에서 . 와 .. 의 차이를 이해해야 한다.
     * . : 정확하게 해당 위치의 패키지
     * .. : 해당 위치의 패키지와 그 하위 패키지도 포함
     */
    @Test
    void packageMatchSubPackage1() {
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage2() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    /**
     * 타입 매칭 - 부모 타입 허용
     *
     * typeExactMatch() 는 타입 정보가 정확하게 일치하기 때문에 매칭된다.
     * typeMatchSuperType() 을 주의해서 보아야 한다.
     *
     * execution 에서는 MemberService 처럼 부모 타입을 선언해도 그 자식 타입은 매칭된다. 다형성에서
     * 부모타입 = 자식타입 이 할당 가능하다는 점을 떠올려보면 된다.
     */
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    /**
     * 타입 매칭 - 부모 타입에 있는 메서드만 허용
     *
     * typeMatchInternal() 의 경우 MemberServiceImpl 를 표현식에 선언했기 때문에 그 안에 있는
     * internal(String) 메서드도 매칭 대상이 된다.
     * typeMatchNoSuperTypeMethodFalse() 를 주의해서 보아야 한다.
     * 이 경우 표현식에 부모 타입인 MemberService 를 선언했다. 그런데 자식 타입인 MemberServiceImpl 의
     * internal(String) 메서드를 매칭하려 한다. 이 경우 매칭에 실패한다. MemberService 에는
     * internal(String) 메서드가 없다!
     * 부모 타입을 표현식에 선언한 경우 부모 타입에서 선언한 메서드가 자식 타입에 있어야 매칭에 성공한다.
     * 그래서 부모 타입에 있는 hello(String) 메서드는 매칭에 성공하지만, 부모 타입에 없는
     * internal(String) 는 매칭에 실패한다.
     */

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    // 포인트컷으로 지정한 MemberService 는 internal 이라는 이름의 메서드가 없다.
    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }


    /**
     * 파라미터 매칭
     *
     * execution 파라미터 매칭 규칙은 다음과 같다.
     *
     *  (String) : 정확하게 String 타입 파라미터
     *  () : 파라미터가 없어야 한다.
     *  (*) : 정확히 하나의 파라미터, 단 모든 타입을 허용한다.
     *  (*, *) : 정확히 두 개의 파라미터, 단 모든 타입을 허용한다.
     *  (..) : 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다. 참고로 파라미터가 없어도 된다. 0..* 로 이해하면 된다.
     *  (String, ..) : String 타입으로 시작해야 한다. 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.
     *      예) (String) , (String, Xxx) , (String, Xxx, Xxx) 허용
     */

    // String 타입의 파라미터 허용
    // (String)
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 파라미터가 없어야 함
    // ()
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    // 정확히 하나의 파라미터 허용, 모든 타입 허용
    // (Xxx)
    @Test
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    // 파라미터가 없어도 됨
    // (), (Xxx), (Xxx, Xxx)
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    // (String), (String, Xxx), (String, Xxx, Xxx) 허용
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
