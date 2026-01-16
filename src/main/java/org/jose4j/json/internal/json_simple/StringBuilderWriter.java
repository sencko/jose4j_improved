/*
 * Internal utility class for JSON serialization
 */
package org.jose4j.json.internal.json_simple;

import java.io.Writer;

/**
 * A Writer that uses StringBuilder instead of StringBuffer for better performance
 * in single-threaded contexts like JSON serialization.
 */
class StringBuilderWriter extends Writer {
    private final StringBuilder builder;

    public StringBuilderWriter() {
        this.builder = new StringBuilder();
    }

    public StringBuilderWriter(int initialCapacity) {
        this.builder = new StringBuilder(initialCapacity);
    }

    @Override
    public void write(int c) {
        builder.append((char) c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
        if ((off < 0) || (off > cbuf.length) || (len < 0) ||
            ((off + len) > cbuf.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        builder.append(cbuf, off, len);
    }

    @Override
    public void write(String str) {
        builder.append(str);
    }

    @Override
    public void write(String str, int off, int len) {
        builder.append(str, off, off + len);
    }

    @Override
    public Writer append(CharSequence csq) {
        builder.append(csq);
        return this;
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) {
        builder.append(csq, start, end);
        return this;
    }

    @Override
    public Writer append(char c) {
        builder.append(c);
        return this;
    }

    @Override
    public void flush() {
        // No-op for StringBuilder
    }

    @Override
    public void close() {
        // No-op for StringBuilder
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
