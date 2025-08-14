package org.jdelira.springbootapp;

import org.jdelira.springbootapp.DAO.BancoRepository;
import org.jdelira.springbootapp.DAO.CuentaRepository;
import org.jdelira.springbootapp.models.Banco;
import org.jdelira.springbootapp.models.Cuenta;
import org.jdelira.springbootapp.services.CuentaService;
import org.jdelira.springbootapp.services.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

	CuentaRepository cuentaRepository;
	BancoRepository bancoRepository;

	CuentaService service;

	@BeforeEach
	void setUp() {

		cuentaRepository = mock(CuentaRepository.class);
		bancoRepository = mock(BancoRepository.class);
		service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
	}

	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.CUENTA_001);
		when(cuentaRepository.findById(2L)).thenReturn(Datos.CUENTA_002);
		when(bancoRepository.findById(1L)).thenReturn(Datos.BANCO);

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);
		assertEquals("1000", saldoOrigen.toPlainString());
		assertEquals("2000", saldoDestino.toPlainString());

		service.transferir(1L, 2L, new BigDecimal("100"), 1l);

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);

		assertEquals("900", saldoOrigen.toPlainString());
		assertEquals("2100", saldoDestino.toPlainString());

		int total = service.revisarTotalTransferencias(1L);
		assertEquals(1, total);
		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		verify(cuentaRepository, times(2)).update(any(Cuenta.class));

		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).update(any(Banco.class));
	}

}
