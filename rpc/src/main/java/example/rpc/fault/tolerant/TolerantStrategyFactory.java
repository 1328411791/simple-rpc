package example.rpc.fault.tolerant;

import example.rpc.fault.retry.NoRetryStrategy;
import example.rpc.fault.retry.RetryStrategy;
import example.rpc.spi.SpiLoader;

public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FailSafeTolerantStrategy();

    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }

}
