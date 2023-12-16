package com.example.social.media.platform.API.controller;

import com.example.social.media.platform.API.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupMessage")
public class MessageController {


    @MessageMapping("/message")
    @SendTo("/topic/return-to")
    public Message getContent(@RequestBody Message message) {

//        try {
//            //processing
////            Thread.sleep(2000);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return message;
    }

    @GetMapping("/get")
    public String getString(){
        return "done";
    }


}