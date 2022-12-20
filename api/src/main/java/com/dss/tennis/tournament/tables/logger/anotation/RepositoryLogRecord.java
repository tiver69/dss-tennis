package com.dss.tennis.tournament.tables.logger.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RepositoryLogRecord {

    QueryMethod method();

    ResultType resultType() default ResultType.GENERAL;

    enum QueryMethod {
        GET("[GET] %-13s: %s", ""),
        IS_QUERY("[IS_QUERY] %-8s: %s", "  ||  RESULT: %s"),
        UPDATE("[UPDATE] %-10s: %s", "  ||  RESULT: %s record(s) updated"),
        SAVE("[SAVE] %-12s: %s", "  ||  RESULT: %s"),
        DELETE("[DELETE] %-10s: %s", "  ||  RESULT: record deleted.");

        public final String recordFormat;
        public final String resultFormat;

        QueryMethod(String recordFormat, String resultFormat) {
            this.recordFormat = recordFormat;
            this.resultFormat = resultFormat;
        }
    }

    enum ResultType {
        GENERAL("  ||  RESULT: %s"),
        SINGLE_RECORD("  ||  RESULT:  %s"),
        MULTIPLE_RECORDS("  ||  RESULT: %s records found."),
        PAGE("  ||  RESULT: page %s/%s found with %s records.");

        public final String resultFormat;

        ResultType(String resultFormat) {
            this.resultFormat = resultFormat;
        }
    }
}
