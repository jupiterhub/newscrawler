package org.jupiterhub.newscrawler.controller;

import org.jupiterhub.newscrawler.service.NewsCrawlerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HackerNewsJsonController {

    @Resource
    private NewsCrawlerService newsCrawlerService;

    @GetMapping(value = "/top", produces = MediaType.APPLICATION_JSON_VALUE)
    public String topContents() {
        return newsCrawlerService.topNewsJson();
    }
}
