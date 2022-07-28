package com.aws.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

//import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Data
//@Entity
//@Table(name = "customer")
public class Customer {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @PrimaryKeyColumn(
            name = "id",
            ordinal = 2,
            type = PrimaryKeyType.CLUSTERED,
            ordering = Ordering.DESCENDING)
    private UUID id;

    //@Column(name="firstname")
    private String firstName;

    //@Column(name="lastname")
    private String lastName;
}
