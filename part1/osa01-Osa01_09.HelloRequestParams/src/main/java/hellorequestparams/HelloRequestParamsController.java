package hellorequestparams;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloRequestParamsController {
    @GetMapping("/params")
    @ResponseBody
    public String hellorequestparams(@RequestParam Map<String, String> params) {
        return params.keySet().toString();
    }
    
    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam String param) {
        return "Hello " + param;
    }
}
