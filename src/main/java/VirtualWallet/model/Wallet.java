package VirtualWallet.model;

/* VirtualWallet.Exceptions */
import VirtualWallet.Exceptions.WalletNotFoundException;
import VirtualWallet.Exceptions.NotEnoughBalanceException;
import com.fasterxml.jackson.annotation.JsonIgnore;

/* JPA entity */
import javax.persistence.*;

import java.util.*;

/**
 * Created by Jae on 11/12/18.
 */
@Entity
@Table(name = "WALLET")
public class Wallet {

    @Id
    @Column(name = "Wallet_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "balance")
    private int balance;

    @OneToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JsonIgnore
    @JoinColumn(name = "User_ID")
    private User user;

    @OneToMany(fetch=FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.MERGE}, mappedBy = "w1")
    private List<Transaction> log;

    protected Wallet() {}

    public Wallet(int b, User u) {
        this.balance = b;
        this.user = u;
        this.log = new ArrayList<Transaction>();
    }

    /* Deposit "amount" to wallet */
    public synchronized Transaction deposit(int amount) {
        this.balance += amount;
        Transaction t = new Transaction("D", amount, this);
        log.add(t);
        return t;

    }
    /* Withdraw "amount" from current wallet */
    public synchronized Transaction withdrawal(int amount) throws NotEnoughBalanceException {
        if(this.balance - amount < 0) {
            throw new NotEnoughBalanceException("There is not enough money on your account");
        }
        this.balance -= amount;
        Transaction t = new Transaction("W", amount, this);
        log.add(t);
        return t;
    }

    /* Transfer "amount" from current wallet to "wallet" */
    public synchronized Transaction transfer(Wallet wallet, int amount) throws NotEnoughBalanceException, WalletNotFoundException {
        if(this.balance - amount < 0) {
            throw new NotEnoughBalanceException("Theres not enough money on your account");
        }
        if(wallet == null) {
            throw new WalletNotFoundException("The User Doesn't have an Existing Wallet");
        }
        this.balance -= amount;
        wallet.setBalance(wallet.getBalance() + amount);

        Transaction sent = new Transaction("T", amount, this, wallet);
        Transaction recieved = new Transaction("T", amount, wallet, this);
        log.add(sent);
        wallet.log.add(recieved);

        return sent;
    }
    @Override
    public String toString() {
        return String.format("Wallet[id=%d, user_name=%s, user_id=%d, balance=%d]",
                this.getWalletID(), this.getUser().getName(), this.getUser().getUserID(), this.getBalance());
    }

    /* Getter Methods */
    public Long getWalletID() {return this.id;}
    public int getBalance() {return this.balance;}
    public void setBalance(int newBalance) {this.balance = newBalance;}
    public User getUser() {return this.user;}
    public List<Transaction> getHistory() {return this.log;}

}
