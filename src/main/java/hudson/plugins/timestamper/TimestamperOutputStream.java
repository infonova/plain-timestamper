package hudson.plugins.timestamper;

import hudson.console.LineTransformationOutputStream;
import org.apache.commons.lang.time.FastDateFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import jenkins.model.GlobalConfiguration;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rkiesswetter on 27.07.17.
 */
public class TimestamperOutputStream extends LineTransformationOutputStream {

    /**
     * The delegate output stream.
     */
    private final OutputStream delegate;
    private final String timestampFormat;

    /**
     * @param delegate
     *            the delegate output stream
     */
    TimestamperOutputStream(OutputStream delegate) {
        this.delegate = checkNotNull(delegate);
        TimestamperConfig timestamperConfig = GlobalConfiguration.all().get(TimestamperConfig.class);
        if(timestamperConfig != null) {
            timestampFormat =  timestamperConfig.getSystemTimeFormat();
        } else {
            timestampFormat = TimestamperConfig.DEFAULT_TIMESTAMP_FORMAT;
        }
    }

    private String format(long currentTimeInMillis) {
        // Jenkins.
        String template = "[%s] ";
        String systemTime = FastDateFormat.getInstance(timestampFormat).format(currentTimeInMillis);
        return String.format(template, systemTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void eol(byte[] b, int len) throws IOException {
        OutputStreamWriter streamWriter = new OutputStreamWriter(delegate, "UTF-8");
        streamWriter.write(format(System.currentTimeMillis()));
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