package LLD.RateLimiter;

public class TokenBucketRateLimiterExample {

    static class TokenBucketRateLimiter{
        private final int maxToken;
        private final int refillRatePerSecond;

        private int currToken;
        private long lastRefillTimestamp;

        public TokenBucketRateLimiter(int maxToken, int refillRatePerSecond) {
            this.maxToken = maxToken;
            this.refillRatePerSecond = refillRatePerSecond;
            this.currToken = maxToken;
            this.lastRefillTimestamp = System.nanoTime();
        }

        private void refill() {
            long now = System.nanoTime();

            // Important: We use double here because if we use int,
            // any interval less than 1 second will be considered 0, and no tokens will be added.
            // But even a small fraction like 0.x seconds, when multiplied with refillRatePerSecond,
            // can result in 1 or more tokens — so using double ensures we don't miss valid refills.

            double diff = (now - lastRefillTimestamp) / 1_000_000_000.0;
            int tokensToAdd = (int) (diff * refillRatePerSecond);
            if (tokensToAdd > 0) {
                currToken = Math.min(maxToken, currToken + tokensToAdd);

                // Also use `now` here and not System.nano();
                lastRefillTimestamp = now;
            }
        }

        public boolean allowRequest() {
            refill();

            if (currToken > 0) {
                currToken--;
                return true;
            }

            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 7);
        int requestAllowed = 0, requestDropped = 0;

        for (int i=0; i<100; i++) {
            if (limiter.allowRequest()) {
                requestAllowed++;
                System.out.println("Request allowed: " + i);
            } else {
                requestDropped++;
                System.out.println("Request Dropped: " + i);
            }
            Thread.sleep(100);
        }

        System.out.println("Request Allowed Count: " + requestAllowed);
        System.out.println("Request Dropped Count: " + requestDropped);
    }
    
}
