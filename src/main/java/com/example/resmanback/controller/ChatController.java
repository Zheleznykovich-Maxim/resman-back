package com.example.resmanback.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping
    public String home(@RequestParam("prompt") String prompt) {
        return chatClient.prompt(prompt)
                .call()
                .content();
    }

}