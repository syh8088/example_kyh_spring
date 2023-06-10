package hello.servlet.web.frontcontroller;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 서블릿 종속성 제거
 *
 * 컨트롤러 입장에서 HttpServletRequest, HttpServletResponse이 꼭 필요할까?
 * 요청 파라미터 정보는 자바의 Map으로 대신 넘기도록 하면 지금 구조에서는 컨트롤러가 서블릿 기술을
 * 몰라도 동작할 수 있다.
 * 그리고 request 객체를 Model로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다.
 * 우리가 구현하는 컨트롤러가 서블릿 기술을 전혀 사용하지 않도록 변경해보자.
 * 이렇게 하면 구현 코드도 매우 단순해지고, 테스트 코드 작성이 쉽다.
 *
 * 뷰 이름 중복 제거
 *
 * 컨트롤러에서 지정하는 뷰 이름에 중복이 있는 것을 확인할 수 있다.
 * 컨트롤러는 뷰의 논리 이름을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화
 * 하자.
 * 이렇게 해두면 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다.
 */
public class ModelView {

    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
