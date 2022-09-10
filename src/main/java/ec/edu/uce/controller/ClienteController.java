package ec.edu.uce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ec.edu.uce.modelo.Cliente;
import ec.edu.uce.modelo.ReservarVehiculoTO;
import ec.edu.uce.modelo.Vehiculo;
import ec.edu.uce.service.IClienteService;
import ec.edu.uce.service.IGestorClienteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    
	private static Logger LOG =  LogManager.getLogger(ClienteController.class);

    @Autowired
    private IGestorClienteService iGestorClienteService;
      
    @Autowired
    private IClienteService clienteService;

    // c.a
    @GetMapping("/vehiculoBuscar")
	public String obtenerVehiculo(Vehiculo vehiculo) {
		return "v_buscar_marca";
	}

    @GetMapping("/buscar/marcaModelo")
	public String buscarVehiculo(Vehiculo vehiculo, BindingResult result, Model model,
			RedirectAttributes redirectAttrs) {

		List<Vehiculo> listaVehiculo = this.iGestorClienteService.buscarVehiculosDisponibles( vehiculo.getMarca(),vehiculo.getModelo());
        model.addAttribute("listaVehiculo", listaVehiculo);
		return "v_m_marca";
	}


    // c.c

    @GetMapping("/registro")	
	public String registroCliente(Cliente cliente) {
		return "c_registro";

	}

    @GetMapping("/insertar")
	public String insertarClienteC(Cliente cliente, BindingResult result, Model modelo,
			RedirectAttributes redirectAttrs) {
    		this.iGestorClienteService.registrarCliente(cliente);
		//return "c_registro_valido";
		return "redirect:/clientes/registro?exito";
	}
    
    
    

    @GetMapping("/editar/{clieCedula}")
    public String editarCliente(@PathVariable("clieCedula") String cedula, Cliente cliente,Model modelo) {
    	Cliente clie = this.clienteService.buscarClientePorCedula(cedula);
    	LOG.info(clie.toString());
    	modelo.addAttribute("clie", clie);
    	return "clieActualiza";
    }
    
    @PutMapping("/actualizar/{clieCedula}")
    public String actualizarCliente(@PathVariable("clieCedula")String cedula,Cliente clie,RedirectAttributes redirectAttrs) {
    	Cliente cliente = this.clienteService.buscarClientePorCedula(clie.getCedula());
    	clie.setId(cliente.getId());
    	clie.setTipoRegistro(cliente.getTipoRegistro());
    	clie.setReservaActiva(cliente.getReservaActiva());
    	this.clienteService.actualizarCliente(clie);
    	redirectAttrs
        .addFlashAttribute("mensaje", "Cliente cedula: "+clie.getCedula()+" editado correctamente")
        .addFlashAttribute("clase", "success");
    	return "redirect:/clientes/todos";
    }
    
    @GetMapping("/todos")
   public String listarClientes(Model modelo) {
	   List<Cliente> clientes = this.clienteService.listarClientes();
	   modelo.addAttribute("clientes", clientes);
    	return "todosClientes";
   }


//    
   @GetMapping("retirar/sinReserva")
	private String retirarSinReserva( ReservarVehiculoTO reservarVehiculoTO, Model modelo) {

		modelo.addAttribute("visible1", false);
		modelo.addAttribute("visible2", false);
		modelo.addAttribute("visible3", false);
		return "sin_reserva";
	}

	@PostMapping("disponiblidad")
	public String buscarVehiculosT(@RequestParam("marca") String idMarca,
			@RequestParam("modelo") String idModelo, Model modelo, 
			ReservarVehiculoTO reservarVehiculoTO) {

		modelo.addAttribute("listVehiculos", this.iGestorClienteService.buscarVehiculosDisponibles(idMarca, idModelo));
		modelo.addAttribute("visible1", true);
		modelo.addAttribute("visible2", true);
		modelo.addAttribute("visible3", false);
		return "sin_reserva";
	}

	@PostMapping("buscarReserva")
	public String insertarReserva(ReservarVehiculoTO reservarVehiculoTO, 
			BindingResult result, Model modelo, RedirectAttributes redirectAttributes) {

		if (this.iGestorClienteService.verificarDisponibilidad(reservarVehiculoTO)) {
			redirectAttributes.addFlashAttribute("mensaje", "Vehiculo disponible");
			reservarVehiculoTO.setValorTotalAPagar(this.iGestorClienteService.generarPago(reservarVehiculoTO.getPlaca(),
					reservarVehiculoTO.getFechaInicio(), reservarVehiculoTO.getFechaFinal()).getValorTotalAPagar());

			modelo.addAttribute("reservarVehiculoTO", reservarVehiculoTO);
			modelo.addAttribute("visible1", false);
			modelo.addAttribute("visible2", false);
			modelo.addAttribute("visible3", true);
			return "sin_reserva";
		} else {
			redirectAttributes.addFlashAttribute("mensaje", "Vehiculo no disponible o Fechas incorrectas");
			return "redirect:/empleados/retirar/sinReserva";
		}

	}

	@PostMapping("insertarPago")
	public String insertarPago(ReservarVehiculoTO reservarVehiculoTO,  BindingResult result,
			Model modelo, RedirectAttributes redirectAttributes) {

		LOG.info(reservarVehiculoTO.getFechaInicio());
		LOG.info(reservarVehiculoTO.getFechaFinal());
		this.iGestorClienteService.crearReserva(reservarVehiculoTO);
		redirectAttributes.addFlashAttribute("mensaje", "Reservacion Creada");
		return "redirect:/empleados/retirar/sinReserva";
	}
}
