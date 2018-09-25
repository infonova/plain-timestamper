/*
 * The MIT License
 *
 * Copyright (c) 2012 Frederik Fromm
 * Copyright (c) 2012 Steven G. Brown
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
import hudson.Extension;
import hudson.model.Descriptor;
import java.text.SimpleDateFormat;
import javax.annotation.CheckForNull;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import com.google.common.base.Objects;

/**
 * Global configuration for the Timestamper plug-in, as shown on the Jenkins
 * Configure System page.
 *
 * @author Frederik Fromm
 */
@Extension
public final class TimestamperConfig extends GlobalConfiguration {

    /**
     * The default {@link #timestampFormat}.
     */
    public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * The chosen format for displaying the system clock time, as recognised by
     * {@link SimpleDateFormat}.
     */
    @CheckForNull
    private String timestampFormat;

    /**
     * Constructor.
     */
    public TimestamperConfig() {
        load();
    }

    /**
     * Get the format for displaying the system clock time.
     *
     * @return the system clock time format
     */
    public String getSystemTimeFormat() {
        return Objects.firstNonNull(timestampFormat, DEFAULT_TIMESTAMP_FORMAT);
    }

    /**
     * Set the format for displaying the system clock time.
     *
     * @param timestampFormat
     *          the system clock time format in {@link SimpleDateFormat} pattern
     */
    public void setSystemTimeFormat(@CheckForNull String timestampFormat) {
        this.timestampFormat = timestampFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean configure(StaplerRequest req, JSONObject json)
            throws Descriptor.FormException {
        req.bindJSON(this, json);
        save();
        return true;
    }
}
