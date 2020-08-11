package org.jupiterhub.newscrawler.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
public class HackerNewsCrawlerService implements NewsCrawlerService {

    private static final Gson gson =  new GsonBuilder().setPrettyPrinting().create();
    public static final String ENDPOINT_TOP_STORIES = "https://hacker-news.firebaseio.com/v0/topstories.json";
    public static final String ENDPOINT_STORY = "https://hacker-news.firebaseio.com/v0/item/%s.json";
    public static final int NO_OF_STORIES_TO_GET = 20;

    @Override
    public String topNewsJson() {

        try {
            InputStream in = new URL(ENDPOINT_TOP_STORIES).openStream();
            String topNewsIdJson = IOUtils.toString(in, StandardCharsets.UTF_8);
            Type collectionType = new TypeToken<List<String>>(){}.getType();
            List<String> topNewsIds = gson.fromJson(topNewsIdJson, collectionType);

            return topNewsIds.subList(0, NO_OF_STORIES_TO_GET - 1).stream().map(getStoryJson())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()).toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Function<String, String> getStoryJson() {
        return i -> {
            try {
                String endpointFormatted = String.format(ENDPOINT_STORY, i);
//                log.debug("Processing " + endpointFormatted);
                return gson.fromJson(IOUtils.toString(new URL(endpointFormatted).openStream(), StandardCharsets.UTF_8), JsonElement.class).toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
