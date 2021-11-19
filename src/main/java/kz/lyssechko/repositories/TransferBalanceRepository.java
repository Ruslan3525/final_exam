package kz.lyssechko.repositories;

import kz.lyssechko.controllers.TransferBalanceController.TransferBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferBalanceRepository extends JpaRepository<TransferBalance, Long> {
    List<TransferBalance> findTransferBalancesByFromCardNumber(Long cardNumber);
}
