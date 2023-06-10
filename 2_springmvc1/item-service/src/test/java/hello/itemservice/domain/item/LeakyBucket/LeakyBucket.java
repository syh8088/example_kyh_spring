package hello.itemservice.domain.item.LeakyBucket;


import lombok.Getter;

@Getter
public class LeakyBucket extends RateLimiter {

	private final long capacity;
	private long used;
	private final long leakInterval;
	private long lastLeakTime;

	public LeakyBucket(int maxRequestPerSec) {
		super(maxRequestPerSec);
		this.capacity = maxRequestPerSec;
		this.used = 0;
		//this.leakInterval = 1000 / maxRequestPerSec;
		this.leakInterval = 180000 / maxRequestPerSec;

		System.out.println("this.leakInterval = " + this.leakInterval);
		System.out.println("this.used = " + this.used);
		this.lastLeakTime = System.currentTimeMillis();
	}

	@Override
	public boolean allow() {
		System.out.println("this.used = " + this.used);
		leak();
		synchronized (this) {
			this.used++;
			if (this.used >= this.capacity) {
				return false;
			}
			return true;
		}
	}
	private void leak() {
		final long now = System.currentTimeMillis();
		if (now > this.lastLeakTime) {
			long millisSinceLastLeak = now - this.lastLeakTime;
			long leaks = millisSinceLastLeak / this.leakInterval;
			if(leaks > 0) {
				if(this.used <= leaks){
					this.used = 0;
				} else {
					this.used -= (int) leaks;
				}
				this.lastLeakTime = now;
			}
		}
	}
}
