package com.whooo.babr.data.remote.searchupc;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ItemDeserializer extends JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonParser p, DeserializationContext descx) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        List<Item.Child> nodes = new ArrayList<>();

        int index = 0;
        do {
            JsonNode childNode = node.get(String.valueOf(index));
            if (childNode == null || !childNode.isObject()) {
                break;
            }
            /*
                Item.Child child = mapper.convertValue(childNode, Item.Child.class);

                why not use mapper: #1. it uses reflection --> cause slow performance for parsing
                #2: can't find nested constructor for Item.Child class
             */
            Item.Child child = new Item().new Child();
            child.productName = childNode.get("productname").asText();
            child.imageUrl = childNode.get("imageurl").asText();
            child.productUrl = childNode.get("producturl").asText();
            child.price = childNode.get("price").asText();
            child.currency = childNode.get("currency").asText();
            child.salePrice = childNode.get("saleprice").asText();
            child.storeName = childNode.get("storename").asText();

            nodes.add(child);
            index++;
        } while (true);

        return new Item(nodes);
    }
}
