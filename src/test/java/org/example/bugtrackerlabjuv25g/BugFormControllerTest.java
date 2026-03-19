package org.example.bugtrackerlabjuv25g;

import org.example.bugtrackerlabjuv25g.exception.ResourceNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
                        .param("description", "Too short title and missing fields"))
                .andExpect(status().isOk())
                .andExpect(view().name("create_view"));
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
    @DisplayName("GET Homepage is correct")
    void homePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bugs"))
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
                .andExpect(view().name("edit_view"))
                .andExpect(model().attributeHasFieldErrors("bugForm", "title", "description"));

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
}

