package de.geo2web.construction.adapter.persistence;

import lombok.*;
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
public class ConstructionElementJpa {

    @Id
    @Column
    @Type(type = "uuid-char")//To make sure UUID is stored as string not binary, because binary can't be read properly, apparently.
    UUID id;

    @Version
    @Column
    long version;

    int constructionIndex;

    String input;
}
