package org.example.bugtrackerlabjuv25g;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class BugFormController {

    @Autowired
    BugFormService bugformService;

    @GetMapping("/reports/add")
    public String showBugForm(Model model){
        model.addAttribute("bugForm", new Bug());
        return "create_view";
    }

    @PostMapping("/reports/add")
    public String postBugForm(@ModelAttribute("bugform") Bug bugForm){
        System.out.println("Saved bug! " + bugForm.getTitle());
        bugformService.saveReport(bugForm);
        return "showform";
    }

    @GetMapping("/reports/all")
    public String showAllReports(Model model){
        model.addAttribute("bugs", bugformService.getAllBugs());
        return "buglist";
    }
}
