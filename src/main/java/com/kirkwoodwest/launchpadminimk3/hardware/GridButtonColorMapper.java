package com.kirkwoodwest.launchpadminimk3.hardware;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GridButtonColorMapper {
    private Map<String, int[]> colorMap;

    public void load(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Map<String, int[]>> jsonMap = objectMapper.readValue(new File(filePath), new TypeReference<>(){} );
            colorMap = jsonMap.get("colors");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getVelocity(GridButtonColor state) {
        return colorMap.get(state.name())[0];
    }
    public int getChannel(GridButtonColor state) {
        return colorMap.get(state.name())[1];
    }
}
