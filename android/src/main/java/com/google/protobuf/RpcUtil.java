package com.google.protobuf;

public final class RpcUtil {

    public static final class AlreadyCalledException extends RuntimeException {
        private static final long serialVersionUID = 5469741279507848266L;

        public AlreadyCalledException() {
            super("This RpcCallback was already called and cannot be called multiple times.");
        }
    }

    private RpcUtil() {
    }

    private static <Type extends Message> Type copyAsType(Type type, Message message) {
        return type.newBuilderForType().mergeFrom(message).build();
    }

    public static <Type extends Message> RpcCallback<Message> generalizeCallback(final RpcCallback<Type> rpcCallback, final Class<Type> cls, final Type type) {
        return new RpcCallback<Message>() {
            public void run(Message message) {
                Object obj;
                try {
                    obj = (Message) cls.cast(message);
                } catch (ClassCastException e) {
                    obj = RpcUtil.copyAsType(type, message);
                }
                rpcCallback.run(obj);
            }
        };
    }

    public static <ParameterType> RpcCallback<ParameterType> newOneTimeCallback(final RpcCallback<ParameterType> rpcCallback) {
        return new RpcCallback<ParameterType>() {
            private boolean alreadyCalled = false;

            public void run(ParameterType parameterType) {
                synchronized (this) {
                    if (this.alreadyCalled) {
                        throw new AlreadyCalledException();
                    }
                    this.alreadyCalled = true;
                }
                rpcCallback.run(parameterType);
            }
        };
    }

    public static <Type extends Message> RpcCallback<Type> specializeCallback(RpcCallback<Message> rpcCallback) {
        return rpcCallback;
    }
}
