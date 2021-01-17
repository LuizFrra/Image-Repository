package luiz.imageRepo.entities.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import luiz.imageRepo.dtos.ImageDTO;
import luiz.imageRepo.entities.user.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tbl_images")
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(nullable = false, length = 512, updatable = false)
    private String originalName;

    @Column(unique = true, nullable = false, length = 1024, updatable = false)
    private String remoteName;

    @Column(nullable = false, updatable = false)
    private long size;

    @Enumerated(EnumType.STRING)
    private ImageStatus status;

    @Column(columnDefinition = "Decimal(10,2) default 10")
    private double value = 10.0;

    @Column(columnDefinition = "Decimal(10,2) default 0")
    private double discount = 0;

    @Column
    private boolean forSelling = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Version
    private Integer version;

    public Image(String originalName, String remoteName, long size, ImageStatus status) {
        this.originalName = originalName;
        this.remoteName = remoteName;
        this.size = size;
        this.status = status;
    }

    public double getValueWithDiscount() {
        return value - (value * (discount/100));
    }

    @JsonIgnore
    public ImageDTO toDTO() {
        return new ImageDTO(id, value, discount, forSelling, originalName, size, user.getId(), null);
    }
}
