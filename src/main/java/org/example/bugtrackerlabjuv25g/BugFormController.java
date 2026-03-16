package org.example.bugtrackerlabjuv25g;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.*;


@Controller
public class BugFormController {

    private final MethodValidationPostProcessor methodValidationPostProcessor;
    BugFormService bugformService;

    public BugFormController(BugFormService bugformService, MethodValidationPostProcessor methodValidationPostProcessor) {
        this.bugformService = bugformService;
        this.methodValidationPostProcessor = methodValidationPostProcessor;
    }

    @GetMapping("/reports/add")
    public String showBugForm(Model model) {
        model.addAttribute("bugForm", new CreateBugDTO());
        return "create_view";
    }

    @PostMapping("/reports/add")
    public String postBugForm(@ModelAttribute("bugForm") @Valid CreateBugDTO bugForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("Error has occured! " + bindingResult.toString());
            return "create_view";
        }
        try {
            bugformService.saveReport(bugForm);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("title", "error.bugForm", ex.getMessage());
            return "create_view";
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String homePage(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BugDTO> paged = bugformService.getPagedBugs(pageable);
        model.addAttribute("bugsReported", bugformService.getCount());
        model.addAttribute("highPriorityBugs", bugformService.getBugsByPriority(Priority.HIGH).size());
        model.addAttribute("bugs", paged);
        model.addAttribute("totalPages", paged.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "homescreen";
    }

    @GetMapping("/bugdetails")
    public String viewBugDetails(@RequestParam Long id, Model model) {
        BugDTO bug = bugformService.getReport(id);
        model.addAttribute("bugdetail", bug);
        return "details";
    }

    @GetMapping("/bugdetails/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        BugDTO bug = bugformService.getReport(id);
        //Convert BugDTO to UpdateBugDTO to fill out form with current values
        UpdateBugDTO updateForm = new UpdateBugDTO(
                bug.id(),
                bug.title(),
                bug.description(),
                bug.priority(),
                bug.development()
        );
        model.addAttribute("bugForm", updateForm);
        return "edit_view";
    }

    @PostMapping("/bugdetails/edit/{id}")
    public String postEditForm(@PathVariable("id") Long id,
                               @ModelAttribute("bugForm") @Valid UpdateBugDTO updateForm,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("Error has occured! " + bindingResult.toString());
            return "edit_view";
        }
        try {
            bugformService.updateReport(id, updateForm);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("title", "error.bugForm", e.getMessage());
            return "edit_view";
        }
        return "redirect:/bugdetails?id=" + id;
    }

    @GetMapping("/search")
    public String getSearchResult(@RequestParam(required = false) String input, Model model) {
        if (input == null || input.isEmpty()) {
            return "redirect:/";
        } else {
            model.addAttribute("bugs", bugformService.findBugsByTitleOrDescription(input));
        }
        model.addAttribute("bugsReported", bugformService.getCount());
        model.addAttribute("highPriorityBugs", bugformService.getBugsByPriority(Priority.HIGH).size());
        return "homescreen";
    }

//    @GetMapping
//    public String getAllBugsByPage(
//            Model model,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "20") int size
//    ) {
//        Pageable pageable = Pageable.ofSize(size);
//        model.addAttribute("bugsReported", bugformService.getCount());
//        model.addAttribute("highPriorityBugs", bugformService.getBugsByPriority(Priority.HIGH).size());
//        model.addAttribute("bugs", bugformService.getPagedBugs(Pageable.ofSize(size)));
//        return "homescreen";
//    }

}
