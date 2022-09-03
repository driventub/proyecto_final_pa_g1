package ec.edu.uce.service;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import ec.edu.uce.modelo.Vehiculo;

@SpringBootTest
@Rollback(true)
@Transactional
public class GestorClienteServiceImplTest {

    @Autowired
	private IVehiculoService iVehiculoService;
    
    @Test
    void testBuscarVehiculosDisponibles() {
        List<Vehiculo> lista = this.iVehiculoService.buscarMarcaModelo("Toyota", "Prius");
		lista.forEach(vehi -> System.out.println(vehi));
    }
}
