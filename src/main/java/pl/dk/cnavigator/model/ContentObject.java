package pl.dk.cnavigator.model;

import lombok.Data;

import java.util.UUID;

@Data
public class ContentObject {

    private String itemId;

    private String contentType;

    private UUID id;

    private String title;

    private UUID typeId;

    private String typeTitle;

}
