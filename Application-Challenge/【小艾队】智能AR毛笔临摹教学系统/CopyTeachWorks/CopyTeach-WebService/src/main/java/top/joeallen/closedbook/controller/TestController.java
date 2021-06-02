package top.joeallen.closedbook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.joeallen.closedbook.service.TestService;

import javax.annotation.Resource;

/**
 * @author JoeAllen_Li @Date 2021/3/19 16:03
 * @describe
 */
@Controller
public class TestController {
    @Resource
    TestService testService;

    @RequestMapping(value = {"/"})
    public String index() {
        return "live";
    }
    @RequestMapping(value = {"/video"})
    public String video() {
        return "video";
    }
//    @RequestMapping(value = {"/"})
//    public String index() {
//        return "index";
//    }
//
//    @RequestMapping(value = {"/live"})
//    public String live() {
//        return "live";
//    }

    @ResponseBody
    @PostMapping(value = "/closedbook")
    public String closedbook(String closedbook) {

        return closedbook;
    }
}
