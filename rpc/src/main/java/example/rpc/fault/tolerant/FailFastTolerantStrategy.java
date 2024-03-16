package example.rpc.fault.tolerant;

import example.rpc.model.RpcResponse;

import java.util.Map;

public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("fail fast",e);
    }
}
