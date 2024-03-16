import example.rpc.fault.retry.FixedIntervalRetryStrategy;
import example.rpc.fault.retry.NoRetryStrategy;
import example.rpc.fault.retry.RetryStrategy;
import example.rpc.model.RpcResponse;
import org.junit.Test;

public class RetryStrategyTest {



    @Test
    public void  doNoRetry() {
        RetryStrategy retryStrategy = new NoRetryStrategy();
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("do Retry");
               throw new RuntimeException("retry failed");
            });
        }catch (Exception e){
            System.out.println("retry failed");
            e.printStackTrace();
        }
    }

    @Test
    public void  doFixedIntervalRetry() {
        RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("do Retry");
                throw new RuntimeException("retry failed");
            });
        }catch (Exception e){
            System.out.println("retry failed");
            e.printStackTrace();
        }
    }
}
