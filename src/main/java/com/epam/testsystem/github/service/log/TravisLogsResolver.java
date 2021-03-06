package com.epam.testsystem.github.service.log;

import com.epam.testsystem.github.service.http.HttpResolverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * github_test
 * Created on 08.07.17.
 */

@Service
@RequiredArgsConstructor
public class TravisLogsResolver implements LogResolver {
    private static final String REQUEST = "https://api.travis-ci.org/jobs/" +
            "%d/" + //<- job_id (calculate as build_id + 1)
            "log";
    private final HttpResolverService httpResolverService;

    public String getLogs(final String user, final long buildId) {
        return httpResolverService.sendGETRequest(String.format(REQUEST, buildId + 1), String.class);
    }
}
