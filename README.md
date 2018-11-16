# VirtualWallet (2018)

Design Choices
----------------------------------
# Model
*Transaction*

1.(id, timestamp, form, amount, w1, w2)


*User*

1.(id, wallet, name, password)

2.Each user can at most have one wallet; therefore, I did a @OnetoOne mapping from user to Wallet.

3.A Wallet is not initialized when a user is initialized and must be created.


*Wallet*

1.(id, balance, user, log)

2.To accomodate the ability to find all the transactions done for a specific wallet or "account", I would store all services(deposit/withdrawal/transfer) inside a log to have quick and easy access. As the userbase expands, the database may be subject to a lot of memory storage; however, I thought that scaling horizontally would be better than scaling vertically in the future. 


# Constraints: 
1.A virtual wallet is used to access one or more transaction accounts. A user can have multiple

transaction accounts. For the sake of this tech challenge, a user will have a single wallet and all access

to the transaction account(s) needs to be provide by the wallet interface.

2.The library should provide clear public endpoints to

3.Create a new wallet for a user

4.Return current account balance

5.Perform a withdrawal transaction on an account

6.Perform a deposit transaction on an account

7.Perform a transfer from one account to another account

8.Return last N transactions for an account


# Controllers/API Endpoints

*Created an API with methods:*

1.deposit

2.withdrawal

3.transfer

4.findBalance

5.findTransactionbyid

6.findTransactions

7.findUsers

8.findNTransactions

9. ...


# Repositories

*TransactionRepository & User Repository*

+Created a TransactionRepository and UserRepository to be able to cache or in-memory store the transactions and users so that they would persist. (This would be enough to see if it was working)


# Exceptions

*NotEnoughBalanceException*

1.a User might not have enough balance to make a transfer or withdrawal.

*TransactionNotFoundException*

1.a Transaction may not exist

*UserNotFoundException*

1.a User might not exist

*WalletNotFoundException*

1.a User might not have a Wallet


# Testing
*JpaDataTests - repositoryLogic*

1.I actually had a few JpaDataTests that tested for logic with the transfer/deposit/withdrawal services and persistence within the repositories but it was deleted :(. 

*API endpoint/integration - endpoint logic*

2.I tested API endpoints/integration with Postman. I tried to test with Junit but difficult to test endpoints that save to a respository because you cant have "JpaDataTest" and "SpringBootApplication" to test both the repository and endpoints.


# Future

1.More Wallets per User

2.NoSQL Database (Cassandra/AWS DynamoDB) to store variable-sized data 

3.LoadBalancing

4.Research More On Testing JpaDataTest and SpringBootApplication



