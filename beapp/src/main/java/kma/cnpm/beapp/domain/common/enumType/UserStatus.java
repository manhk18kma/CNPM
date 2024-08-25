package kma.cnpm.beapp.domain.common.enumType;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("active")
    ACTIVE ,
    @JsonProperty("inactive")
    INACTIVE ,
    @JsonProperty("none")
    NONE
}
