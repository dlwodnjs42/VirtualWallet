package VirtualWallet.Repositories;

import VirtualWallet.model.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jae on 11/13/18.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
