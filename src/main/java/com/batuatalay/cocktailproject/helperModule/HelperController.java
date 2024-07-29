package com.batuatalay.cocktailproject.helperModule;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HelperController {

    public Object prepareReturn(String Code, Object returnData) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", Code);
        if(returnData instanceof List) {
            map.put("size", ((List) returnData).size());
        }
        map.put("message", returnData);
        return map;


    }
}
