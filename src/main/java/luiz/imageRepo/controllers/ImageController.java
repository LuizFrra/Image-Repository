package luiz.imageRepo.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import luiz.imageRepo.entities.image.Image;
import luiz.imageRepo.entities.image.ImageStatus;
import luiz.imageRepo.entities.user.User;
import luiz.imageRepo.repositories.especification.Image.ImageSpecificationBuilder;
import luiz.imageRepo.services.image.ImageDatabaseService;
import luiz.imageRepo.services.image.ImageStorageService;
import luiz.imageRepo.services.user.UserService;
import luiz.imageRepo.utils.Functions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v1/image")
@AllArgsConstructor
public class ImageController {

    private final ImageStorageService imageStorageService;

    private final ImageDatabaseService imageDatabaseService;

    private final UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<List<Image>> uploadFile(@RequestParam("files") MultipartFile[] files, Authentication authentication) {

        List<Image> imageList = new ArrayList<>();

        long userId = Long.parseLong((String)authentication.getPrincipal());
        Optional<User> user = userService.findUserById(userId);

        user.ifPresent(value -> Arrays.stream(files).parallel().forEach((file) -> {
            String originalName = file.getOriginalFilename();
            String remoteName = Functions.generateRemoteName(originalName);
            long size = file.getSize();
            imageStorageService.save(file, remoteName);
            Image imageToSave = new Image(originalName, remoteName, size, ImageStatus.UPLOADING);
            imageToSave.setUser(value);
            Image imageDb = imageDatabaseService.create(imageToSave);
            imageList.add(imageDb);
            imageStorageService.saveOnFTP(imageDb);
        }));

        return new ResponseEntity<>(imageList, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Image> updateImage(@RequestBody Image image, Authentication authentication) {

        Optional<Image> imageOp = imageDatabaseService.findById(image.getId());
        long userId = Long.parseLong((String)authentication.getPrincipal());
        image = imageDatabaseService.update(image, userId);

        if(image == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @PostMapping("/{imageId}/buy")
    public ResponseEntity<Image> buyImage(@PathVariable long imageId, Authentication authentication) {
        long buyerId = Long.parseLong((String)authentication.getPrincipal());
        Image image = imageDatabaseService.buyImage(buyerId, imageId);

        if(image != null)
            return new ResponseEntity<>(image, HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<Image>> findByFilter(@RequestParam(value = "query", required = false) String search) {
        ImageSpecificationBuilder builder = new ImageSpecificationBuilder();
        builder.with(search);
        return new ResponseEntity<>(imageDatabaseService.findByFilter(builder.build()), HttpStatus.OK);
    }

}
