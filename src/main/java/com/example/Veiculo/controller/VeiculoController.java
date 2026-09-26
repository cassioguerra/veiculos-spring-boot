package com.example.Veiculo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Veiculo.entidade.Veiculo;
import com.example.Veiculo.service.VeiculoService;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
	
	@Autowired
	private VeiculoService service;
	
	@GetMapping
	public List<Veiculo> listar (){
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public Veiculo buscar (@PathVariable Long id) {
		return service.buscar(id);
	}
	
	@PostMapping
	public Veiculo criar (@RequestBody Veiculo veiculo) {
		return service.criar(veiculo);
	}
	
	
	@PutMapping("/{id}")
	public Veiculo atualizar(@PathVariable Long id, @RequestBody Veiculo novo) {
		return service.atualizar(id, novo);
	}
	
	@DeleteMapping("/{id}")
	public String delete (@PathVariable Long id) {
	  service.deletar(id);
	  return "Delete feito com Sucesso";
	}
	
	
	
	
	
	

}
