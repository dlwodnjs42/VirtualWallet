package VirtualWallet.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Jae on 11/12/18.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotEnoughBalanceException extends Exception {
    public NotEnoughBalanceException() {super();}
    public NotEnoughBalanceException(String message) {super(message);}
    public NotEnoughBalanceException(Throwable cause) {super(cause);}
}
