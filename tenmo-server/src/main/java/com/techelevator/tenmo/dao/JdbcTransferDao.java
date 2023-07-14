package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    @Override
    public List<Transfer> getMyTransferHistory(int acctId) {
        List<Transfer> transfers = new ArrayList<>();


        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE account_from = ? OR account_to = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, acctId, acctId);
            while (results.next()) {
                transfers.add(mapRowToTransfer(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }


    @Override
    public Transfer getTransferById(int id) {
        Transfer transfer = null;

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                transfer = mapRowToTransfer(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }


    @Override
    public Transfer startTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_Status_id, account_from, account_to, amount)" +
                "VALUES (2, 2, ?, ?, ?) RETURNING transfer_id;";
        try {
            int transferId = jdbcTemplate.queryForObject(sql, int.class, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
            transfer = getTransferById(transferId);
        } catch (
                CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (
                NullPointerException ex) {
            throw new NullPointerException("account does not exist");
        }
        return transfer;
    }


    public Transfer requestTransfer(Transfer transfer) {

        String sql1 =
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                        "VALUES (1, 1, ?, ?, ?);";
        try {

            int transferId = jdbcTemplate.queryForObject(sql1, int.class, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
            transfer = getTransferById(transferId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (NullPointerException ex) {
            throw new NullPointerException("account does not exist");
        }
        return transfer;
    }

    public Transfer acceptRequestById(int transferID) {
        Transfer transfer = getTransferById(transferID);

        String sql1 = "UPDATE transfer" +
                "SET transfer_status_id = 2" +
                "WHERE transfer_id = ?;";

        String sql2 = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                "VALUES(2, 2, ?, ?, ?);";
        try {
            jdbcTemplate.update(sql1, transferID);
            jdbcTemplate.update(sql2, transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (NullPointerException ex) {
            throw new NullPointerException("account does not exist");
        }
        return transfer;
    }

    public Transfer denyRequestById(int transferId) {
        Transfer transfer = getTransferById(transferId);
        String sql1 = "UPDATE transfer" +
                "SET transfer_status_id = 3" +
                "WHERE transfer_id = ?;";
        try {
            jdbcTemplate.update(sql1, transferId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (NullPointerException ex) {
            throw new NullPointerException("account does not exist");
        }
        return transfer;
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));

        return transfer;
    }


}
