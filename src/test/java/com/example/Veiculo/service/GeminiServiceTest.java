package com.example.Veiculo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)

public class GeminiServiceTest {

	  @Mock private RestTemplate restTemplate;
	    @InjectMocks private GeminiService geminiService;

	    @Test void deveRetornarFallbackQuandoApiKeyEstaVazia() {
	        ReflectionTestUtils.setField(geminiService, "apiKey", "");
	        assertEquals("Descrição indisponível no momento.",
	            geminiService.gerarDescricao("Toyota", "Corolla", 2023));
	    }

	    @Test void deveRetornarFallbackQuandoRestTemplateLancaExcecao() {
	        ReflectionTestUtils.setField(geminiService, "apiKey", "chave-invalida");
	        when(restTemplate.postForObject(anyString(), any(), eq(java.util.Map.class)))
	            .thenThrow(new RuntimeException("Conexão recusada"));
	        assertEquals("Descrição indisponível no momento.",
	            geminiService.gerarDescricao("Honda", "Civic", 2024));
	    }

	    @Test void deveRetornarFallbackComDadosDoVeiculo() {
	        ReflectionTestUtils.setField(geminiService, "apiKey", "");
	        assertEquals("Descrição indisponível no momento.",
	            geminiService.gerarDescricao("Fiat", "Uno", 2020));
	    }
}
