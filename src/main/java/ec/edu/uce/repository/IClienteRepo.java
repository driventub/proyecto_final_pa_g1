package ec.edu.uce.repository;

import ec.edu.uce.modelo.Cliente;

public interface IClienteRepo {

	void insertar(Cliente cliente);

	void actualizar(Cliente cliente);

	Cliente buscar(Integer id);

	Cliente buscarCedula(String cedula);

	void borrar(Integer id);
}
