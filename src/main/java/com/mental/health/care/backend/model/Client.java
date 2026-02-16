package com.mental.health.care.backend.model;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@SuperBuilder 
@EqualsAndHashCode(callSuper = true) 
@ToString(callSuper = true)
public class Client extends BaseUser {
    @Indexed(unique = true, sparse = true)
    private String username;
}
