package com.example.Veiculo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Veiculo.entidade.Veiculo;
import com.example.Veiculo.repository.VeiculoRepository;

@Service
public class VeiculoService {
	
	@Autowired
	private VeiculoRepository repository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private GeminiService geminiService;
	
	
	public Veiculo criar (Veiculo veiculo) {
		if (veiculo.getCep() != null && !veiculo.getCep().isEmpty()) {
			buscarEnderecoPorCep(veiculo);
		}
		
		return repository.save(veiculo);
	}
	
	public List<Veiculo> listar(){
		return repository.findAll();
	}
	
	public Veiculo buscar(Long id) {
		Veiculo veiculo = repository.findById(id).orElse(null);
		if (veiculo != null) {
			String descricao = geminiService.gerarDescricao(veiculo.getMarca(), veiculo.getModelo(), veiculo.getAno());
			veiculo.setDescricaoIA(descricao);
		}
		
		return veiculo;
	}
	
	public Veiculo atualizar(Long id, Veiculo novo) {
		novo.setId(id);
		if (novo.getCep() != null && !novo.getCep().isEmpty()) {
			buscarEnderecoPorCep(novo);
		}
		
		return repository.save(novo);
	}
	
	public void deletar (Long id) {
		repository.deleteById(id);
	}
	
	public void buscarEnderecoPorCep(Veiculo veiculo) {
		try {

			String cepLimpo = veiculo.getCep().replaceAll("[^0-9]", "");

			String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";

			ViaCepResposta resposta = restTemplate.getForObject(url, ViaCepResposta.class);

			if (resposta != null) {
				veiculo.setLogradouro(resposta.getLogradouro());
				veiculo.setBairro(resposta.getBairro());
				veiculo.setCidade(resposta.getLocalidade());
				veiculo.setEstado(resposta.getUf());
			}

		} catch (Exception e) {
			System.out.println("Cep não encontrado");
		}
	}
	
	
	
	
	

}
