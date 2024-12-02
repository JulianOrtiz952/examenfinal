package ExamenFinal.ExamenFinal.repository;

import ExamenFinal.ExamenFinal.entity.Cajero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajeroRepository extends JpaRepository<Cajero, Integer> {
    Optional<Cajero> findByToken(String token);
}
