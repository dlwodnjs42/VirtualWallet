package VirtualWallet.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Jae on 11/12/18.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception {
    public UserNotFoundException() {super();}
    public UserNotFoundException(String message) {super(message);}
    public UserNotFoundException(Throwable cause) {super(cause);}
}
