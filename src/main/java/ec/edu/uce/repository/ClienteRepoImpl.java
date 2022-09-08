package ec.edu.uce.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import ec.edu.uce.modelo.Cliente;

@Transactional
@Repository
public class ClienteRepoImpl implements IClienteRepo {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public void insertar(Cliente cliente) {
		this.entityManager.persist(cliente);
	}

	@Override
	public void actualizar(Cliente cliente) {
		this.entityManager.merge(cliente);
	}

	@Override
	public Cliente buscar(Integer id) {
		return this.entityManager.find(Cliente.class, id);
	}

	@Override
	public void borrar(Integer id) {
		this.entityManager.remove(this.buscar(id));
	}

	@Override
	public Cliente buscarCedula(String cedula) {
		TypedQuery<Cliente> myQuery = this.entityManager.createQuery("SELECT c FROM Cliente c WHERE c.cedula=:valor",
				Cliente.class);
		myQuery.setParameter("valor", cedula);
		return myQuery.getSingleResult();
	}

	@Override
	public List<Cliente> listarClientes() {
		
		TypedQuery<Cliente> myQ = this.entityManager.createQuery("Select c from Cliente c ", Cliente.class);
		return myQ.getResultList();
	}

	@Override
	public List<Cliente> buscarPorApellido(String apellido) {
		// TODO Auto-generated method stub
		TypedQuery<Cliente> myQ = this.entityManager.createQuery("Select c from Cliente c where c.apellido=:apellido", Cliente.class);
		return myQ.setParameter("apellido", apellido).getResultList();
	}

	@Override
	public boolean verificarReserva(Integer id) {
		Cliente cliente = this.buscar(id);
		if (cliente.getReservaActiva() > 0) {
			return false;	
		}else{
			return true;
		}
		
	}

}
