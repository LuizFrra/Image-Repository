package luiz.imageRepo.exceptions;

public class AbstractGeralException extends RuntimeException {

    public Object requestData;

    public AbstractGeralException(String message, Object requestData) {
        super(message);
        this.requestData = requestData;
    }
}