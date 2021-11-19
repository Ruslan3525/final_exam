package kz.lyssechko.controllers.userController;

import kz.lyssechko.PasswordEncoder.PasswordEncoder;
import kz.lyssechko.cardGenerator.CreditCardNumberGenerator;
import kz.lyssechko.controllers.cardController.Card;
import kz.lyssechko.jwt.JwtTokenHelper;
import kz.lyssechko.repositories.CardRepository;
import kz.lyssechko.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

import static java.util.Objects.nonNull;

@RestController
public class UserController {
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    private static final char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        User userObj = userRepository.findUserByIin(user.getIin());
        System.out.println(user.getIin().toCharArray());
        if (nonNull(userObj)) {
            return new ResponseEntity<>("You already registered", HttpStatus.NOT_IMPLEMENTED);
        }
        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(numbers[random.nextInt(10)]);
        }
        user.setPassword(PasswordEncoder.hashcode(user.getPassword()));
        Card card = new Card(CreditCardNumberGenerator.generate(), PasswordEncoder.hashcode(builder.toString()),
                false, 0D, user);
        user.setCard(card);
        userRepository.save(user);
        cardRepository.save(card);
        return new ResponseEntity<>("Successfully registered, your pin_code of an account " +
                builder, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        User userObj = userRepository.findUserByIinAndPassword(user.getIin(),
                PasswordEncoder.hashcode(user.getPassword()));
        if (nonNull(userObj)) {
            Card card = userObj.getCard();
            return new ResponseEntity<>("Bearer " + JwtTokenHelper.generateJwt(userObj.getPhoneNumber(),
                    card.getCardNumber()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad credentials", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/user/updatePassword")
    public ResponseEntity<Object> updatePassword(@RequestBody User user, @RequestParam("newPassword") String newPassword) {
        User userObj = userRepository.findUserByIinAndPassword(user.getIin(),
                PasswordEncoder.hashcode(user.getPassword()));
        if (nonNull(userObj)) {
            if (!PasswordEncoder.hashcode(newPassword).equals(userObj.getPassword())) {
                userObj.setPassword(PasswordEncoder.hashcode(newPassword));
                userRepository.save(userObj);
                return new ResponseEntity<>("The user's password changed successfully", HttpStatus.OK);
            }
            return new ResponseEntity<>("The user's password is the same as the previous one",
                    HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestParam("id") Long id){
        userRepository.deleteById(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
}
