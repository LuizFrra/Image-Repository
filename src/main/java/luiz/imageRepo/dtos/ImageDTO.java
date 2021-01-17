package luiz.imageRepo.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import luiz.imageRepo.entities.image.Image;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class ImageDTO {

    private long id;

    private Double value;

    private Double discount;

    private Boolean forSelling;

    private String originalName;

    private long size;

    private long ownerId;

    @JsonIgnore
    public HashMap<String, Object> errors;

    @JsonIgnore
    public boolean isValid() {
        errors = new HashMap<>();

        if(value != null && value <= 0)
            errors.put("value", "product value must be greater than $0");
        if(discount != null && (discount < 0 || discount > 100))
            errors.put("discount", "discount must be between 0 and 100");
        if(id <= 0)
            errors.put("imageId", "imageId must be greater than 0");

        return errors.size() == 0;
    }

    public void setImageProperties(Image image) {
        if(value != null)
            image.setValue(value);
        if(discount != null)
            image.setDiscount(discount);
        if(forSelling != null)
            image.setForSelling(forSelling);
    }

    public double getValueWithDiscount() {
        return value - (value * (discount/100));
    }

}
