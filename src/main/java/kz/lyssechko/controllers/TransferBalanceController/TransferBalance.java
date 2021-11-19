package kz.lyssechko.controllers.TransferBalanceController;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_card_number", nullable = false, length = 16)
    private String fromCardNumber;

    @Column(name = "to_card_number", nullable = false, length = 16)
    private String toCardNumber;

    @Column(name = "Date", columnDefinition = "TEXT", nullable = false)
    private String date_of_transaction;

    @Column(name = "amount", nullable = false)
    private Double amount;
}
