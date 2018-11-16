package VirtualWallet.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Created by Jae on 11/12/18.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class WalletNotFoundException extends Exception {
    public WalletNotFoundException() {super();}
    public WalletNotFoundException(String message) {super(message);}
    public WalletNotFoundException(Throwable cause) {super(cause);}
}
