package conf.exception;

/**
 * Created by wanghongxing on 15/12/7.
 */
public class ZkLockException extends RuntimeException {

    public ZkLockException() {
        super();
    }

    public ZkLockException(String message) {
        super(message);
    }

    public ZkLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZkLockException(Throwable cause) {
        super(cause);
    }

    protected ZkLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
