package kz.lyssechko.controllers.cardController;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import kz.lyssechko.PasswordEncoder.PasswordEncoder;
import kz.lyssechko.jwt.JwtTokenHelper;
import kz.lyssechko.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/showMoney")
    public ResponseEntity<Object> showMoney(@RequestParam("pin_code") String pin_code,
                                            HttpServletRequest request) {
        if (pin_code.length() > 4) {
            return new ResponseEntity<>("Pin-code is invalid", HttpStatus.NOT_IMPLEMENTED);
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = authorizationHeader.replace("Bearer", "");
        Claims body = JwtTokenHelper.decodeJwt(token);
        String card_Number = (String) body.get("card_number");
        Card card = cardRepository.findCardByCardNumberAndPinCode(card_Number, PasswordEncoder.hashcode(pin_code));
        if (nonNull(card)) {
            return new ResponseEntity<>(card.getCardNumber() + " - your balance: " + card.getBalance(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Pin-code is invalid", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/changePinCode")
    public ResponseEntity<Object> changePinCode(@RequestBody Card card, @RequestParam("newPinCode") String newPinCode) {
        Card cardObj = cardRepository.findCardByCardNumberAndPinCode(card.getCardNumber(),
                PasswordEncoder.hashcode(card.getPinCode()));
        if (nonNull(cardObj)) {
            cardObj.setPinCode(PasswordEncoder.hashcode(newPinCode));
            cardRepository.save(cardObj);
            return new ResponseEntity<>("Pin-code of card changed successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad Credentials", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/block")
    public ResponseEntity<Object> block(@RequestBody Card card) {
        Card cardObj = cardRepository.findCardByCardNumberAndPinCode(card.getCardNumber(),
                PasswordEncoder.hashcode(card.getPinCode()));
        if (nonNull(cardObj)) {
            cardObj.setCardBlocked(true);
            cardRepository.save(cardObj);
            return new ResponseEntity<>("The card blocked successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad Credentials", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/open")
    public ResponseEntity<Object> open(@RequestBody Card card) {
        Card cardObj = cardRepository.findCardByCardNumberAndPinCode(card.getCardNumber(),
                PasswordEncoder.hashcode(card.getPinCode()));
        if (nonNull(cardObj)) {
            cardObj.setCardBlocked(false);
            cardRepository.save(cardObj);
            return new ResponseEntity<>("The card unblocked successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad credentials", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/updateFunds")
    public ResponseEntity<Object> updateFunds(@RequestBody Card card) {
        Card cardObj = cardRepository.findCardByCardNumber(card.getCardNumber());
        if (nonNull(cardObj)) {
            cardObj.setBalance(cardObj.getBalance() + card.getBalance());
            cardRepository.save(cardObj);
            return new ResponseEntity<>("Your cash account has been updated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Card number is invalid", HttpStatus.NOT_IMPLEMENTED);
    }
}
