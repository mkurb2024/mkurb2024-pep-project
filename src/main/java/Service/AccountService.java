package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account) {
        return accountDAO.createAccount(account);
    }

    public Account getAccountByUsername(String username) {
        return accountDAO.getAccountByUsername(username);
    }

    public Account getAccountById(int account_id) {
        return accountDAO.getAccountById(account_id);
    }

}
