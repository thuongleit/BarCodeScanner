package com.whooo.babr.data.remote.upcitemdb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class ItemDeserializer extends JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        List<Item.Child> nodes = new ArrayList<>();

        JsonNode items = node.get("items");
        if (items.isArray()) {
            for (int i = 0, size = items.size(); i < size; i++) {
                JsonNode childNode = items.get(i);
                if (childNode.isObject()) {
                    Item.Child child = new Item().new Child();

                    /*
                        Item.Child child = mapper.convertValue(childNode, Item.Child.class);

                        why not use mapper: #1. it uses reflection --> cause slow performance for parsing
                        #2: can't find nested constructor for Item.Child class
                    */

                    child.ean = childNode.get("ean").asText();
                    child.upc = childNode.get("upc").asText();
                    child.title = childNode.get("title").asText();
                    child.description = childNode.get("description").asText();
                    child.brand = childNode.get("brand").asText();
                    child.model = childNode.get("model").asText();
                    child.dimension = childNode.get("dimension").asText();
                    child.weight = childNode.get("weight").asText();
                    child.currency = childNode.get("currency").asText();
                    JsonNode imagesNode = childNode.get("images");
                    if (imagesNode.isArray()) {
                        List<String> images = new ArrayList<>();
                        for (int j = 0, imageSize = imagesNode.size(); j < imageSize; j++) {
                            images.add(imagesNode.get(j).asText());
                        }
                        child.images = images;
                    }

                    nodes.add(child);
                }
            }
        }

        return new Item(nodes);
    }
}
