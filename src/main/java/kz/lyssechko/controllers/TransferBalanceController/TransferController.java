package kz.lyssechko.controllers.TransferBalanceController;

import kz.lyssechko.controllers.cardController.Card;
import kz.lyssechko.repositories.CardRepository;
import kz.lyssechko.repositories.TransferBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.nonNull;

@RestController
public class TransferController {
    @Autowired
    private TransferBalanceRepository transferBalanceRepository;

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/card/payments/{cardNumber}")
    public ResponseEntity<Object> payments(@PathVariable("cardNumber") Long cardNumber) {
        List<TransferBalance> transferBalance = transferBalanceRepository.findTransferBalancesByFromCardNumber(cardNumber);
        if (nonNull(transferBalance)) {
            return new ResponseEntity<>(transferBalance, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid card number", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Object> doTransfer(@RequestBody TransferBalance transferBalance) {
        if (transferBalance.getAmount() <= 100D) {
            return new ResponseEntity<>("the minimum transfer amount is 100", HttpStatus.NOT_IMPLEMENTED);
        }
        Card fromCard = cardRepository.findCardByCardNumber(transferBalance.getFromCardNumber());
        if (nonNull(fromCard)) {
            Card toCard = cardRepository.findCardByCardNumber(transferBalance.getToCardNumber());
            if (nonNull(toCard)) {
                fromCard.setBalance(fromCard.getBalance() - transferBalance.getAmount());
                toCard.setBalance(toCard.getBalance() + transferBalance.getAmount());
                transferBalance.setDate_of_transaction(LocalDate.now().toString());
                cardRepository.save(fromCard);
                cardRepository.save(toCard);
                transferBalanceRepository.save(transferBalance);
                return new ResponseEntity<>("The transfer was successful", HttpStatus.OK);
            }

            return new ResponseEntity<>("ToCard is invalid", HttpStatus.NOT_IMPLEMENTED);
        }

        return new ResponseEntity<>("From Card is invalid", HttpStatus.NOT_IMPLEMENTED);
    }
}
