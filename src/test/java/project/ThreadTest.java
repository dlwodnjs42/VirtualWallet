package project;
import VirtualWallet.model.*;
import org.junit.Test;


/**
 * Created by Jae on 11/15/18.
 */
public class ThreadTest {

    @Test(threadPoolSize= 5)
    public void testconcurrentDepositWithdrawals() {
        User u1 = new User("jae", "temp");
        User u2 = new User("paul", "rip");
        u1.createNewWallet();
        u2.createNewWallet();

    }
}
