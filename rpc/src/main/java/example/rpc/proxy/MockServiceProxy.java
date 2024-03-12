package example.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("MockServiceProxy invoke method: {}, returnType: {}", method.getName(), returnType.getName());

        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> type) {
        if(type.isPrimitive()){
            if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == byte.class) {
                return (byte) 0;
            } else if (type == float.class) {
                return 0.0f;
            } else if (type == double.class) {
                return 0.0d;
            } else if (type == char.class) {
                return '\u0000';
            } else if (type == boolean.class) {
                return false;
            } else {
                return null;
            }
        }
        return null;
    }


}
