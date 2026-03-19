package org.example.bugtrackerlabjuv25g;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class BugFormController {
    private static final Logger logger = LoggerFactory.getLogger(BugFormController.class);

    BugFormService bugformService;

    public BugFormController(BugFormService bugformService) {
        this.bugformService = bugformService;
    }

    @GetMapping("/reports/add")
    public String showBugForm(Model model) {
        model.addAttribute("bugForm", new CreateBugDTO());
        return "create_view";
    }

    @PostMapping("/reports/add")
    public String postBugForm(@ModelAttribute("bugForm") @Valid CreateBugDTO bugForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors in bug creation form: {} error(s)", bindingResult.getErrorCount());
            return "create_view";
        }
        try {
            bugformService.saveReport(bugForm);
        } catch (IllegalArgumentException ex) {
            //Red marking in form when trying to update with invalid data, duplicate title in same development area
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
            logger.debug("Validation errors in bug edit form for id {}: {} error(s)", id, bindingResult.getErrorCount());
            return "edit_view";
        }
        try {
            bugformService.updateReport(id, updateForm);
        } catch (IllegalArgumentException e) {
            //Red marking in form when trying to update with invalid data, duplicate title in same development area
            bindingResult.rejectValue("title", "error.bugForm", e.getMessage());
            return "edit_view";
        }
        return "redirect:/bugdetails?id=" + id;
    }

    @GetMapping("/bugdetails/delete/confirm")
    public String confirmDelete(@RequestParam Long id, Model model) {
        BugDTO bug = bugformService.getReport(id);
        model.addAttribute("bugdetail", bug);

        return "confirm_delete";
    }

    @PostMapping("/bugdetails/delete")
    public String deleteReport(@RequestParam Long id) {
        bugformService.deleteReport(id);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String getSearchResult(@RequestParam(required = false) String input,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size,
                                  Model model) {
        if (input == null || input.isBlank()) {
            return "redirect:/";
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("priorityOrder"));
            Page<BugDTO> paged = bugformService.getSearchByTitleOrDescription(input, pageable);
            model.addAttribute("bugs", paged);
            model.addAttribute("highPriorityBugs", bugformService.getBugsByPriority(Priority.HIGH).size());
            model.addAttribute("bugsReported", bugformService.getCount());
            model.addAttribute("totalPages", paged.getTotalPages());
            model.addAttribute("pageSize", size);
            model.addAttribute("currentPage", page);
            model.addAttribute("search", input);
        }
        return "homescreen";
    }


}
