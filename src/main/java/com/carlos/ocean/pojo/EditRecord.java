package com.carlos.ocean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Carlos Li
 * @date 2021/5/28
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditRecord {

    private String username;
    private String title;

}
