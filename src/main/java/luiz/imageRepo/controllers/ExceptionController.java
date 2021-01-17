package luiz.imageRepo.controllers;

import luiz.imageRepo.entities.error.ErrorInfo;
import luiz.imageRepo.exceptions.ImageNotExistException;
import luiz.imageRepo.exceptions.NotMeetRequisitesException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ErrorInfo> conflitException(HttpServletRequest req, Exception exception) {
        ErrorInfo errorInfo = ErrorInfo.create(req, exception);
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NotMeetRequisitesException.class})
    public ResponseEntity<ErrorInfo> notMeetRequisitesException(HttpServletRequest req, Exception exception) {
        ErrorInfo errorInfo = ErrorInfo.create(req, exception);
        return new ResponseEntity<>(errorInfo, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({ImageNotExistException.class})
    public ResponseEntity<ErrorInfo> quoteNotExistException(HttpServletRequest req, Exception exception) {
        ErrorInfo errorInfo = ErrorInfo.create(req, exception);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

}
