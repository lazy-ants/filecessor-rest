package com.lazyants.filecessor.model;

import com.lazyants.filecessor.controller.PhotoController;
import com.lazyants.filecessor.utils.ExtensionGenerator;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class PhotoResource extends ResourceSupport {
    private final Photo photo;

    public PhotoResource(Photo photo) {
        this.photo = photo;

        String path = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/")
                .replaceQuery("")
                .build()
                .toString();

        add(linkTo(methodOn(PhotoController.class).get(photo.getId())).withSelfRel());
        add(new Link(path + "original/" + photo.getId() + "." + photo.getExtension(), "original"));

        List<String> resizableFormats = asList(ExtensionGenerator.IMAGE_JPEG, ExtensionGenerator.IMAGE_PNG, ExtensionGenerator.IMAGE_TIFF);
        if (resizableFormats.contains(photo.getExtension())) {
            add(new Link(new UriTemplate(path + "transform/resize_{width}x{height}/" + photo.getId() + "." + photo.getExtension()), "resize"));
            add(new Link(new UriTemplate(path + "transform/crop_{width}x{height}/" + photo.getId() + "." + photo.getExtension()), "crop"));
            add(new Link(new UriTemplate(path + "transform/crop_coordinates_{x1}x{y1}_{x2}x{y2}/" + photo.getId() + "." + photo.getExtension()), "coordinates"));
            add(new Link(new UriTemplate(path + "transform/rotate_{angle}/" + photo.getId() + "." + photo.getExtension()), "rotate"));
        }
    }
}
