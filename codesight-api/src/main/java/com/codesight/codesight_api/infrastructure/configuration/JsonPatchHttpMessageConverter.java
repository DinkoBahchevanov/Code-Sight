package com.codesight.codesight_api.infrastructure.configuration;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonReader;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

@Component
public class JsonPatchHttpMessageConverter extends AbstractHttpMessageConverter<JsonPatch> {

    public JsonPatchHttpMessageConverter() {
        super(MediaType.valueOf("application/json-patch+json"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return JsonPatch.class.isAssignableFrom(clazz);
    }

    @Override
    protected JsonPatch readInternal(Class<? extends JsonPatch> aClass, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
        try (JsonReader reader = Json.createReader(inputMessage.getBody())) {
            return Json.createPatch(reader.readArray());
        } catch (Exception e) {
            throw new HttpMessageNotReadableException(e.getMessage(), inputMessage);
        }
    }

    @Override
    protected void writeInternal(JsonPatch patch, HttpOutputMessage httpOutputMessage) throws HttpMessageNotWritableException {
        try {
            throw new ExecutionControl.NotImplementedException("The write Json patch is not implemented");
        } catch (ExecutionControl.NotImplementedException e) {
            e.printStackTrace();
        }
    }
}
