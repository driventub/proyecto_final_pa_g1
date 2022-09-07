package ec.edu.uce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ec.edu.uce.modelo.Cliente;
import ec.edu.uce.service.IClienteService;
import ec.edu.uce.service.IGestorEmpleadoService;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

	@Autowired
	private IGestorEmpleadoService gestorEmpleadoService;
	
	  @Autowired
	    private IClienteService clienteService;
	
	@GetMapping("/registro")	
	public String registroClienteE(Cliente cliente) {
		return "e_registro";
	}

    @GetMapping("/insertar")
	public String insertarClienteE(Cliente cliente, BindingResult result, Model modelo,
			RedirectAttributes redirectAttrs) {
    		this.gestorEmpleadoService.registrarCliente(cliente);
		//return "c_registro_valido";
    		System.out.println("registrando tipo empleado");
		return "redirect:/empleados/todos?exito";
	}
    
    @GetMapping("/editar/{clieCedula}")
    public String editarCliente(@PathVariable("clieCedula") String cedula, Cliente cliente,Model modelo) {
    	Cliente clie = this.clienteService.buscarClientePorCedula(cedula);
    	System.out.println(clie.toString());
    	modelo.addAttribute("clie", clie);
    	return "clieActualizaE";
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
    	return "redirect:/empleados/todos";
    }
    
    @GetMapping("/eliminar/{clieCedula}")
    public String eliminarCliente(@PathVariable("clieCedula")String cedula) {
    	Cliente cliente = this.clienteService.buscarClientePorCedula(cedula);
    	this.clienteService.borrarClientePorId(cliente.getId());
//    	redirectAttrs
//        .addFlashAttribute("mensaje", "Cliente cedula: "+clie.getCedula()+"Eliminado correctamente")
//        .addFlashAttribute("clase", "success");
    	return "redirect:/empleados/todos";
    }
    
    @GetMapping("/ver/{cedula}")
    public String verDatosCliente(@PathVariable("cedula")String cedula, Model modelo) {
    	Cliente cliente = this.clienteService.buscarClientePorCedula(cedula);
    	modelo.addAttribute("cliente", cliente);
    	return "datosCliente";
    }
    
    
    @GetMapping("/todos")
    public String listarClientes(Model modelo,@Param("apellido")String apellido) {
 	   List<Cliente> clientes2 = this.clienteService.listarClientes();
    	
    	List<Cliente> clientes = this.clienteService.listarClientesPorApellido(apellido);
    	modelo.addAttribute("clientes", clientes);
    	modelo.addAttribute("apellido", apellido);
    	System.out.println("apellido: "+apellido);
    	if(apellido==null) {
    		modelo.addAttribute("clientes", clientes2);
    	}
    	
     	return "todosClientesE";
    }
}
