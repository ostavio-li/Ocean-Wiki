package com.carlos.ocean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Carlos Li
 * @date 2021/5/27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article implements Serializable {
    private String title;
    private String content;
}
