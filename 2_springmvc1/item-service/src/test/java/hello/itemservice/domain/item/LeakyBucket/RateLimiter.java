package hello.itemservice.domain.item.LeakyBucket;

public abstract class RateLimiter {

	protected final int maxRequestPerSec;

	protected RateLimiter(int maxRequestPerSec) {
		this.maxRequestPerSec = maxRequestPerSec;
	}

	abstract boolean allow();
}