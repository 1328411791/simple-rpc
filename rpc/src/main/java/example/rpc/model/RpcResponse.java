package example.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse {

    private Object data;

    private Class<?> dataType;

    private String message;

    private Exception exception;
}
