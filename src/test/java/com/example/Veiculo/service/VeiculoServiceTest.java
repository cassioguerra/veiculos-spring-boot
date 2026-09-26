package com.example.Veiculo.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.example.Veiculo.entidade.Veiculo;
import com.example.Veiculo.repository.VeiculoRepository;

@ExtendWith(MockitoExtension.class)
public class VeiculoServiceTest {

	 @Mock private VeiculoRepository repository;
	 @Mock private GeminiService geminiService;
	 @Mock private RestTemplate restTemplate;
	 @InjectMocks private VeiculoService service;

	    private Veiculo veiculo;

	    @BeforeEach
	    void setUp() {
	        veiculo = new Veiculo();
	        veiculo.setId(1L); veiculo.setMarca("Toyota");
	        veiculo.setModelo("Corolla"); veiculo.setAno(2023);
	    }

	    @Test void deveCriarVeiculoSemCep() {
	        when(repository.save(veiculo)).thenReturn(veiculo);
	        Veiculo r = service.criar(veiculo);
	        assertNotNull(r);
	        assertEquals("Toyota", r.getMarca());
	        verify(repository, times(1)).save(veiculo);
	    }

	    @Test void deveCriarVeiculoComCepValido() {
	        veiculo.setCep("01310100");
	        when(repository.save(any(Veiculo.class))).thenReturn(veiculo);
	        assertNotNull(service.criar(veiculo));
	    }

	    @Test void deveListarTodosVeiculos() {
	        Veiculo v2 = new Veiculo(); v2.setMarca("Honda");
	        when(repository.findAll()).thenReturn(Arrays.asList(veiculo, v2));
	        assertEquals(2, service.listar().size());
	    }

	    @Test void deveBuscarVeiculoPorId() {
	        when(repository.findById(1L)).thenReturn(Optional.of(veiculo));
	        when(geminiService.gerarDescricao(anyString(), anyString(), anyInt()))
	            .thenReturn("Descrição gerada pela IA");
	        Veiculo r = service.buscar(1L);
	        assertEquals("Descrição gerada pela IA", r.getDescricaoIA());
	    }

	    @Test void deveRetornarNullQuandoIdNaoExiste() {
	        when(repository.findById(99L)).thenReturn(Optional.empty());
	        assertNull(service.buscar(99L));
	    }

	    @Test void deveAtualizarVeiculo() {
	        Veiculo novo = new Veiculo(); novo.setMarca("Honda");
	        when(repository.save(any(Veiculo.class))).thenReturn(novo);
	        assertEquals("Honda", service.atualizar(1L, novo).getMarca());
	    }

	    @Test void deveDeletarVeiculo() {
	        doNothing().when(repository).deleteById(1L);
	        service.deletar(1L);
	        verify(repository, times(1)).deleteById(1L);
	    }

	    @Test void deveListarVazioQuandoNaoHaVeiculos() {
	        when(repository.findAll()).thenReturn(Arrays.asList());
	        assertTrue(service.listar().isEmpty());
	    }

}
