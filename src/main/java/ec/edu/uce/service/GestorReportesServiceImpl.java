package ec.edu.uce.service;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.uce.modelo.ReporteClienteVIPTO;
import ec.edu.uce.modelo.ReporteReservas;
import ec.edu.uce.modelo.ReporteVehiculosVIPTO;

@Service
public class GestorReportesServiceImpl implements IGestorReportesService {

	@Autowired
	private IReservaService iReservaService;

	@Autowired
	private IClienteService iClienteService;

	@Override
	@Transactional(value = TxType.NOT_SUPPORTED)
	public List<ReporteReservas> reporteReservas(LocalDateTime fechaInicio, LocalDateTime fechaFinal) {
		return this.iReservaService.reporteReservas(fechaInicio, fechaFinal);
	}

	@Override
	@Transactional(value = TxType.NOT_SUPPORTED)
	public List<ReporteClienteVIPTO> reporteClientesVIP() {

		CompletableFuture<List<ReporteClienteVIPTO>> valor1 = this.iClienteService
				.clientesVip(this.iReservaService.todasReservas(), "total");
		CompletableFuture<List<ReporteClienteVIPTO>> valor2 = this.iClienteService
				.clientesVip(this.iReservaService.todasReservas(), "iva");
		CompletableFuture.allOf(valor1, valor2).join();

		try {
			int cont = 0;
			for (ReporteClienteVIPTO lista : valor1.get()) {
				lista.setValorTotalAPagar(lista.getValorTotalAPagar().setScale(2, RoundingMode.HALF_UP));
				lista.setValorIVA(valor2.get().get(cont).getValorIVA().setScale(2, RoundingMode.HALF_UP));
				cont = cont + 1;
			}
			return valor1.get().parallelStream()
					.sorted((o1, o2) -> o2.getValorTotalAPagar().compareTo(o1.getValorTotalAPagar()))
					.collect(Collectors.toList());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@Transactional(value = TxType.NOT_SUPPORTED)
	public List<ReporteVehiculosVIPTO> reporteVehiculodVIP(String mes, String anio) {
		return this.iReservaService.buscarMesAnio(mes, anio);
	}

}
