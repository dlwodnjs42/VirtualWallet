package VirtualWallet.model;

import VirtualWallet.Exceptions.WalletNotFoundException;
import VirtualWallet.Exceptions.NotEnoughBalanceException;

/* JPA entity */
import javax.persistence.*;

/* java */
import java.util.*;
/**
 * Created by Jae on 11/12/18.
 */
@Entity
@Table(name="USER")
public class User {
    @Id
    @Column(name="User_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //cascade = delete all thats related to it
    //fetch=lazy: only load the one that we want
    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    @JoinColumn(name="Wallet_ID")
    private Wallet wallet;

    @Column(name = "Name")
    private String name;

    @Column(name = "Password")
    private String password;

    protected User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Transaction depositAmount(int amount) throws WalletNotFoundException {
        if(this.wallet == null) throw new WalletNotFoundException();

        return this.wallet.deposit(amount);
    }

    public Transaction withdrawAmount(int amount) throws WalletNotFoundException {
        if(this.wallet == null) throw new WalletNotFoundException();
        Transaction trans = null;
        try {
            trans = this.wallet.withdrawal(amount);
        } catch(NotEnoughBalanceException e) {
            e.printStackTrace();
            System.out.print("There is not enough money on your account.");
        }
        return trans;

    }
    public Transaction transferAmount(User u, int amount) throws WalletNotFoundException {
        if (this.wallet == null) throw new WalletNotFoundException();
        Transaction trans = null;
        try {
            trans =  this.wallet.transfer(u.wallet, amount);
        } catch(NotEnoughBalanceException e) {
            e.printStackTrace();
            System.out.print("There is not enough money on your account.");
        } catch(WalletNotFoundException e) {
            e.printStackTrace();
            System.out.print("The wallet you are trying to transfer to does not exist.");

        }
        return trans;
    }

    public List<Transaction> findHistory(int n) {
        List<Transaction> translog = this.wallet.getHistory();
        if(n >= translog.size())  {
            return translog;
        }
        return translog.subList(translog.size()- n, translog.size());
    }

//    @Override
//    public String toString() {
//        return String.format("User[id=%d, name=%s, wallet_id=%d, wallet_balance=%d]",
//                this.getUserID(), this.getName(), this.getWallet().getWalletID(), this.getWallet().getBalance());
//    }


    public String getName() { return this.name;}
    public Long getUserID() {return this.id;}
    public String getPassword() { return this.password;}
    public Wallet getWallet() {return this.wallet;}

    public void setName() { this.name = name;}
    public void setId(Long id) { this.id = id;}
    public void setPassword() { this.password = password;}

    public void createNewWallet() { this.wallet = new Wallet(0, this); }


}
