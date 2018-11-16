package VirtualWallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Time;
import java.time.LocalDateTime;
import java.sql.Timestamp;


/* JPA entity */
import javax.persistence.*;


/**
 * Created by Jae on 11/12/18.
 */

@Entity
@EntityListeners(AuditingEntityListener.class) // automaticall
@Table(name="TRANSACTIONS")
public class Transaction {

    @Id
    @Column(name = "Transaction_ID")
    @GeneratedValue(strategy = GenerationType.AUTO) // IDENTITY = autoincrement
    private Long id; //globally unique identifier


    @Column(name = "Timestamp")
    private Timestamp timestamp;

    @Column(name = "Form")
    private String form; //debit or credit

    @Column(name = "Amount")
    private int amount;

    @OneToOne
    @JsonIgnore
    @AttributeOverrides({
            @AttributeOverride(name = "Wallet_ID", column = @Column(name = "walletOne"))
    })
    private Wallet w1;

    @JsonIgnore
    @OneToOne
    @AttributeOverrides({
            @AttributeOverride(name = "Wallet_ID", column = @Column(name = "walletTwo"))
    })
    private Wallet w2;

    protected Transaction() {}

    /* deposit/withdrawal Transaction */
    public Transaction(String f, int amount, Wallet w1) {
        this.form = f;
        this.amount = amount;
        this.w1 = w1;
        this.w2 = null;
        this.timestamp = new Timestamp(System.currentTimeMillis());
//        this.id = Integer.toString(this.amount +(this.timestamp + this.form).hashCode() + w1.hashCode());
    }

    /* transfer Transaction */
    public Transaction(String f, int amount, Wallet w1, Wallet w2) {
        this.form = f;
        this.amount = amount;
        this.w1 = w1;
        this.w2 = w2;
        this.timestamp = new Timestamp(System.currentTimeMillis());

//        this.id = Integer.toString(this.amount +(this.timestamp + this.form).hashCode() + w1.hashCode() + w2.hashCode());
    }

    @Override
    public String toString() {
        return String.format("Transaction[id=%d, form=%s, amount=%d, timestamp=%s, wallet1=%s]",
                this.id, this.form, this.amount, this.timestamp, w1.getUser().getName());
    }

    /* Getter Methods */
    public Long getId() {return this.id;}
    public String getForm() { return this.form; }
    public Timestamp getTimestamp() {return this.timestamp;}
    public int getAmount() { return this.amount; }
    public Long getWalletOne() {return this.w1.getWalletID();}
    public Long getWalletTwo() {return this.w2 == null? null : this.w2.getWalletID();}


    public Wallet getWalletOne(Transaction t) {
        return t.w1;
    }
    public Wallet getWalletTwo(Transaction t) {
        return t.w2;
    }




}
