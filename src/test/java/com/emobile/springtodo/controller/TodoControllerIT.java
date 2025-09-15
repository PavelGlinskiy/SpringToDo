package com.emobile.springtodo.controller;

import com.emobile.springtodo.BasePostgresTest;
import com.emobile.springtodo.DTO.TodoRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoControllerIT extends BasePostgresTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /todo/find/all возвращает список задач (JSONAssert)")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void getAll_returnsList() throws Exception {
        String content = mockMvc.perform(get("/todo/find/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("[ {id:100,title:'Task A',completed:false}, {id:101,title:'Task B',completed:true}, {id:102,title:'Task C',completed:false} ]",
                content, false);
    }

    @Test
    @DisplayName("GET /todo/100 возвращает один объект (JSONAssert)")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void getById_returnsOne() throws Exception {
        String content = mockMvc.perform(get("/todo/100"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{id:100,title:'Task A',completed:false}", content, false);
    }

    @Test
    @DisplayName("POST /todo/add создаёт задачу")
    @Sql({"classpath:db/testdata/clear.sql"})
    void create_creates() throws Exception {
        TodoRequestDTO req = new TodoRequestDTO("New", false);
        String body = objectMapper.writeValueAsString(req);

        String content = mockMvc.perform(post("/todo/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{title:'New',completed:false}", content, false);
    }

    @Test
    @DisplayName("PUT /todo/update/{id} обновляет задачу")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void update_updates() throws Exception {
        TodoRequestDTO req = new TodoRequestDTO("Up", true);
        String body = objectMapper.writeValueAsString(req);

        String content = mockMvc.perform(put("/todo/update/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals("{id:100,title:'Up',completed:true}", content, false);
    }

    @Test
    @DisplayName("DELETE /todo/delete/{id} удаляет задачу")
    @Sql({"classpath:db/testdata/clear.sql", "classpath:db/testdata/todos.sql"})
    void delete_deletes() throws Exception {
        mockMvc.perform(delete("/todo/delete/100"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/todo/100"))
                .andExpect(status().is4xxClientError());
    }
}




