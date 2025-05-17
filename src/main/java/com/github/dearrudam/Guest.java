package com.github.dearrudam;

import jakarta.nosql.Column;
import jakarta.nosql.Embeddable;
import jakarta.nosql.Id;

@Embeddable
public record Guest(
        @Id
        String document,
        @Column
        String name
) {
}
