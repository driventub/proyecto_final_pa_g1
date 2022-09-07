package ec.edu.uce.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import ec.edu.uce.modelo.Cliente;
import ec.edu.uce.modelo.ReporteClienteVIPTO;
import ec.edu.uce.modelo.Reserva;

public interface IClienteService {

	void insertarCliente(Cliente cliente);

	void actualizarCliente(Cliente cliente);

	Cliente buscarClientePorId(Integer id);

	Cliente buscarClientePorCedula(String cedula);

	void borrarClientePorId(Integer id);
	
	List<Cliente> listarClientes();
	
	List<Cliente> listarClientesPorApellido(String apellido);

	CompletableFuture<List<ReporteClienteVIPTO>> clientesVip(List<Reserva> listaVuelo, String tipo);
}
