package pl.dk.cnavigator.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Content {

    private Map<String, Object> identity = new LinkedHashMap<>();

    private Map<String, Object> titles = new LinkedHashMap<>();

    private Map<String, Object> params = new LinkedHashMap<>();

    private Map<String, Object> objects = new LinkedHashMap<>();

    private Map<String, Object> others = new LinkedHashMap<>();

}
