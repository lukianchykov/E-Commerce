package com.gbsfo.ecommerce.domain.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.gbsfo.ecommerce.utils.identifiable.IdentifiableEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "roles")
public class Role extends IdentifiableEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;
}