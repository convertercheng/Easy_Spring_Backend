package com.qhieco.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-15 下午3:41
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping
@Slf4j
public class FileWeb {

    @Value("${web.upload-path}")
    private String webUploadPath;

    @Autowired
    private ResourceLoader resourceLoader;


    @RequestMapping(value = "file/**/{filename:.+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> activity(@PathVariable String filename, HttpServletRequest request) {
        Pattern pattern = Pattern.compile("file(/.+)");
        Matcher m = pattern.matcher(request.getRequestURL());
//        filename
        String path = null;
        if(m.find()){
            System.out.println(m.group(1));
            path = m.group(1);
        }else {
            path = filename;
        }
        System.out.println(resourceLoader.getResource("file:" + Paths.get(webUploadPath, path).toString()));
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(webUploadPath, path).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

//    public static void main(String... args){
//        Pattern pattern = Pattern.compile("(/\\w+)*(/.+)");
//        Matcher m = pattern.matcher("/picture/achievement/184a84affb3f41aaa73e539cfc604eb7.pdf");
//        if(m.find()){
//            System.out.println(m.group());
//        }
//    }
}
