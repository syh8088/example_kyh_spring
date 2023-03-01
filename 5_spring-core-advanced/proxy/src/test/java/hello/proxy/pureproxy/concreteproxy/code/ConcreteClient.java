package hello.proxy.pureproxy.concreteproxy.code;

// ConcreteClient 클래스는 ConcreteLogic 을 위존하고 있습니다.
public class ConcreteClient {

    private ConcreteLogic concreteLogic; // ConcreteLogic, TimeProxy 모두 주입 가능

    public ConcreteClient(ConcreteLogic concreteLogic) {
        this.concreteLogic = concreteLogic;
    }

    public void execute() {
        concreteLogic.operation();
    }
}