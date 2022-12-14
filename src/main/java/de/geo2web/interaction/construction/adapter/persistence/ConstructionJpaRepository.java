package de.geo2web.interaction.construction.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ConstructionJpaRepository extends JpaRepository<ConstructionElementJpa, UUID> {
}
