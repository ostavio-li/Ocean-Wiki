package com.carlos.ocean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

/**
 * 邮件
 * @author Carlos Li
 * @date 2021/6/2
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Mail {

    @Value("${spring.mail.username}")
    private String from;
    private String to;
    private String subject;
    private String content;

}