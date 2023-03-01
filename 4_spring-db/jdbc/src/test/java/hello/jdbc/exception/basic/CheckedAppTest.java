package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 *	체크 예외의 최상위 타입인 'Exception' 을 던지게 되면 다른 체크 예외를 체크할 수 있는 기능이 무효화 되고, 중요한 체크 예외를 다 놓치게 된다.
 *	중간에 중요한 체크 예외가 발생해도 컴파일러는 'Exception' 을 던지기 때문에 문법에 맞다고 판단해서 컴파일 오류가 발생하지 않는다.
 * 	이렇게 하면 모든 예외를 다 던지기 때문에 체크 예외를 의도한 대로 사용하는 것이 아니다. 따라서 꼭 필요한 경우가 아니면 이렇게 'Exception' 자체를 밖으로 던지는 것은 좋지 않은 방법이다.
 */
public class CheckedAppTest {

	@Test
	void checked() {
		Controller controller = new Controller();
		Assertions.assertThatThrownBy(() -> controller.request())
				.isInstanceOf(Exception.class);
	}

	static class Controller {
		Service service = new Service();

		public void request() throws SQLException, ConnectException {
			service.logic();
		}
	}

	static class Service {
		Repository repository = new Repository();
		NetworkClient networkClient = new NetworkClient();

		public void logic() throws SQLException, ConnectException {
			repository.call();
			networkClient.call();
		}
	}

	static class NetworkClient {
		public void call() throws ConnectException {
			throw new ConnectException("연결 실패");
		}
	}

	static class Repository {
		public void call() throws SQLException {
			throw new SQLException("ex");
		}
	}

}
