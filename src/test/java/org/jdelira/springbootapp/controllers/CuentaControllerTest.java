package org.jdelira.springbootapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jdelira.springbootapp.models.TransaccionDto;
import org.jdelira.springbootapp.services.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.jdelira.springbootapp.Datos.crearCuenta001;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService cuentaService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testDetalle() throws Exception {
        //Given
        when(cuentaService.findById(1L)).thenReturn(crearCuenta001().orElseThrow());

        //When
        mvc.perform(MockMvcRequestBuilders.get("/api/cuentas/1").contentType(APPLICATION_JSON))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Jorge"))
                .andExpect(jsonPath("$.saldo").value("1000"));

        verify(cuentaService).findById(1L);
    }

    @Test
    void testTransferir() throws Exception{

        //Given
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaOrigenId(1L);
        dto.setCuentaDestinoId(2L);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);

        System.out.println(objectMapper.writeValueAsString(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("mensaje", "Transferencia realizada con éxito!");
        response.put("transacción", dto);

        System.out.println(objectMapper.writeValueAsString(response));

        //When
        mvc.perform(post("/api/cuentas/transferir")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))

                //Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con éxito!"))
                .andExpect(jsonPath("$.transacción.cuentaOrigenId").value(dto.getCuentaOrigenId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

    }
}