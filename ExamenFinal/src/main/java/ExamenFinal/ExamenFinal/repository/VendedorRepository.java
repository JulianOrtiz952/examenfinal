package ExamenFinal.ExamenFinal.repository;

import ExamenFinal.ExamenFinal.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Integer> {
    Optional<Vendedor> findByDocumento(String documento);
}
