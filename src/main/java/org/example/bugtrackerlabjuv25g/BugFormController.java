package org.example.bugtrackerlabjuv25g;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BugFormController {

    @GetMapping("/report")
    public String showBugForm(Model model){
        model.addAttribute("bugForm", new Bug());
        return "create_view";
    }

    @PostMapping("/report")
    public String postBugForm(@ModelAttribute("bugform") Bug bugForm){
        System.out.println("Saved bug! " + bugForm.getTitle());
        return "showform";
    }
}
