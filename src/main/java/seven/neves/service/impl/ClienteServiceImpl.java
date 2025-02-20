package seven.neves.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seven.neves.model.Cliente;
import seven.neves.model.ClienteRepository;
import seven.neves.model.Endereco;
import seven.neves.model.EnderecoRepository;
import seven.neves.service.ClienteService;
import java.util.Optional;
import seven.neves.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired 
    private ViaCepService viaCepService;
    @Override
    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }
    @Override   
    public void inserir(Cliente cliente){
        salvarClienteComCep(cliente);
    }
    private void salvarClienteComCep(Cliente cliente){
        String cep=cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(()->{
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
    @Override
	public Cliente buscarPorId(Long id){
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }
    @Override
	public void atualizar(Long id, Cliente cliente){
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()){
            salvarClienteComCep(cliente);
        }
    }
    @Override
	public void deletar(Long id){
        clienteRepository.deleteById(id);
    }
}
