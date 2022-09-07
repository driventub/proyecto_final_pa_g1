package ec.edu.uce.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import ec.edu.uce.modelo.Cliente;
import ec.edu.uce.modelo.ReporteClienteVIPTO;
import ec.edu.uce.modelo.Reserva;

public interface IClienteService {

	void insertar(Cliente cliente);

	void actualizar(Cliente cliente);

	Cliente buscar(Integer id);

	Cliente buscarCedula(String cedula);

	void borrar(Integer id);

	CompletableFuture<List<ReporteClienteVIPTO>> clientesVip(List<Reserva> listaVuelo, String tipo);
}
