package com.example.demo.deserializer;

import com.example.demo.entity.AppElementContent;
import com.example.demo.entity.AttachmentContent;
import com.example.demo.entity.TextContent;
import com.example.demo.util.ElementTypeEnum;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AppElementContentDeserializer extends JsonDeserializer<AppElementContent> {
    @Override
    public AppElementContent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        AppElementContent appElementContent = null;
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        final Long id = node.get("id").asLong();
        ElementTypeEnum type = ElementTypeEnum.valueOf(node.get("type").asText());
        switch (type) {
            case TEXT:
            case BIG_TITLE:
            case MEDIUM_TITLE:
            case SMALL_TITLE:
            case VERY_SMALL_TITLE:
            case SOURCE_CODE:
            case HYPERLINK:
                appElementContent = TextContent
                        .builder()
                        .text(node.get("text").asText())
                        .build();
                break;
            case ATTACHMENT:
                appElementContent = AttachmentContent
                        .builder()
                        .name(node.get("name").asText())
                        .width(node.get("width").asInt())
                        .height(node.get("height").asInt())
                        .data(node.get("data").binaryValue())
                        .build();
        }

        appElementContent.setId(id);
        appElementContent.setType(type.name());
        return appElementContent;
    }
}
