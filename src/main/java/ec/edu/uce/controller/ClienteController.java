package ec.edu.uce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ec.edu.uce.modelo.Cliente;
import ec.edu.uce.modelo.Reserva;
import ec.edu.uce.modelo.Vehiculo;
import ec.edu.uce.service.IGestorClienteService;
import ec.edu.uce.service.IVehiculoService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    

    @Autowired
    private IGestorClienteService cliente;
    @Autowired
    private IVehiculoService vehiculoService;
    

    // c.a
    @GetMapping("/vehiculoBuscar")
	public String obtenerVehiculo(Vehiculo vehiculo) {
		return "v_buscar_marca";

	}

    @GetMapping("/buscar/marcaModelo")
	public String buscarVehiculo(Vehiculo vehiculo, BindingResult result, Model model,
			RedirectAttributes redirectAttrs) {

		List<Vehiculo> listaVehiculo = this.cliente.buscarVehiculosDisponibles( vehiculo.getMarca(),vehiculo.getModelo());
        model.addAttribute("listaVehiculo", listaVehiculo);
		return "v_m_marca";
	}

    
    //c.b
    @GetMapping("/reservaBuscar")
    public String vistaPaginaBuscarVehiculoReserva(Reserva reserva) {

    	return "c_buscar_vehiculo_reserva";
    }
    

    
    

    // c.c

    @GetMapping("/registro")
	
	public String registroCliente(Cliente cliente) {
		return "c_registro";

	}

    @GetMapping("/insertar")
	public String insertarCliente(Cliente cliente, BindingResult result, Model modelo,
			RedirectAttributes redirectAttrs) {
        
		this.cliente.registrarCliente(cliente);

		return "c_registro_valido";
	}
    
    
    
    

}
