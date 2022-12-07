package de.geo2web.interaction.construction.adapter.persistence;

import de.geo2web.interaction.matrix4x4.adapter.persistence.Matrix4x4Jpa;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ConstructionElementJpa {

    @Id
    @Column
    @Type(type = "uuid-char")//To make sure UUID is stored as string not binary, because binary can't be read properly, apparently.
    UUID id;

    @Version
    @Column
    long version;

    int constructionIndex;

    String name;

    String input;

    String output;

    @OneToOne
    @JoinColumn(name = "transform_id")
    Matrix4x4Jpa transform;
}
