package com.mariaboaretto.rinhaapi.repositories;
import com.mariaboaretto.rinhaapi.domain.CustomerAccountInfoDTO;
import com.mariaboaretto.rinhaapi.domain.entities.Customer;
import com.mariaboaretto.rinhaapi.domain.CustomerTransactionDTO;
import com.mariaboaretto.rinhaapi.domain.entities.Transaction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    // Updates customer balance
    @Modifying
    @Query("""
            UPDATE Customer c
            SET c.balance = c.balance + :amount
            WHERE c.id = :id
            """)
    void updateCustomerBalance(@Param("id") Integer id, @Param("amount") Integer amount);

    // Adds a new transaction to DB
    @Modifying
    @Query(value = """
            INSERT INTO transactions(customer_id, transaction_description, transaction_value, transaction_type)
            VALUES (:customerId, :description, :amount, :type)
            """, nativeQuery = true)
    public void createTransaction(@Param("customerId") Integer customerId, @Param("description") String description,
                                  @Param("amount") Integer amount, @Param("type") char type);

    // Gets necessary data to generate extract
    @Query(value = """
            SELECT new com.mariaboaretto.rinhaapi.domain.CustomerTransactionDTO(
                   COALESCE(t.description, ''),
            	   COALESCE(t.amount, 0),
            	   COALESCE(t.type, ''),
            	   COALESCE(t.date, CURRENT_DATE),
            	   c.balance,
            	   c.limit)
            FROM Customer c LEFT JOIN Transaction t
            ON c.id = t.customerId
            WHERE c.id = :customerId
            ORDER BY date DESC
            LIMIT 10
            """)
    public List<CustomerTransactionDTO> getExtractData(@Param("customerId") Integer customerId);

    @Query(value = """
            SELECT new com.mariaboaretto.rinhaapi.domain.CustomerAccountInfoDTO(
                c.balance,
                c.limit)
                FROM Customer c
                WHERE c.id = :customerId
            """)
    public CustomerAccountInfoDTO getAccountInfo(@Param("customerId") Integer customerId);
}
