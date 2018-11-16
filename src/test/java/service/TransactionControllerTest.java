package service;


import VirtualWallet.Exceptions.UserNotFoundException;
import VirtualWallet.Exceptions.WalletNotFoundException;
import VirtualWallet.ProjectApplication;
import VirtualWallet.model.*;
import VirtualWallet.Repositories.*;
import VirtualWallet.Controller.*;


import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Created by Jae on 11/12/18.
 */

/* TESTS DONT WORK BECAUSE YOU CANT HAVE JPA TESTING
 ITH SPRINGBOOTTEST(Random.PORT). SPRING BOOT TEST allows you to run spring
 and hit the endpoint with the given port while jpa testing allows persistence of
 object from repositories.
 */


@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackages = "VirtualWallet")
//@SpringBootTest(classes = ProjectApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {


    @LocalServerPort private int port;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private TransactionResource transactionResource;

    @MockBean
    private UserResource userResource;

    TestRestTemplate restTemplate = new TestRestTemplate();



    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
    private String Init() {
        HashMap requestBody = new HashMap();
        requestBody.put("jae", "tadsfdsaf");

        HashMap requestBody2 = new HashMap();
        requestBody2.put("paul", "adsfsfasdf");


        /* CREATE USERS */
        User u1 = new User("jae", "s");
        User u2 = new User("pass", "joe");
        HttpEntity<User> request = new HttpEntity<>(u1);
        HttpEntity<User> request2 = new HttpEntity<>(u2);

        ResponseEntity<Long> re1  = restTemplate.exchange(createURLWithPort("/users"), HttpMethod.POST,  request, Long.class);
        ResponseEntity<Long> re2 =  restTemplate.exchange(createURLWithPort("/users"), HttpMethod.POST, request2, Long.class);

         /* Test for Response Status */
        Assert.assertEquals(re1.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(re2.getStatusCode(), HttpStatus.OK);


        Long uid1 = re1.getBody();
        Long uid2 = re2.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        /* CREATE WALLETS */
        HttpEntity<Long> request3 = new HttpEntity<>(headers);
        ResponseEntity<Long> re3 = restTemplate.exchange(createURLWithPort("/users" + Long.toString(uid1) + "/wallet"), HttpMethod.PUT, request3, Long.class);
        HttpEntity<Long> request4 = new HttpEntity<>(headers);
        ResponseEntity<Long> re4 = restTemplate.exchange(createURLWithPort("/users" + Long.toString(uid2) + "/wallet"), HttpMethod.PUT, request4, Long.class);

         /* Test for Response Status */
        Assert.assertEquals(re3.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(re4.getStatusCode(), HttpStatus.OK);

        return String.valueOf(uid1) + " " + String.valueOf(uid2);
    }

    @Test
    public void DepositTransactionTest() {
        String[] s = Init().split(" ");
        Long uid1 = Long.parseLong(s[0]);
        Long uid2 = Long.parseLong(s[1]);

        /* DEPOSIT AMOUNT */
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/users/deposit"))
                .queryParam("id", uid1)
                .queryParam("amount", 1000);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);


        HttpHeaders headers2 = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/users/deposit"))
                .queryParam("id", uid2)
                .queryParam("amount", 1000);

        HttpEntity<?> entity2 = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = restTemplate.exchange(
                builder2.toUriString(),
                HttpMethod.POST,
                entity2,
                String.class);


        /* Test for Response Status */
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(response2.getStatusCode(), HttpStatus.OK);

        /* Test for Correct Balance */
        HttpEntity<?> entity3 = new HttpEntity<>(headers);
        HttpEntity<?> entity4 = new HttpEntity<>(headers);
        ResponseEntity<Long> reBalance1  = restTemplate.exchange(createURLWithPort("/users" + uid1 + "/balance"), HttpMethod.GET, entity3, Long.class);
        ResponseEntity<Long> reBalance2 =  restTemplate.exchange(createURLWithPort("/users" + uid2 + "/balance"), HttpMethod.GET, entity4, Long.class);
        Long balance1 = reBalance1.getBody();
        Long balance2 = reBalance2.getBody();

        /* Test for Response Status */
        Assert.assertEquals( reBalance1.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals( reBalance2.getStatusCode(), HttpStatus.OK);

        Assert.assertEquals(1000, (long) balance1);
        Assert.assertEquals(1000,(long) balance2);

    }
    @Test
    public void WithdrawalTest() {
        User u1 = new User("jae", "sdsafdsaf");
        User u2 = new User("pass", "joe");
        ResponseEntity<Long> re1 = userResource.createUser(u1);
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
            userResource.withdrawalAmount(uid1, 200);
            userResource.withdrawalAmount(uid2, 300);
            Assert.assertEquals(transactionResource.getAllTransaction().size(), 4);
            Assert.assertEquals(userResource.retrieveLogById(uid1), 2);
            Assert.assertEquals(userResource.retrieveLogById(uid2), 2);
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
        ResponseEntity<Long> re1 = userResource.createUser(u1);
        ResponseEntity<Long> re2 = userResource.createUser(u2);

        Long uid1 = re1.getBody();
        Long uid2 = re2.getBody();
        try {
            userResource.createWalletFromID(uid1);
            userResource.createWalletFromID(uid2);
        } catch(UserNotFoundException e) {

            e.printStackTrace();
        }
        System.out.println("wallet");

        try {
            userResource.depositAmount(uid1, 500);
            userResource.depositAmount(uid2, 600);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("deposit");
        try {
            userResource.transferAmount(uid1,uid2, 200);
            Assert.assertEquals(transactionResource.getAllTransaction().size(), 3);
            Assert.assertEquals(userResource.retrieveLogById(uid1), 2);
            Assert.assertEquals(userResource.retrieveLogById(uid2), 2);
        } catch(UserNotFoundException e) {
            e.printStackTrace();
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("transfer");

        userRepository.flush();
        transactionRepository.flush();


    }


}
