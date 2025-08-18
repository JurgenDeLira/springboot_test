package org.jdelira.springbootapp.DAO;

import org.jdelira.springbootapp.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Long> {

    //List<Banco> findAll();
    //Banco findById(Long id);
    //void update (Banco banco);
}
