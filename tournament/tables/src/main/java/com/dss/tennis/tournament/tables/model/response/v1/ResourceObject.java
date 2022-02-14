package com.dss.tennis.tournament.tables.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObject {

    private Integer id;
    private String type;
    private Object attributes;

    public enum ResourceObjectType {
        PLAYER("player");

        public final String value;

        private ResourceObjectType(String value) {
            this.value = value;
        }
    }
}
