package de.geo2web.interaction.matrix4x4.adapter.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Matrix4x4Jpa {

    @Id
    @Column
    @Type(type = "uuid-char")//To make sure UUID is stored as string not binary, because binary can't be read properly, apparently.
    UUID id;

    @Version
    @Column
    long version;

    @Column
    float n11;
    @Column
    float n12;
    @Column
    float n13;
    @Column
    float n14;

    @Column
    float n21;
    @Column
    float n22;
    @Column
    float n23;
    @Column
    float n24;

    @Column
    float n31;
    @Column
    float n32;
    @Column
    float n33;
    @Column
    float n34;

    @Column
    float n41;
    @Column
    float n42;
    @Column
    float n43;
    @Column
    float n44;
}
