package hudson.plugins.timestamper;

import hudson.console.LineTransformationOutputStream;
import hudson.model.Run;
import org.apache.commons.lang.time.FastDateFormat;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by rkiesswetter on 27.07.17.
 */
public class TimestamperOutputStream extends LineTransformationOutputStream {

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * The delegate output stream.
     */
    private final OutputStream delegate;

    /**
     * @param delegate
     *            the delegate output stream
     */
    TimestamperOutputStream(OutputStream delegate) {
        this.delegate = checkNotNull(delegate);
    }

    private String format(long currentTimeInMillis) {
        // Jenkins.
        // TODO get global configuration which defines timeFormat then
        String template = "[%s] ";
        String systemTime = FastDateFormat.getInstance(DEFAULT_TIME_FORMAT).format(currentTimeInMillis);
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