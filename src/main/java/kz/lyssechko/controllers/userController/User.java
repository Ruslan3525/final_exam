package kz.lyssechko.controllers.userController;

import kz.lyssechko.controllers.cardController.Card;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "iin", unique = true)
    private String iin;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", unique = true, length = 12, nullable = false)
    private String phoneNumber;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Card card;
}
