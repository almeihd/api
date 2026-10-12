package com.example.trabalho.service;

import com.example.trabalho.model.Cliente;
import com.example.trabalho.model.Pedido;
import com.example.trabalho.model.Produto;
import com.example.trabalho.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    public Pedido salvar(Pedido pedido) {
        if (pedido.getProduto() == null || pedido.getProduto().getId() == null ||
                pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente e Produto são obrigatórios para o pedido.");
        }

        Produto produto = produtoService.buscarPorId(pedido.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getEstoque()) {
            throw new RuntimeException("Produto fora de estoque");
        }

        Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        pedido.setProduto(produto);
        pedido.setCliente(cliente);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
}