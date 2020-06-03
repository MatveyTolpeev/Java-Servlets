package ru.service.db.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class User {
    private Integer id;
    private String firstName;
    private String secondName;
    private String positionId;
}
