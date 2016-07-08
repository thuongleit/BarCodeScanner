package com.whooo.babr.data.remote.upcitemdb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class ItemDeserializer extends JsonDeserializer<Item> {

    @Override
    public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        TreeNode node = p.getCodec().readTree(p);
        ObjectMapper mapper = new ObjectMapper();

        List<Item.Child> nodes = new ArrayList<>();

        TreeNode items = node.get("items");
        if (items.isArray()) {
            for (int i = 0, size = items.size(); i < size; i++) {
                TreeNode childNode = node.get(i);
                if (childNode.isObject()) {
                    Item.Child child = mapper.treeToValue(childNode, Item.Child.class);
                    nodes.add(child);
                }
            }
        }

        return new Item(nodes);
    }
}
