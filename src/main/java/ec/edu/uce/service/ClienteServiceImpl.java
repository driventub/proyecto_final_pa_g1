package ec.edu.uce.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ec.edu.uce.modelo.Cliente;
import ec.edu.uce.modelo.ReporteClienteVIPTO;
import ec.edu.uce.modelo.Reserva;
import ec.edu.uce.repository.IClienteRepo;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteRepo iClienteRepo;

	@Autowired
	private IReservaService iReservaService;

	@Override
	@Transactional
	public void insertar(Cliente cliente) {
		this.iClienteRepo.insertar(cliente);
	}

	@Override
	@Transactional
	public void actualizar(Cliente cliente) {
		this.iClienteRepo.actualizar(cliente);
	}

	@Override
	public Cliente buscar(Integer id) {
		return this.iClienteRepo.buscar(id);
	}

	@Override
	@Transactional
	public void borrar(Integer id) {
		this.iClienteRepo.borrar(id);
	}

	@Override
	public Cliente buscarCedula(String cedula) {
		return this.iClienteRepo.buscarCedula(cedula);
	}

	@Override
	@Async
	public CompletableFuture<List<ReporteClienteVIPTO>> clientesVip(List<Reserva> listaVuelo, String tipo) {

		List<ReporteClienteVIPTO> lista = new ArrayList<>();
		List<ReporteClienteVIPTO> lista2 = new ArrayList<>();
		if (tipo.compareTo("total") == 0) {

			Map<Cliente, DoubleSummaryStatistics> employeesByCity = listaVuelo.parallelStream()
					.collect(Collectors.groupingBy(Reserva::getCliente, Collectors
							.summarizingDouble(value -> value.getPagos().getValorTotalAPagar().doubleValue())));

			employeesByCity.forEach((key, value) -> {
				lista.add(new ReporteClienteVIPTO(key, new BigDecimal(0), new BigDecimal(value.getSum())));
			});
			return CompletableFuture.completedFuture(lista);
		}
		if (tipo.compareTo("iva") == 0) {

			Map<Cliente, DoubleSummaryStatistics> employeesByCity = listaVuelo.parallelStream()
					.collect(Collectors.groupingBy(Reserva::getCliente,
							Collectors.summarizingDouble(value -> value.getPagos().getValorIVA().doubleValue())));

			employeesByCity.forEach((key, value) -> {
				lista2.add(new ReporteClienteVIPTO(key, new BigDecimal(value.getSum()), new BigDecimal(0)));
			});
			return CompletableFuture.completedFuture(lista2);
		}

		return CompletableFuture.completedFuture(null);
	}

}
