package kma.cnpm.beapp.domain.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Locale {

    VIETNAM("vn"),
    US("us"),
    ;

    private final String code;
}
