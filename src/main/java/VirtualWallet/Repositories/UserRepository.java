package VirtualWallet.Repositories;

import VirtualWallet.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Jae on 11/13/18.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
