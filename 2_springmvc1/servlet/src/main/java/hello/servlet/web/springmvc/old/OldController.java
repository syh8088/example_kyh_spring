package hello.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 이 컨트롤러는 어떻게 호출될 수 있을까?
 *
 *HandlerMapping(핸들러 매핑)
 * 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 한다.
 * 예) 스프링 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑이 필요하다.
 *
 * HandlerAdapter(핸들러 어댑터)
 * 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요하다.
 * 예) Controller 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾고 실행해야 한다.
 *
 * 스프링은 이미 필요한 핸들러 매핑과 핸들러 어댑터를 대부분 구현해두었다. 개발자가 직접 핸들러 매핑과
 * 핸들러 어댑터를 만드는 일은 거의 없다.
 *
 * 스프링 부트가 자동 등록하는 핸들러 매핑과 핸들러 어댑터
 * (실제로는 더 많지만, 중요한 부분 위주로 설명하기 위해 일부 생략)
 *
 * HandlerMapping
 * 0 = RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서
 * 사용
 * 1 = BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다 <-- 지금 OldController 는 이거다!
 *
 * HandlerAdapter
 * 0 = RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서
 * 사용1 = HttpRequestHandlerAdapter : HttpRequestHandler 처리
 * 2 = SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용)   <-- 지금 OldController 는 이거다!
 * 처리
 *
 */
@Component("/springmvc/old-controller")
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return null;
    }
}
