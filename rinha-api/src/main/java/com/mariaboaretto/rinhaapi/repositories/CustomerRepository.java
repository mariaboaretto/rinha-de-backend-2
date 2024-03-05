package com.mariaboaretto.rinhaapi.repositories;

import com.mariaboaretto.rinhaapi.domain.CustomerAccountInfoDTO;
import com.mariaboaretto.rinhaapi.domain.CustomerTransactionDTO;
import com.mariaboaretto.rinhaapi.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepository {
    @Value("${spring.datasource.url}")
    private String connectionString;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    // Gets extract data
    public List<CustomerTransactionDTO> getExtractData(Integer id) throws SQLException, UserNotFoundException {
        List<CustomerTransactionDTO> extractData = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection(this.connectionString, this.user, this.password);

            String extractQuery = """
                    SELECT customers.customer_balance,
                    	   customers.customer_limit,
                    	   transactions.transaction_description,
                    	   transactions.transaction_value,
                    	   transactions.transaction_type,
                    	   transactions.transaction_date
                    FROM customers LEFT JOIN transactions
                    ON customers.customer_id = transactions.customer_id
                    WHERE customers.customer_id = ?
                    ORDER BY transaction_date DESC
                    LIMIT 10;
                    """;

            // Preparing statement with parameter
            PreparedStatement preparedStatement = conn.prepareStatement(extractQuery);

            // Setting customer ID
            preparedStatement.setInt(1, id);

            // Executing query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Mapping extract data
            while (resultSet.next()) {
                CustomerTransactionDTO customerTransactionDTO = new CustomerTransactionDTO();

                customerTransactionDTO.setCustomer_balance(resultSet.getInt("customer_balance"));
                customerTransactionDTO.setCustomer_limit(resultSet.getInt("customer_limit"));

                // If transaction description is not null, customer has transactions to be mapped
                if (resultSet.getString("transaction_value") != null) {
                    customerTransactionDTO.setTransaction_amount(resultSet.getInt("transaction_value"));
                    customerTransactionDTO.setTransaction_description(resultSet.getString("transaction_description"));
                    customerTransactionDTO.setTransaction_date(resultSet.getTimestamp("transaction_date"));
                    customerTransactionDTO.setTransaction_type(resultSet.getString("transaction_type").charAt(0));
                }

                // Adding transaction/customer information to extract data list
                extractData.add(customerTransactionDTO);
            }

            // Checking if result set is empty (no users found)
            if (extractData.isEmpty()) {
                throw new UserNotFoundException("User Not Found");
            }

            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return extractData;
    }

    // Updates customer balance
    public void updateCustomerBalance(Integer id, Integer amount) {
        try {
            Connection conn = DriverManager.getConnection(this.connectionString, this.user, this.password);

            String updateBalanceQuery = """
                    UPDATE customers
                    SET customer_balance = customer_balance + ?
                    WHERE customer_id = ?;
                    """;

            // Preparing statement with parameter
            PreparedStatement preparedStatement = conn.prepareStatement(updateBalanceQuery);

            // Setting amount
            preparedStatement.setInt(1, amount);
            // Setting customer ID
            preparedStatement.setInt(2, id);

            // Executing query
            preparedStatement.execute();

            conn.close();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Adds new transaction to DB
    public void createTransaction(Integer customerId, String description, Integer amount, char type) {
        try {
            Connection conn = DriverManager.getConnection(this.connectionString, this.user, this.password);

            String addTransactionQuery = """
                    INSERT INTO transactions(customer_id, transaction_description, transaction_value, transaction_type)
                    VALUES (?, ?, ?, ?);
                    """;

            // Preparing statement with parameter
            PreparedStatement preparedStatement = conn.prepareStatement(addTransactionQuery);

            // Setting ID
            preparedStatement.setInt(1, customerId);
            // Setting description
            preparedStatement.setString(2, description);
            // Setting ID
            preparedStatement.setInt(3, amount);
            // Setting type
            preparedStatement.setString(4, String.valueOf(type));

            // Executing query
            preparedStatement.execute();

            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // Gets customer account information (balance limit)
    public CustomerAccountInfoDTO getAccountInfo(Integer customerId) {
        CustomerAccountInfoDTO customerAccountInfo = new CustomerAccountInfoDTO();

        try {
            Connection conn = DriverManager.getConnection(this.connectionString, this.user, this.password);

            String selectQuery = """
                SELECT customer_balance,
                	   customer_limit
                FROM customers
                WHERE customer_id = ?;
                """;

            // Preparing statement with parameter
            PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);

            // Setting ID
            preparedStatement.setInt(1, customerId);

            // Executing query
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                customerAccountInfo.setSaldo(resultSet.getInt("customer_balance"));
                customerAccountInfo.setLimite(resultSet.getInt("customer_limit"));
            }

            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customerAccountInfo;
    }
}
