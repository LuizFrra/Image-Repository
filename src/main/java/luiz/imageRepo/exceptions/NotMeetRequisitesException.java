package luiz.imageRepo.exceptions;

public class NotMeetRequisitesException extends AbstractGeralException {
    public NotMeetRequisitesException(String message, Object requestData) {
        super(message, requestData);
    }
}
