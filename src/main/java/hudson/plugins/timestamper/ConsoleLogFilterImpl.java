package hudson.plugins.timestamper;

import hudson.console.ConsoleLogFilter;
import hudson.model.AbstractBuild;
import hudson.model.Run;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class ConsoleLogFilterImpl extends ConsoleLogFilter implements Serializable {

    private static final long serialVersionUID = 817268124L;

    public ConsoleLogFilterImpl(Run<?, ?> build) {
    }

    @Override
    public OutputStream decorateLogger(Run run, OutputStream logger) throws IOException, InterruptedException {
        return new TimestamperOutputStream(logger);
    }
}

