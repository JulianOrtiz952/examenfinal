package ExamenFinal.ExamenFinal.repository;


import ExamenFinal.ExamenFinal.entity.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Integer> {
    Optional<Tienda> findByUuid(String uuid);
}
