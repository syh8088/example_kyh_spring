package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    //private final MemberRepository memberRepository = new MemoryMemberRepository();

    /**
     * 문제점 발견
     * - 우리는 역할과 구현을 충실하게 분리했다. OK
     * - 다형성도 활용하고, 인터페이스와 구현 객체를 분리했다. OK
     * - OCP, DIP 같은 객체지향 설계 원칙을 충실히 준수했다
     * - 그렇게 보이지만 사실은 아니다.
     * - DIP: 주문서비스 클라이언트( OrderServiceImpl )는 DiscountPolicy 인터페이스에 의존하면서 DIP를 지킨 것 같은데?
     * - 클래스 의존관계를 분석해 보자. 추상(인터페이스) 뿐만 아니라 구체(구현) 클래스에도 의존하고 있다.
     * - 추상(인터페이스) 의존: DiscountPolicy
     * - 구체(구현) 클래스: FixDiscountPolicy , RateDiscountPolicy
     * - OCP: 변경하지 않고 확장할 수 있다고 했는데!
     * - 지금 코드는 기능을 확장해서 변경하면, 클라이언트 코드에 영향을 준다! 따라서 OCP를 위반한다.
     */
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    /**
     * 어떻게 문제를 해결할 수 있을가?
     * - 클라이언트 코드인 OrderServiceImpl 은 DiscountPolicy 의 인터페이스 뿐만 아니라 구체 클래스도 함께 의존한다.
     * - 그래서 구체 클래스를 변경할 때 클라이언트 코드도 함께 변경해야 한다.
     * - DIP 위반 추상에만 의존하도록 변경(인터페이스에만 의존)
     * - DIP를 위반하지 않도록 인터페이스에만 의존하도록 의존관계를 변경하면 된다.
     *
     * 인터페이스에만 의존하도록 설계와 코드를 변경했다.
     * - 그런데 구현체가 없는데 어떻게 코드를 실행할 수 있을까?
     * - 실제 실행을 해보면 NPE(null pointer exception)가 발생한다.
     *
     * ★해결방안
     * - 이 문제를 해결하려면 누군가가 클라이언트인 OrderServiceImpl 에 DiscountPolicy 의 구현 객체를
     * - 대신 생성하고 주입해주어야 한다.
      */
    //private DiscountPolicy discountPolicy;


    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
