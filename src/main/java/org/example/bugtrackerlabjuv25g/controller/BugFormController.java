package org.example.bugtrackerlabjuv25g.controller;

import jakarta.validation.Valid;
import org.example.bugtrackerlabjuv25g.dto.BugDTO;
import org.example.bugtrackerlabjuv25g.dto.CreateBugDTO;
import org.example.bugtrackerlabjuv25g.dto.UpdateBugDTO;
import org.example.bugtrackerlabjuv25g.model.Priority;
import org.example.bugtrackerlabjuv25g.service.BugFormService;
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


/**
 * Controller responsible for handling bug report-related views and actions.
 * This includes creating, updating, deleting, listing, and viewing bugs,
 * as well as searching for bugs by title or description.
 */
@Controller
public class BugFormController {
    private static final Logger logger = LoggerFactory.getLogger(BugFormController.class);

    BugFormService bugformService;

    public BugFormController(BugFormService bugformService) {
        this.bugformService = bugformService;
    }

    /**
     * Displays the form for adding a new bug report.
     *
     * @param model the model object used to pass attributes to the view
     * @return the name of the view template for creating a new bug report
     */
    @GetMapping("/reports/add")
    public String showBugForm(Model model) {
        model.addAttribute("bugForm", new CreateBugDTO());
        return "create_view";
    }

    /**
     * Handles the submission of the bug report form. Validates the form data, and if valid, saves the report.
     * If errors occur during validation or data processing, the form is returned with appropriate error messages.
     *
     * @param bugForm       The bug report form data encapsulated in a {@code CreateBugDTO}.
     * @param bindingResult The object to hold validation errors for the form fields.
     * @return A string indicating the next view to be rendered. Returns "create_view" if there are validation errors
     * or issues saving the report, otherwise redirects to the home page ("/").
     */
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

    /**
     * Handles the request to load the home page of the bug tracker application.
     * Populates the model with the necessary attributes for rendering the home screen.
     *
     * @param model The Model object used to add attributes for rendering the view.
     * @param page  The current page number for pagination (default is 0).
     * @param size  The number of items to display per page in the paginated bug list (default is 20).
     * @return The name of the Thymeleaf template for the home screen.
     */
    @GetMapping("/")
    public String homePage(Model model,
                           @RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "20") int size,
                           @RequestParam(value = "sort", defaultValue = "id") String sortOrder,
                           @RequestParam(value = "dir", defaultValue = "asc") String dir) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, 100));
        Sort sorting = dir.equals("asc") ? Sort.by(sortOrder).ascending()
                : Sort.by(sortOrder).descending();

        Pageable pageable = PageRequest.of(safePage, safeSize, sorting);
        Page<BugDTO> paged = bugformService.getPagedBugs(pageable);
        model.addAttribute("bugsReported", bugformService.getCount());
        model.addAttribute("highPriorityBugs", bugformService.getBugsByPriority(Priority.HIGH).size());
        model.addAttribute("bugs", paged);
        model.addAttribute("totalPages", paged.getTotalPages());
        model.addAttribute("currentPage", safePage);
        model.addAttribute("pageSize", safeSize);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("currentDir", dir);
        model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");
        return "homescreen";
    }

    /**
     * Handles requests to view detailed information about a specific bug report.
     *
     * @param id    the unique identifier of the bug report to retrieve
     * @param model the {@code Model} object used to add attributes for rendering in the view
     * @return the name of the view template to display bug details
     */
    @GetMapping("/bugdetails")
    public String viewBugDetails(@RequestParam Long id, Model model) {
        BugDTO bug = bugformService.getReport(id);
        model.addAttribute("bugdetail", bug);
        return "details";
    }

    /**
     * Handles requests to display the bug edit form.
     * Retrieves the bug details associated with the given ID, converts them into an
     * UpdateBugDTO object, and populates the model with this object to enable
     * form pre-filling. Returns the name of the view to display the edit form.
     *
     * @param id the unique identifier of the bug to be edited
     * @param model the model object used to pass data to the view
     * @return the name of the view to render the bug edit form
     */
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

    /**
     * Handles the submission of the bug edit form.
     * This method updates an existing bug report if the form data is valid.
     * In case of validation errors or update failures, it redirects back to the edit view.
     *
     * @param id The ID of the bug report being edited.
     * @param updateForm The data transfer object containing the updated bug information.
     * @param bindingResult The result of binding the form data to the UpdateBugDTO.
     * @param model The model used to supply attributes to the view.
     * @return A string indicating the name of the view to render. If successful, redirects to the bug details page.
     *         Otherwise, returns the edit view.
     */
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

    /**
     * Handles the confirmation page for deleting a bug report.
     *
     * @param id    the unique identifier of the bug report to be deleted
     * @param model the model to add attributes for rendering the view
     * @return the name of the view to display the confirmation page
     */
    @GetMapping("/bugdetails/delete/confirm")
    public String confirmDelete(@RequestParam Long id, Model model) {
        BugDTO bug = bugformService.getReport(id);
        model.addAttribute("bugdetail", bug);

        return "confirm_delete";
    }

    /**
     * Deletes a bug report identified by its unique ID and redirects to the homepage.
     *
     * @param id The unique identifier of the bug report to be deleted.
     * @return A redirection string to the homepage upon successful deletion of the bug report.
     */
    @PostMapping("/bugdetails/delete")
    public String deleteReport(@RequestParam Long id) {
        bugformService.deleteReport(id);
        return "redirect:/";
    }

    /**
     * Handles search requests by filtering bug reports based on the input query
     * and paginating the results. If the input query is empty or not provided,
     * the user is redirected to the homepage.
     *
     * @param input Optional search query to filter bug reports by title or description.
     * @param page  The page index for pagination, defaults to 0 if not provided.
     * @param size  The size of items per page for pagination, defaults to 20 if not provided.
     * @param model The Model object to store attributes for rendering the view.
     * @return The name of the view template to be rendered. Redirects to the homepage
     *         if the input query is empty or blank, otherwise returns "homescreen".
     */
    @GetMapping("/search")
    public String getSearchResult(@RequestParam(required = false) String input,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "20") int size,
                                  @RequestParam(value = "sort", defaultValue = "id") String sortOrder,
                                  Model model) {
        if (input == null || input.isBlank()) {
            return "redirect:/";
        } else {
            int safePage = Math.max(page, 0);
            int safeSize = Math.max(1, Math.min(size, 100));
            Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(sortOrder));
            Page<BugDTO> paged = bugformService.getSearchByTitleOrDescription(input, pageable);
            model.addAttribute("bugs", paged);
            model.addAttribute("highPriorityBugs", bugformService.getBugsByPriority(Priority.HIGH).size());
            model.addAttribute("bugsReported", bugformService.getCount());
            model.addAttribute("totalPages", paged.getTotalPages());
            model.addAttribute("pageSize", safeSize);
            model.addAttribute("currentPage", safePage);
            model.addAttribute("search", input);
            model.addAttribute("sortOrder", sortOrder);
        }
        return "homescreen";
    }


}
