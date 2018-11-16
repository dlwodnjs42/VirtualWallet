package VirtualWallet.Controller;

import VirtualWallet.Exceptions.*;

import VirtualWallet.model.*;

import java.net.URI;
import java.util.*;

import VirtualWallet.Repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Created by Jae on 11/12/18.
 */
@RestController
public class TransactionResource {
    @Autowired
    private TransactionRepository transactionRepository;



    /* Get Methods for getting All Transactions or a specific a transaction */
    /* ------------------------------------------------------------------ */
    @GetMapping("/transactions")
    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransactionById(Long id) throws TransactionNotFoundException {
        Optional<Transaction> trans = transactionRepository.findById(id);
        if (!trans.isPresent()) {
            throw new TransactionNotFoundException("There is no transaction with id=" + id);
        }
        return trans.get();
    }


    /* Post Methods for Creating Transaction */
    /* ------------------------------------------------------------------ */
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(Transaction t) {
        Transaction savedTransaction = transactionRepository.save(t);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedTransaction.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /* Delete Method for Deleting a transaction */
    /* ------------------------------------------------------------------ */
    @DeleteMapping("/transactions/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
    }

}
