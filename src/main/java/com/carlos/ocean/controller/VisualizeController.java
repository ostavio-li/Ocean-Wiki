package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.Trib;
import com.carlos.ocean.vo.Result;
import com.carlos.util.kg.KGUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/6/4
 */

@RestController
@RequestMapping("/vis")
public class VisualizeController {

    @GetMapping("")
    public Result visualize(
            @RequestParam("sub") String sub,
            @RequestParam("n") int n
    ) {
        List<Trib> list = KGUtil.getInstance().visualize(sub, n);
        return Result.ok().data("tribs", list);
    }

}
