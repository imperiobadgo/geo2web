package de.geo2web.interaction.matrix4x4.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface Matrix4x4JpaRepository extends JpaRepository<Matrix4x4Jpa, UUID> {
}
