package ec.edu.uce.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.edu.uce.modelo.Vehiculo;
import ec.edu.uce.repository.IVehiculoRepo;

@Service
public class VehiculoServicesImpl implements IVehiculoService {

	@Autowired
	private IVehiculoRepo iVehiculoRepo;

	@Override
	@Transactional
	public void insertar(Vehiculo vehiculo) {
		this.iVehiculoRepo.insertar(vehiculo);
	}

	@Override
	@Transactional
	public void actualizar(Vehiculo vehiculo) {
		this.iVehiculoRepo.actualizar(vehiculo);
	}

	@Override
	public Vehiculo buscar(Integer id) {
		return this.iVehiculoRepo.buscar(id);
	}

	@Override
	@Transactional
	public void borrar(Integer id) {
		this.iVehiculoRepo.borrar(id);
	}

	@Override
	public List<Vehiculo> buscarMarcaModelo(String marca, String modelo) {
		return this.iVehiculoRepo.buscarMarcaModelo(marca, modelo);
	}

	@Override
	public Vehiculo buscarPorPlaca(String placa) {
		return this.iVehiculoRepo.buscarPorPlaca(placa);
	}

}
