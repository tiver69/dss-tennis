package com.dss.tennis.tournament.tables.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRefreshToken {

    private String refreshToken;
}
