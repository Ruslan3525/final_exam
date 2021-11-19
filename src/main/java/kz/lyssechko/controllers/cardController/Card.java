package kz.lyssechko.controllers.cardController;

import kz.lyssechko.controllers.userController.User;

import javax.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "pin", nullable = false, columnDefinition = "TEXT")
    private String pinCode;

    @Column(name = "is_card_blocked", nullable = false)
    private boolean isCardBlocked;

    @Column(name = "balance", length = 999999999)
    private Double balance;

    @OneToOne
    private User user;

    public Card() {
    }

    public Card(String cardNumber, String pinCode, boolean isCardBlocked, Double balance, User user) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.isCardBlocked = isCardBlocked;
        this.balance = balance;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public boolean isCardBlocked() {
        return isCardBlocked;
    }

    public void setCardBlocked(boolean cardBlocked) {
        isCardBlocked = cardBlocked;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", pinCode='" + pinCode + '\'' +
                ", isCardBlocked=" + isCardBlocked +
                ", balance=" + balance +
                ", user=" + user +
                '}';
    }
}

