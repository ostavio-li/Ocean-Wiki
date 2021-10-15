package com.carlos.ocean.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 留言
 * @author Carlos Li
 * @date 2021/6/2
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String email;
    private String subject;
    private String content;
}
