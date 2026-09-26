package com.example.Veiculo.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

	 @Value("${gemini.api.key}")
	private String apiKey;
	
	 
	 @Autowired
	 private RestTemplate restTemplate;

	 
	 public String gerarDescricao(String marca, String modelo, int ano) {
		 try {
			 // fornecido pela gemine
			 String url = "https://generativelanguage.googleapis.com" +
		                "/v1beta/models/gemini-3.8-flash:generateContent?key=" + apiKey;

		            String pergunta = "Em 2 frases curtas em português, " +
		                "descreva o veículo: " + marca + " " + modelo + " " + ano;

		            // Monta o JSON que a API do Gemini espera
		            Map<String, Object> body = Map.of(
		                "contents", List.of(
		                    Map.of("parts", List.of(
		                        Map.of("text", pergunta)
		                    ))
		                )
		            );

		            // Faz POST para o Gemini e converte a resposta em Map
		            Map resposta = restTemplate.postForObject(url, body, Map.class);

		            // Navega pelo JSON de resposta para extrair o texto gerado
		            List candidates = (List) resposta.get("candidates");
		            Map candidate = (Map) candidates.get(0);
		            Map content = (Map) candidate.get("content");
		            List parts = (List) content.get("parts");
		            Map part = (Map) parts.get(0);

		            return (String) part.get("text");
			 
			
		} catch (Exception e) {
			System.out.println("GEMINI ERRO: " + e.getMessage());
			return "Descrição indisponível no momento.";
		}
	 }
}
