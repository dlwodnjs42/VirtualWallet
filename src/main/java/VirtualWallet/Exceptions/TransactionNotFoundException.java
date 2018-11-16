package VirtualWallet.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Created by Jae on 11/12/18.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException() {super();}
    public TransactionNotFoundException(String message) {super(message);}
    public TransactionNotFoundException(Throwable cause) {super(cause);}
}
