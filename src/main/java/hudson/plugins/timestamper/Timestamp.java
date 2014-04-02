/*
 * The MIT License
 * 
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

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Objects;

/**
 * A time-stamp, consisting of the elapsed time and the clock time.
 * 
 * @author Steven G. Brown
 */
@Immutable
public final class Timestamp {

  /**
   * The elapsed time in milliseconds since the start of the build.
   */
  public final long elapsedMillis;

  /**
   * The clock time in milliseconds since midnight, January 1, 1970 UTC.
   */
  public final long millisSinceEpoch;

  /**
   * Create a {@link Timestamp}.
   * 
   * @param elapsedMillis
   *          the elapsed time in milliseconds since the start of the build
   * @param millisSinceEpoch
   *          the clock time in milliseconds since midnight, January 1, 1970 UTC
   */
  public Timestamp(long elapsedMillis, long millisSinceEpoch) {
    this.elapsedMillis = elapsedMillis;
    this.millisSinceEpoch = millisSinceEpoch;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(elapsedMillis, millisSinceEpoch);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Timestamp) {
      Timestamp other = (Timestamp) obj;
      return elapsedMillis == other.elapsedMillis
          && millisSinceEpoch == other.millisSinceEpoch;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return Objects.toStringHelper(this).add("elapsedMillis", elapsedMillis)
        .add("millisSinceEpoch", millisSinceEpoch).toString();
  }
}
