package org.jdelira.springbootapp.DAO;

import org.jdelira.springbootapp.models.Banco;

import java.util.List;

public interface BancoRepository {

    List<Banco> findAll();
    Banco findById(Long id);
    void update (Banco banco);
}
