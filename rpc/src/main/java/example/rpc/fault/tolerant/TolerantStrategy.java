package example.rpc.fault.tolerant;

import example.rpc.model.RpcResponse;

import java.util.Map;

public interface TolerantStrategy {

    RpcResponse doTolerant(Map<String,Object> context,Exception e);
}
