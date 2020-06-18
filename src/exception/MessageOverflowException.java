package exception;

/**
 * 内容过长将会抛出异常
 * @author 22591
 */
public class MessageOverflowException extends Exception {

    public MessageOverflowException() {
        super("内容过长，无法发送数据");
    }
}
