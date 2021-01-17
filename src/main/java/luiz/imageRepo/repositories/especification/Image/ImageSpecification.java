package luiz.imageRepo.repositories.especification.Image;

import luiz.imageRepo.entities.image.Image;
import luiz.imageRepo.repositories.especification.common.CommonSpecification;
import luiz.imageRepo.repositories.especification.common.SearchCriteria;

public class ImageSpecification extends CommonSpecification<Image> {

    public ImageSpecification(SearchCriteria searchCriteria) {
        super(searchCriteria);
    }

}

