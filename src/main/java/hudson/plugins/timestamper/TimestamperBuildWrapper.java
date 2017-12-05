/*
 * The MIT License
 *
 * Copyright (c) 2011 Steven G. Brown
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.timestamper;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.console.ConsoleLogFilter;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import jenkins.tasks.SimpleBuildWrapper;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Build wrapper that decorates the build's logger to record time-stamps as each
 * line is logged.
 */
public final class TimestamperBuildWrapper extends SimpleBuildWrapper implements Serializable {

    private static final long serialVersionUID = 817268125L;

    private static final Logger LOGGER = Logger.getLogger(TimestamperBuildWrapper.class.getName());

    /**
     * Create a new {@link TimestamperBuildWrapper}.
     */
    @DataBoundConstructor
    public TimestamperBuildWrapper() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher,
                      TaskListener listener, EnvVars initialEnvironment) throws IOException, InterruptedException {
        // nothing to do
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ConsoleLogFilter createLoggerDecorator(Run<?, ?> build) {
        return new ConsoleLogFilterImpl(build);
    }

    /**
     * Registers {@link TimestamperBuildWrapper} as a {@link BuildWrapper}.
     */
    @Extension
    public static final class DescriptorImpl extends BuildWrapperDescriptor {

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return Messages.DisplayName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }
    }

}
