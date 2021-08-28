package todoapplication;

import org.springframework.stereotype.Controller;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoApplicationController {

    private Map<String, Task> tasks;

    public TodoApplicationController() {
        this.tasks = new TreeMap<>();
        Task task = new Task("test task 1");
        this.tasks.put(task.getId(), task);
    }

    @PostMapping("/")
    public String post(@RequestParam String name) {
        Task task = new Task(name);
        this.tasks.put(task.getId(), task);
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("items", this.tasks.values());
        return "index";
    }
    
    @GetMapping("/{id}")
    public String todo(Model model, @PathVariable String id) {
        System.out.println("getmapping id " + id);
        // retrieve object from DB
        Task task = this.tasks.get(id);
        System.out.println("selected task: " + task);
        // update the value of checked
        task.setChecked(task.getChecked() + 1);
        System.out.println("updated task + 1: " + task);
        
        model.addAttribute("item", this.tasks.get(id));

        return "todo";
    }
}
