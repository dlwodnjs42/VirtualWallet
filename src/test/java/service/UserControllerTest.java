package service;


import VirtualWallet.Exceptions.UserNotFoundException;
import VirtualWallet.Exceptions.WalletNotFoundException;
import VirtualWallet.ProjectApplication;
import VirtualWallet.model.*;
import VirtualWallet.Repositories.*;
import VirtualWallet.Controller.*;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * Created by Jae on 11/15/18.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=ProjectApplication.class)
@SpringBootTest
@WebAppConfiguration
@DataJpaTest
public class UserControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ProjectApplication.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionResource transactionResource;

    @Autowired
    private UserResource userResource;

    @Test
    public void createWalletTest() {
        User u1 = new User("jae", "s");
        User u2 = new User("pass", "joe");
        ResponseEntity<Long> re1 =  userResource.createUser(u1);
        ResponseEntity<Long> re2 = userResource.createUser(u2);

        Long uid1 = re1.getBody();
        Long uid2 = re2.getBody();
        try {
            userResource.createWalletFromID(uid1);
            userResource.createWalletFromID(uid2);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        }

        User check1 = null;
        User check2 = null;
        try {
            check1 = userResource.retrieveUserById(uid1).getBody();
            check2 = userResource.retrieveUserById(uid2).getBody();
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertNotEquals(check1.getWallet(), null);
        Assert.assertNotEquals(check2.getWallet(), null);
        userRepository.flush();
        transactionRepository.flush();

    }

    @Test
    public void DepositTest() {
        User u1 = new User("jae", "s");
        User u2 = new User("pass", "joe");
        ResponseEntity<Long> re1 =  userResource.createUser(u1);
        ResponseEntity<Long> re2 = userResource.createUser(u2);

        Long uid1 = re1.getBody();
        Long uid2 = re2.getBody();
        try {
            userResource.createWalletFromID(uid1);
            userResource.createWalletFromID(uid2);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            userResource.depositAmount(uid1, 500);
            userResource.depositAmount(uid2, 600);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }

        User check1 = null;
        User check2 = null;
        try {
            check1 = userResource.retrieveUserById(uid1).getBody();
            check2 = userResource.retrieveUserById(uid2).getBody();
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        }

        Assert.assertEquals(check1.getWallet().getBalance(), 500);
        Assert.assertEquals(check2.getWallet().getBalance(), 600);

        userRepository.flush();
        transactionRepository.flush();
    }
    @Test
    public void FindBalanceTest() {
        User u1 = new User("jae", "s");
        User u2 = new User("pass", "joe");
        ResponseEntity<Long> re1 =  userResource.createUser(u1);
        ResponseEntity<Long> re2 = userResource.createUser(u2);

        Long uid1 = re1.getBody();
        Long uid2 = re2.getBody();
        try {
            userResource.createWalletFromID(uid1);
            userResource.createWalletFromID(uid2);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            userResource.depositAmount(uid1, 500);
            userResource.depositAmount(uid2, 600);
            Assert.assertEquals((int)userResource.retrieveBalanceById(uid1).getBody() ,500);
            Assert.assertEquals((int)userResource.retrieveBalanceById(uid1).getBody() ,600);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }

        userRepository.flush();
        transactionRepository.flush();




    }
    @Test
    public void WithdrawalTest() {
        User u1 = new User("jae", "s");
        User u2 = new User("pass", "joe");
        ResponseEntity<Long> re1 =  userResource.createUser(u1);
        ResponseEntity<Long> re2 = userResource.createUser(u2);

        Long uid1 = re1.getBody();
        Long uid2 = re2.getBody();
        try {
            userResource.createWalletFromID(uid1);
            userResource.createWalletFromID(uid2);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            userResource.depositAmount(uid1, 500);
            userResource.depositAmount(uid2, 600);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }


        try {
            userResource.withdrawalAmount(uid1, 200);
            userResource.withdrawalAmount(uid2, 300);
            Assert.assertEquals((int)userResource.retrieveBalanceById(uid1).getBody() ,300);
            Assert.assertEquals((int)userResource.retrieveBalanceById(uid1).getBody() ,300);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }

        userRepository.flush();
        transactionRepository.flush();

    }
    @Test
    public void TransferTest() {
        User u1 = new User("jae", "s");
        User u2 = new User("pass", "joe");
        ResponseEntity<Long> re1 =  userResource.createUser(u1);
        ResponseEntity<Long> re2 = userResource.createUser(u2);

        Long uid1 = re1.getBody();
        Long uid2 = re2.getBody();
        try {
            userResource.createWalletFromID(uid1);
            userResource.createWalletFromID(uid2);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        }

        try {
            userResource.depositAmount(uid1, 500);
            userResource.depositAmount(uid2, 600);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }

        try {
            userResource.transferAmount(uid1,uid2, 200);
            Assert.assertEquals((int)userResource.retrieveBalanceById(uid1).getBody() ,300);
            Assert.assertEquals((int)userResource.retrieveBalanceById(uid1).getBody() ,800);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }

        userRepository.flush();
        transactionRepository.flush();


    }


}
