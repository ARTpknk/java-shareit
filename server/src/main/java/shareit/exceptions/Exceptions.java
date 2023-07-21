package shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import shareit.exceptions.model.EmptyOwnerFieldException;
import shareit.exceptions.model.OwnerNotFoundException;
import shareit.exceptions.model.ShareItNotFoundException;

import java.util.Date;

@RestControllerAdvice
public class Exceptions {

    @ExceptionHandler(OwnerNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundException(OwnerNotFoundException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(value = {EmptyOwnerFieldException.class,
            ShareItNotFoundException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage internalServerError(EmptyOwnerFieldException ex, WebRequest request) {

        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
    }


}
