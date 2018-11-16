# VirtualWallet (2018)

Design Choices
----------------------------------
# Model
* Transaction *
+(id, timestamp, form, amount, w1, w2)

* User *
+(id, wallet, name, password)
+Each user can at most have one wallet; therefore, I did a @OnetoOne mapping from user to Wallet.
+A Wallet is not initialized when a user is initialized and must be created.

* Wallet *
+(id, balance, user, log)
+To accomodate the ability to find all the transactions done for a specific wallet or "account", I would store all services(deposit/withdrawal/transfer) inside a log to have quick and easy access. As the userbase expands, the database may be subject to a lot of memory storage; however, I thought that scaling horizontally would be better than scaling vertically in the future. 

#Constraints: 
+A virtual wallet is used to access one or more transaction accounts. A user can have multiple
transaction accounts. For the sake of this tech challenge, a user will have a single wallet and all access
to the transaction account(s) needs to be provide by the wallet interface.
+The library should provide clear public endpoints to
+Create a new wallet for a user
+Return current account balance
+Perform a withdrawal transaction on an account
+Perform a deposit transaction on an account
+Perform a transfer from one account to another account
+Return last N transactions for an account


#Controllers/API Endpoints
*Created an API with methods:*
+deposit
+withdrawal
+transfer
+findBalance
+findTransactionbyid
+findTransactions
+findUsers
+findNTransactions


#Repositories
*TransactionRepository & User Repository *
+Created a TransactionRepository and UserRepository to be able to cache or in-memory store the transactions and users so that they would persist. (This would be enough to see if it was working)


#Exceptions
*NotEnoughBalanceException*
+a User might not have enough balance to make a transfer or withdrawal.
*TransactionNotFoundException*
+a Transaction may not exist
*UserNotFoundException*
+a User might not exist
*WalletNotFoundException*
+a User might not have a Wallet

#Testing
* JpaDataTests - repositoryLogic *
+ I actually had a few JpaDataTests that tested for logic with the transfer/deposit/withdrawal services and persistence within the repositories but it was deleted :(. 
*API endpoint/integration - endpoint logic*
+ I tested API endpoints/integration with Postman. I tried to test with Junit but difficult to test endpoints that save to a respository because you cant have "JpaDataTest" and "SpringBootApplication" to test both the repository and endpoints.

# Future

+ More Wallets per User
+ NoSQL Database (Cassandra/AWS DynamoDB) to store variable-sized data 
+ LoadBalancing
+ Research More On Testing JpaDataTest and SpringBootApplication


