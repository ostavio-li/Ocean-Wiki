package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.Mail;
import com.carlos.ocean.pojo.Message;
import com.carlos.ocean.service.MailService;
import com.carlos.ocean.service.MessageBoardService;
import com.carlos.ocean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 留言板
 * @author Carlos Li
 * @date 2021/6/2
 */

@RestController
@RequestMapping("/mb")
public class MessageBoardController {

    @Autowired
    private MessageBoardService messageBoardService;

    @Autowired
    private MailService mailService;

    @GetMapping("")
    public Result listAllMessages() {
        return Result.ok().data("messages", messageBoardService.list());
    }

    @GetMapping("/reply")
    public Result reply(
            @RequestParam("messageId") int messageId,
            @RequestParam("to") String to,
            @RequestParam("content") String content
    ) {
        Mail mail = new Mail(
                "carlosli99@163.com",
                to,
                "海宝百科客服回复",
                content
        );
        if (mailService.sendMail(mail)) {
            messageBoardService.deleteById(messageId);
            return Result.ok();
        } else {
            return Result.error().message("邮件发送失败");
        }
    }

    @PostMapping("")
    public Result addMessage(
            @RequestBody Message message
    ) {
        if (messageBoardService.save(message) > 0) {
            return Result.ok();
        } else {
            return Result.error().message("提交失败！");
        }
    }

}
