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

import static com.google.common.base.Preconditions.checkNotNull;
import hudson.Extension;
import hudson.Launcher;
import hudson.console.LineTransformationOutputStream;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.lang.time.FastDateFormat;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Build wrapper that decorates the build's logger to record time-stamps as each
 * line is logged.
 * <p>
 * Note: The Configuration Slicing Plugin depends on this class.
 *
 * @author Steven G. Brown
 */
public final class TimestamperBuildWrapper extends BuildWrapper {

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
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
    @SuppressWarnings("rawtypes")
    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {
        // Jenkins requires this method to be overridden.
        return new Environment() {
            // No tear down behaviour or additional environment variables.
        };
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public OutputStream decorateLogger(AbstractBuild build, OutputStream logger) {
        return new TimestamperOutputStream(logger, build);
    }


    /**
     * Output stream that writes each line to the provided delegate output stream
     * after inserting a {@link TimestampNote}.
     */
    private static class TimestamperOutputStream extends LineTransformationOutputStream {

        /**
         * The delegate output stream.
         */
        private final OutputStream delegate;
        private Run<?, ?> build;
        private OutputStreamWriter streamWriter;

        /**
         * Create a new {@link TimestampNotesOutputStream}.
         *
         * @param delegate
         *            the delegate output stream
         */
        TimestamperOutputStream(OutputStream delegate, Run<?, ?> build) {
            this.delegate = checkNotNull(delegate);
            this.streamWriter = new OutputStreamWriter(delegate);
            this.build = build;
        }

        private String format(Timestamp timestamp) {
            // Jenkins.
            // TODO get global configuration which defines timeFormat then
            String template = "[%s] ";
            String systemTime = FastDateFormat.getInstance(DEFAULT_TIME_FORMAT).format(new Date(timestamp.millisSinceEpoch));
            return String.format(template, systemTime);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void eol(byte[] b, int len) throws IOException {
            OutputStreamWriter streamWriter = new OutputStreamWriter(delegate);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis() - build.getTimeInMillis(), System
                .currentTimeMillis());
            streamWriter.write(format(timestamp));
            streamWriter.flush();
            delegate.write(b, 0, len);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            super.close();
            delegate.close();
        }
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
            return "displayname";
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
}
