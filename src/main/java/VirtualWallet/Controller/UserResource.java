package VirtualWallet.Controller;

/* Models */
import VirtualWallet.Exceptions.WalletNotFoundException;
import VirtualWallet.Exceptions.UserNotFoundException;
import VirtualWallet.Repositories.TransactionRepository;
import VirtualWallet.model.*;

/* Wrappers */

/* java packages */
import java.net.URI;
import java.util.*;

import VirtualWallet.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

/**
 * Created by Jae on 11/12/18.
 */
@RestController
public class UserResource {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    /* Get Methods for retrieving All Users, a user*/
    /* ------------------------------------------------------------------ */
    @GetMapping("/users")
    public ResponseEntity<List<User>> retrieveAllUsers() {
        return new ResponseEntity<List<User>>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> retrieveUserById(@PathVariable Long id) throws UserNotFoundException {
        Optional<User> User = userRepository.findById(id);
        if (!User.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }

        return new ResponseEntity<User>(User.get(), HttpStatus.OK);
    }

    /* Get Methods to get a specific transaction or all transactions for a user with {id} */
    /* ----------------------------------------------------------------------------------------------*/
    @GetMapping("/users/{id}/transactions")
    public ResponseEntity<List<Transaction>> retrieveLogById(@PathVariable Long id) throws UserNotFoundException {
        Optional<User> User = userRepository.findById(id);
        if (!User.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }
        return new ResponseEntity<List<Transaction>>(User.get().getWallet().getHistory(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/transactions/{num}")
    public ResponseEntity<List<Transaction>> findAllTransactionsById(@PathVariable Long id, @PathVariable int num) throws UserNotFoundException, WalletNotFoundException {
        Optional<User> OPU = userRepository.findById(id);
        if (!OPU.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }
        User user = OPU.get();
        if (user.getWallet() == null) {
            throw new WalletNotFoundException("id:" + id);
        }
        return new ResponseEntity<List<Transaction>>(user.findHistory(num), HttpStatus.OK);
    }

    /* Get the wallet balance for a user with {id} */
    /* ----------------------------------------------------------------------------------------------*/
    @GetMapping("/users/{id}/balance")
    public ResponseEntity<Integer> retrieveBalanceById(@PathVariable Long id) throws UserNotFoundException, WalletNotFoundException {
        Optional<User> User = userRepository.findById(id);
        if (!User.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }
        //it does not give me the balance
        User u = User.get();
        if (u.getWallet() == null) {
            throw new WalletNotFoundException("No Wallet");
        }
        return new ResponseEntity<Integer>(u.getWallet().getBalance(), HttpStatus.OK);
    }



    /* Put Methods for Creating Wallet for User */
    /* ------------------------------------------------------------------ */
    @PutMapping("/users/{id}/wallet")
    public ResponseEntity<Long> createWalletFromID(@PathVariable Long id) throws UserNotFoundException {
        Optional<User> OPU = userRepository.findById(id);
        if (!OPU.isPresent()) {
            throw new UserNotFoundException("id:" + id);
        }
        User u = OPU.get();
        u.createNewWallet();
        return new ResponseEntity<Long>((Long) u.getWallet().getWalletID(), HttpStatus.OK);
    }




    /* Post Methods for Creating User */
    /* ------------------------------------------------------------------ */
    @PostMapping("/users")
    public ResponseEntity<Long> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getUserID()).toUri();

        ResponseEntity.created(location).build();

        return new ResponseEntity<Long>(savedUser.getUserID(), HttpStatus.OK);

    }



    /* Post Methods for Deposit, Withdrawal, and Transfer */
    /* ------------------------------------------------------------------ */
    @PostMapping(value = "/users/deposit")
    public ResponseEntity<Object> depositAmount(@RequestParam("id") Long id, @RequestParam("amount") int amount) throws UserNotFoundException, WalletNotFoundException {
        Optional<User> OPU = userRepository.findById(id);


        if (!OPU.isPresent()) throw new UserNotFoundException();
        User u = OPU.get();
        Transaction t = u.depositAmount(amount);
        transactionRepository.save(t);

        /* create location */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        URI final_location = ServletUriComponentsBuilder.fromUri(location).path("/{amount}")
                .buildAndExpand(amount).toUri();
        ResponseEntity.created(final_location).build();


        return new ResponseEntity<Object>(t, HttpStatus.OK);
    }

    @PostMapping(value = "/users/withdrawal")
    public ResponseEntity<Object> withdrawalAmount(@RequestParam("id") Long id, @RequestParam("amount") int amount) throws UserNotFoundException, WalletNotFoundException {

        Optional<User> OPU = userRepository.findById(id);

        if (!OPU.isPresent()) throw new UserNotFoundException();
        User u = OPU.get();
        Transaction t = u.withdrawAmount(amount);
        transactionRepository.save(t);
        userRepository.save(u);

        /* create location */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        URI final_location = ServletUriComponentsBuilder.fromUri(location).path("/{amount}")
                .buildAndExpand(amount).toUri();
        ResponseEntity.created(final_location).build();

        return new ResponseEntity<Object>(t, HttpStatus.OK);
    }

    @PostMapping(value = "/users/transfer")
    public ResponseEntity<Object> transferAmount(@RequestParam("id1") Long id1, @RequestParam("id2") Long id2, @RequestParam("amount") int amount) throws UserNotFoundException, WalletNotFoundException {
        Optional<User> OPU = userRepository.findById(id1);
        Optional<User> OPU2 = userRepository.findById(id2);

        if (!OPU.isPresent() || !OPU2.isPresent()) throw new UserNotFoundException();
        User u = OPU.get();
        User u2 = OPU2.get();


        Transaction t = u.transferAmount(u2, amount);
        transactionRepository.save(t);
        userRepository.save(u);

        /* create location */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id1}")
                .buildAndExpand(id1).toUri();
        URI locationTwo = ServletUriComponentsBuilder.fromUri(location).path("/{amount}")
                .buildAndExpand(amount).toUri();
        URI final_location = ServletUriComponentsBuilder.fromUri(locationTwo).path("/{id2}")
                .buildAndExpand(id2).toUri();
        ResponseEntity.created(final_location).build();

        return new ResponseEntity<Object>(t, HttpStatus.OK);
    }

    /* Post Method for getting the last {n} Transactions for account {id} */
    /* ------------------------------------------------------------------ */
    @PostMapping("users/transactions")
    public ResponseEntity<Object> createUser(@RequestParam("id") Long id, @RequestParam("n") int n) throws UserNotFoundException {
        Optional<User> User = userRepository.findById(id);
        if(!User.isPresent()) throw new UserNotFoundException();

        User u = User.get();

        /* create location */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
        URI final_location = ServletUriComponentsBuilder.fromUri(location).path("/num")
                .buildAndExpand().toUri();
        ResponseEntity.created(final_location).build();

        return new ResponseEntity<Object>(u.findHistory(n), HttpStatus.OK);

    }

    /* Delete Method to delete user from repository */
    /* ------------------------------------------------------------------ */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

}
