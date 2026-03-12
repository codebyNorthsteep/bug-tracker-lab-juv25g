package org.example.bugtrackerlabjuv25g;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class BugFormController {

    BugFormService bugformService;

    public BugFormController(BugFormService bugformService){
        this.bugformService = bugformService;
    }

    @GetMapping("/reports/add")
    public String showBugForm(Model model){
        model.addAttribute("bugForm", new CreateBugDTO());
        return "create_view";
    }

    @PostMapping("/reports/add")
    public String postBugForm(@ModelAttribute("bugForm") @Valid CreateBugDTO bugForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            System.out.println("Error has occured! " + bindingResult.toString());
            return "create_view";
        }
        bugformService.saveReport(bugForm);
        return "redirect:/";
    }

    @GetMapping("/reports/all")
    public String showAllReports(Model model){
        model.addAttribute("bugs", bugformService.getAllBugs());
        return "buglist";
    }

    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("bugsReported", bugformService.getCount());
        model.addAttribute("highPriorityBugs", bugformService.getBugsByPriority(Priority.HIGH).size());
        model.addAttribute("bugs", bugformService.getAllBugs());
        return "homescreen";
    }

    @GetMapping("/bugdetails")
    public String viewBugDetails(@RequestParam Long id, Model model){
        var bug = bugformService.getReport(id);
        if(bug.isEmpty()){
            //handle 404
            return "redirect:/";
        }
        model.addAttribute("bugdetail", bug.get());
        return "bugdetails";
    }
}
