package com.example.Veiculo.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Veiculo.entidade.Veiculo;
import com.example.Veiculo.service.VeiculoService;

@WebMvcTest(VeiculoController.class)
public class VeiculoControllerTest {

	 @Autowired private MockMvc mockMvc;
	    @MockitoBean private VeiculoService service;

	    @Test void deveListarVeiculos() throws Exception {
	        Veiculo v = new Veiculo(); v.setId(1L); v.setMarca("Toyota");
	        when(service.listar()).thenReturn(Arrays.asList(v));
	        mockMvc.perform(get("/veiculos"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$[0].marca").value("Toyota"));
	    }

	    @Test void deveBuscarVeiculoPorId() throws Exception {
	        Veiculo v = new Veiculo(); v.setId(1L); v.setDescricaoIA("IA desc");
	        when(service.buscar(1L)).thenReturn(v);
	        mockMvc.perform(get("/veiculos/1"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.descricaoIA").value("IA desc"));
	    }

	    @Test void deveCriarVeiculo() throws Exception {
	        Veiculo v = new Veiculo(); v.setId(1L); v.setMarca("Honda");
	        when(service.criar(any(Veiculo.class))).thenReturn(v);
	        mockMvc.perform(post("/veiculos").contentType(MediaType.APPLICATION_JSON)
	            .content("{\"marca\":\"Honda\",\"modelo\":\"Civic\",\"ano\":2024}"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.marca").value("Honda"));
	    }

	    @Test void deveAtualizarVeiculo() throws Exception {
	        Veiculo v = new Veiculo(); v.setMarca("Fiat");
	        when(service.atualizar(eq(1L), any(Veiculo.class))).thenReturn(v);
	        mockMvc.perform(put("/veiculos/1").contentType(MediaType.APPLICATION_JSON)
	            .content("{\"marca\":\"Fiat\",\"modelo\":\"Uno\",\"ano\":2022}"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.marca").value("Fiat"));
	    }

	    @Test void deveDeletarVeiculo() throws Exception {
	        doNothing().when(service).deletar(anyLong());
	        mockMvc.perform(delete("/veiculos/1"))
	            .andExpect(status().isOk())
	            .andExpect(content().string("Delete feito com Sucesso"));
	    }
}
