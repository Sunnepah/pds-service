package com.sunnepah.savewithme.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunnepah.savewithme.core.FacebookUser;

import java.util.Map;

/**
 * Created by sunnepah on 22/11/2016.
 * Sunday Ayandokun @sundayayandokun
 */
public class ResourceMapper {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static FacebookUser parseFacebookUser(Map<String, Object> responseEntity) {
        return MAPPER.convertValue(responseEntity, FacebookUser.class);
    }
}
