package luiz.imageRepo.services.image;

import lombok.AllArgsConstructor;
import luiz.imageRepo.exceptions.ImageNotExistException;
import luiz.imageRepo.exceptions.NotMeetRequisitesException;
import luiz.imageRepo.entities.image.Image;
import luiz.imageRepo.entities.user.User;
import luiz.imageRepo.repositories.image.ImageRepository;
import luiz.imageRepo.services.user.UserService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageDatabaseService {

    private final ImageRepository imageRepository;

    private final UserService userService;

    public Image create(Image image) {
        return imageRepository.save(image);
    }

    public Image update(Image image, long userId) {

        Optional<Image> imageOp = findById(image.getId());
        Optional<User> userDb = userService.findUserById(userId);

        if(imageOp.isPresent()) {
            if(image.getVersion().equals(imageOp.get().getVersion()) && imageOp.get().getUser().getId() == userId) {
                if (imageOp.get().getUser().getId() == userId) {
                    userDb.ifPresent(image::setUser);
                    return imageRepository.save(image);
                }
            } else {
                throw new NotMeetRequisitesException("You Haven't Permission To Edit this Image", image.getId());
            }
        } else {
            throw new ImageNotExistException("Image Not Exist", image.getId());
        }

        return null;
    }

    public Image buyImage(long buyerId, long imageId) {
        Optional<Image> imageOp = findById(imageId);
        Optional<User> buyerOp = userService.findUserById(buyerId);

        if(buyerOp.isPresent() && imageOp.isPresent()) {
            User buyer = buyerOp.get();
            Image image = imageOp.get();
            User seller = image.getUser();

            boolean buyerHaveEnoughMoney = buyer.subtractFromCash(image.getValueWithDiscount());
            boolean sellerIsNotBuyer = seller.getId() != buyerId;

            if(!sellerIsNotBuyer)
                throw new NotMeetRequisitesException("You can't Buy An Image From yourself.", null);

            if(!buyerHaveEnoughMoney)
                throw new NotMeetRequisitesException("You Haven't Enough Money to Buy The Image", null);

            seller.addToCash(image.getValueWithDiscount());
            image.setUser(buyer);
            image.setForSelling(false);
            Image result = imageRepository.save(image);
            userService.updateUser(buyer);
            userService.updateUser(seller);
            return result;

        } else {
            throw new ImageNotExistException("Imag Not Exist", imageId);
        }
    }

    public List<Image> findByOriginalName(String originalName) {
        return imageRepository.findByOriginalName(originalName);
    }

    public Optional<Image> findByRemoteName(String RemoteName) {
        return imageRepository.findByRemoteName(RemoteName);
    }

    public List<Image> findAll() {
        List<Image> imageList = new ArrayList<>();
        imageRepository.findAll().forEach(imageList::add);
        return imageList;
    }

    public List<Image> findByFilter(Specification<Image> specification) {
        return imageRepository.findAll(specification);
    }

    public Optional<Image> findById(long id) {
        return imageRepository.findById(id);
    }
}
