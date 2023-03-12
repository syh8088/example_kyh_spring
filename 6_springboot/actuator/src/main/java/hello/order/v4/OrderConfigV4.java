package hello.order.v4;

import hello.order.OrderService;
import io.micrometer.core.aop.TimedAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV4 {
    @Bean
    OrderService orderService() {
        return new OrderServiceV4();
    }

    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect();
    }
}