package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class JdbcAccountBalanceDao implements AccountBalanceDao {


    private JdbcTemplate jdbcTemplate;

    public JdbcAccountBalanceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(int accountId){
        Account account = null;
        String sql = "SELECT account_id, user_id, balance \n" +
                "FROM account \n" +
                "WHERE account_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if(results.next()){
                account = mapRowToAccount(results);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return account;
    }

    @Override
    public BigDecimal getBalanceById(int userId) {
        Account account = null;
        BigDecimal balance = new BigDecimal("0.00");


        String sql = "SELECT account_id, user_id, balance from account Where user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
                balance = account.getBalance();
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return balance;
    }

    @Override
    public Account updateSenderAccountById(Account account, BigDecimal amount) {
        boolean success = false;

        String sql1 = "UPDATE account SET balance = balance - ?" +
                "WHERE account_id = ?;";

        try {

            jdbcTemplate.update(sql1, amount, account.getAccount_id());


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return account;
    }

    @Override
    public Account updateReceiverAccountById(Account account, BigDecimal amount) {

        String sql1 = "UPDATE account SET balance = balance + ?" +
                "WHERE account_id = ?;";

        try {

            jdbcTemplate.update(sql1, amount, account.getAccount_id());


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return account;

    }

    @Override
    public int userToAcctId(int userId) {
        int acctId = 0;
        Account account = null;

        String sql = "SELECT account_id, user_id, balance \n" +
                "FROM account \n" +
                "WHERE user_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if(results.next()){
                account = mapRowToAccount(results);
                acctId = account.getAccount_id();
            }


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (NullPointerException ex) {
            throw new NullPointerException("account does not exist");
        }
        return acctId;
    }



    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setUser_id(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
