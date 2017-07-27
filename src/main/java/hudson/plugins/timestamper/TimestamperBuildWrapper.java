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
 * <p>
 * Note: The Configuration Slicing Plugin depends on this class.
 *
 * @author Steven G. Brown
 */
public final class TimestamperBuildWrapper extends SimpleBuildWrapper implements Serializable{

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

/*    *//**
     * {@inheritDoc}
     *//*
    @SuppressWarnings("rawtypes")
    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {
        // Jenkins requires this method to be overridden.
        return new Environment() {
            // No tear down behaviour or additional environment variables.
        };
    }*/

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
            return "plain text timestamp";
            //            return Messages.DisplayName();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }
    }

    private class ConsoleLogFilterImpl extends ConsoleLogFilter implements Serializable {
        public ConsoleLogFilterImpl(Run<?, ?> build) {
        }

        @Override
        public OutputStream decorateLogger(AbstractBuild abstractBuild, OutputStream logger) throws IOException, InterruptedException {
            return new TimestamperOutputStream(logger, abstractBuild);
        }
    }
}
