package luiz.imageRepo.entities.user;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "tbl_authorities")
public class Authoritie {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @Column(nullable = false)
    private ROLES authorite;

    public Authoritie(ROLES authorite, User user) {
        setAuthorite(authorite);
        this.user = user;
    }

    public ROLES setAuthorite(ROLES role) {
        this.authorite = role;
        return this.authorite;
    }

    public ROLES getAuthorite() {
        return this.authorite;
    }


}
