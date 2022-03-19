package com.dss.tennis.tournament.tables.model.response.v1;

import com.dss.tennis.tournament.tables.exception.DetailedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.UNSUPPORTED_RESOURCE_TYPE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObject {

    private Integer id;
    private String type;
    private Object attributes;

    public enum ResourceObjectType {
        PLAYER("player"),
        TEAM("team"),
        TOURNAMENT("tournament"),
        CONTEST("contest");

        public final String value;

        private ResourceObjectType(String value) {
            this.value = value;
        }

        public static ResourceObjectType fromStringValue(String value) {
            Optional<ResourceObjectType> result = Arrays.stream(ResourceObjectType.values())
                    .filter(tt -> value.equals(tt.value)).findFirst();
            return result.orElseThrow(() -> new DetailedException(UNSUPPORTED_RESOURCE_TYPE, value));
        }
    }
}
