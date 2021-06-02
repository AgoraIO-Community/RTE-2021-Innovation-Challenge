package mobi.yiyin.common;

/**
 * @ProjectName: yiyin
 * @Package: mobi.yiyin.common
 * @ClassName: ArRuntimeException
 * @Description: todo java类作用描述
 * @Author: zhoujiangnan
 * @CreateDate: 1/9/21 4:54 PM
 * @UpdateUser: 更新者：
 * @UpdateDate: 1/9/21 4:54 PM
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class ArRuntimeException  extends RuntimeException{
    /**
     * Constructor.
     */
    public ArRuntimeException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message message
     */
    public ArRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message message
     * @param cause cause
     */
    public ArRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}