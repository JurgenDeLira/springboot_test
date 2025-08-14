package org.jdelira.springbootapp.DAO;

import org.jdelira.springbootapp.models.Cuenta;

import java.util.List;

public interface CuentaRepository {
    List<Cuenta> findAll();

    Cuenta findById(Long id);

    void update(Cuenta cuenta);
}
