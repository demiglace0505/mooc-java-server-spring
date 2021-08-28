package postredirectget;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostRedirectGetController {

    private List<String> list;

    public PostRedirectGetController() {
        this.list = new ArrayList<>();
        this.list.add("testData");
    }
    
   @GetMapping("/")
   public String note(Model model) {
      model.addAttribute("list", list);
      System.out.println("list items: " + list);
      return "index";
   }
   
   @PostMapping
   public String post(@RequestParam String data) {
       System.out.println("data received: " + data);
       this.list.add(data.trim());
       return "redirect:/";
   }
    
}
