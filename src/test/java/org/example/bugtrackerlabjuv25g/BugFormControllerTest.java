package org.example.bugtrackerlabjuv25g;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BugFormControllerTest {

    MockMvc mockMvc;
    @Mock
    BugFormService bugFormService;

    @BeforeEach
    void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(new BugFormController(bugFormService)).build();
    }


    @Test
    @DisplayName("GET Add bug is presented with correct html, model and status ok")
    void showBugForm() throws Exception{
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
    void postBugFormInvalidForm() throws Exception{
        mockMvc.perform(post("/reports/add")
                .param("title", "s")
                .param("description", "Too short title and missing fields"))
                .andExpect(view().name("create_view"));
    }

    @Test
    @DisplayName("POST Valid form data should expect redirection to '/'")
    void postBugForm() throws Exception{
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
    void homePage() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bugs"))
                .andExpect(view().name("homescreen"));
    }

    @Test
    @DisplayName("GET Bug details with non existent bug should redirect")
    void viewBugDetailsRedirect() throws Exception {
        mockMvc.perform(get("/bugdetails")
                //@RequestParam data
                .param("id", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("GET Bug details returns with correct view, attribute and status")
    void viewBugDetails() throws Exception{
        var bug = Optional.of(new BugDTO(1L,"test","desc", "date", Priority.LOW, Development.BACKEND));
        Mockito.when(bugFormService.getReport(1)).thenReturn(bug);
        mockMvc.perform(get("/bugdetails")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bugdetail"))
                .andExpect(view().name("details"));

    }
}
