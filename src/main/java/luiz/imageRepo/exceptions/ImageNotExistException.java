package luiz.imageRepo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ImageNotExistException extends AbstractGeralException {
    public ImageNotExistException(String message, Object requestData) {
        super(message, requestData);
    }
}