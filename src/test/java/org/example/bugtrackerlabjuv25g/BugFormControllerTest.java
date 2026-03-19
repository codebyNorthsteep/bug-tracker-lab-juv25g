package org.example.bugtrackerlabjuv25g;

import org.example.bugtrackerlabjuv25g.exception.ResourceNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BugFormController.class)
class BugFormControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BugFormService bugFormService;


    @Test
    @DisplayName("GET Add bug is presented with correct html, model and status ok")
    void showBugForm() throws Exception {
        mockMvc.perform(get("/reports/add"))
                //Check httpresponse status
                .andExpect(status().isOk())
                //Check correct html file is presented
                .andExpect(view().name("create_view"))
                //Check model attribute is present
                .andExpect(model().attributeExists("bugForm"));
    }

    @Test
    @DisplayName("POST Invalid form data should return to create_view html")
    void postBugFormInvalidForm() throws Exception {
        mockMvc.perform(post("/reports/add")
                        .param("title", "s")
                        .param("des", "Too short title and missing fields"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_view"))
                .andExpect(model().attributeHasFieldErrors("bugForm", "title", "description"));
    }

    @Test
    @DisplayName("POST Form with duplicate title should stay in edit_view with specific error")
    void postBugFormDuplicateTitle() throws Exception {
        // 1. Mocka så att servicen kastar felet när vi försöker spara
        Mockito.doThrow(new IllegalArgumentException("Title exists"))
                .when(bugFormService).saveReport(Mockito.any());

        // 2. Skicka in GILTIG data (så vi passerar if-satsen med bindingResult.hasErrors)
        mockMvc.perform(post("/reports/add")
                        .param("title", "Existing Title")
                        .param("description", "Valid length description")
                        .param("priority", "LOW")
                        .param("development", "BACKEND"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_view"))
                .andExpect(model().attributeHasFieldErrors("bugForm", "title"));
    }

    @Test
    @DisplayName("POST Valid form data should expect redirection to '/'")
    void postBugForm() throws Exception {
        //Because we use Model attribute we send data as a form and inputs field with .param
        mockMvc.perform(post("/reports/add")
                        .param("title", "testTitle")
                        .param("description", "info")
                        .param("priority", "LOW")
                        .param("development", "BACKEND"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }


    @Test
    @DisplayName("GET Homepage is correct with paged results")
    void homePage() throws Exception {

        var bug = new BugDTO(1L, "test", "desc", "date", Priority.LOW, Development.BACKEND);
        List<BugDTO> bugList = List.of(bug);
        Page<BugDTO> mockPage = new PageImpl<>(bugList, PageRequest.of(0, 20), 1);

        Mockito.when(bugFormService.getPagedBugs(Mockito.any())).thenReturn(mockPage);
        //Others are used in the view to show total bugs and high priority bugs count
        Mockito.when(bugFormService.getCount()).thenReturn(1L);
        Mockito.when(bugFormService.getBugsByPriority(Priority.HIGH)).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bugs", "bugsReported", "highPriorityBugs", "totalPages", "currentPage", "pageSize"))
                .andExpect(view().name("homescreen"));
    }

    @Test
    @DisplayName("GET Bug details with non existent bug should throw ResourceNotFound")
    void viewBugDetailsNotFound() throws Exception {
        Mockito.when(bugFormService.getReport(1L)).thenThrow(new ResourceNotFound("Bug with id 1 not found"));
        mockMvc.perform(get("/bugdetails")
                        .param("id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    @DisplayName("GET Bug details returns with correct view, attribute and status")
    void viewBugDetails() throws Exception {
        var bug = new BugDTO(1L, "test", "desc", "date", Priority.LOW, Development.BACKEND);
        Mockito.when(bugFormService.getReport(1L)).thenReturn(bug);
        mockMvc.perform(get("/bugdetails")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bugdetail"))
                .andExpect(view().name("details"));

    }

    @Test
    @DisplayName("GET Edit form with non existent bug should throw ResourceNotFound")
    void showEditFormNotFound() throws Exception {
        Mockito.when(bugFormService.getReport(1L)).thenThrow(new ResourceNotFound("Bug with id 1 not found"));
        mockMvc.perform(get("/bugdetails/edit")
                        .param("id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    @DisplayName("GET Edit form returns with correct view with data to edit, attribute and status")
    void showEditForm() throws Exception {
        var bug = new BugDTO(1L, "test", "desc", "date", Priority.LOW, Development.BACKEND);
        Mockito.when(bugFormService.getReport(1L)).thenReturn(bug);
        mockMvc.perform(get("/bugdetails/edit")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bugForm"))
                .andExpect(view().name("edit_view"));
    }

    @Test
    @DisplayName("POST Edit form with invalid data should stay in edit_view with error")
    void postEditFormInvalidForm() throws Exception {
        mockMvc.perform(post("/bugdetails/edit/1")
                        .param("title", "s")
                        .param("des", "Too short title and missing fields"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_view"));
    }

    @Test
    @DisplayName("POST Edit form with duplicate title should stay in edit_view with specific error")
    void postEditFormDuplicateTitle() throws Exception {

        Mockito.doThrow(new IllegalArgumentException("Title already exists"))
                .when(bugFormService).updateReport(Mockito.anyLong(), Mockito.any());

        mockMvc.perform(post("/bugdetails/edit/1")
                        .param("id", "1")
                        .param("title", "Duplicate")
                        .param("description", "Valid description")
                        .param("priority", "LOW")
                        .param("development", "BACKEND"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit_view"))
                .andExpect(model().attributeHasFieldErrors("bugForm", "title"));
    }

    @Test
    @DisplayName("POST Edit form with valid data should redirect to bug details")
    void postEditForm() throws Exception {
        mockMvc.perform(post("/bugdetails/edit/1")
                        .param("id", "1")
                        .param("title", "Updated Title")
                        .param("description", "Updated description")
                        .param("priority", "MEDIUM")
                        .param("development", "FRONTEND"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bugdetails?id=1"));
    }

    @Test
    @DisplayName("GET Confirm delete shows correct view and model")
    void confirmDelete() throws Exception {
        var bug = new BugDTO(1L, "Delete Me", "desc", "date", Priority.HIGH, Development.BACKEND);
        Mockito.when(bugFormService.getReport(1L)).thenReturn(bug);

        mockMvc.perform(get("/bugdetails/delete/confirm").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bugdetail"))
                .andExpect(view().name("confirm_delete"));
    }

    @Test
    @DisplayName("POST Delete bug should redirect to home")
    void deleteReport() throws Exception {
        mockMvc.perform(post("/bugdetails/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        Mockito.verify(bugFormService).deleteReport(1L);
    }

    @Test
    @DisplayName("GET Search with blank space should redirect to home")
    void getSearchResultBlankInput() throws Exception {
        mockMvc.perform(get("/search")
                        .param("input", "   "))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @DisplayName("GET Search with null input should redirect to home")
    void getSearchResultNullInput() throws Exception {
        mockMvc.perform(get("/search"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    @DisplayName("GET Search with valid input should show homescreen with paged results")
    void getSearchResultValidInput() throws Exception {

        String searchWord = "test";
        var bug = new BugDTO(1L, "test bug", "desc", "date", Priority.HIGH, Development.BACKEND);
        List<BugDTO> searchResults = List.of(bug);

        Page<BugDTO> mockPage = new PageImpl<>(searchResults, PageRequest.of(0, 20), 1);

        Mockito.when(bugFormService.getSearchByTitleOrDescription(Mockito.eq(searchWord), Mockito.any())).thenReturn(mockPage);

        mockMvc.perform(get("/search")
                        .param("input", searchWord))
                .andExpect(status().isOk())
                .andExpect(view().name("homescreen"))
                .andExpect(model().attributeExists("bugs", "highPriorityBugs", "bugsReported", "totalPages", "pageSize", "currentPage", "search"))
                .andExpect(model().attribute("search", searchWord));
    }
}

