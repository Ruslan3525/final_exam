package kz.lyssechko.repositories;

import kz.lyssechko.controllers.cardController.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findCardByCardNumberAndPinCode(String cardNumber, String pinCode);

    Card findCardByCardNumber(String cardNumber);
}
