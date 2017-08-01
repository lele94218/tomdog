package com.terryx.tomdog.connector.http;

import com.terryx.tomdog.connector.ResponseStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Response stream for the HTTP/1.1 connector. This stream will automatically
 * chunk the answer if using HTTP/1.1 and no Content-Length has been properly
 * set.
 *
 * @author <a href="mailto:remm@apache.org">Remy Maucherat</a>
 */
public final class HttpResponseStream extends ResponseStream {


    // ----------------------------------------------------------- Constructors


    private static final int MAX_CHUNK_SIZE = 4096;


    private static final String CRLF = "\r\n";


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet output stream associated with the specified Request.
     *
     * @param response The associated response
     */
    public HttpResponseStream(HttpResponseImpl response) {

        super(response);
        checkChunking(response);
        checkHead(response);

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * True if chunking is allowed.
     */
    private boolean useChunking;


    /**
     * True if printing a chunk.
     */
    private boolean writingChunk;


    /**
     * True if no content should be written.
     */
    private boolean writeContent;


    // -------------------------------------------- ServletOutputStream Methods


    /**
     * Write the specified byte to our output stream.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int b)
            throws IOException {

        if (suspended)
            return;

        if (!writeContent)
            return;

        if (useChunking && !writingChunk) {
            writingChunk = true;
            try {
                print("1\r\n");
                super.write(b);
                println();
            } finally {
                writingChunk = false;
            }
        } else {
            super.write(b);
        }

    }


    /**
     * Write the specified byte array.
     */
    public void write(byte[] b, int off, int len)
            throws IOException {

        if (suspended)
            return;

        if (!writeContent)
            return;

        if (useChunking && !writingChunk) {
            if (len > 0) {
                writingChunk = true;
                try {
                    println(Integer.toHexString(len));
                    super.write(b, off, len);
                    println();
                } finally {
                    writingChunk = false;
                }
            }
        } else {
            super.write(b, off, len);
        }

    }


    /**
     * Close this output stream, causing any buffered data to be flushed and
     * any further output data to throw an IOException.
     */
    public void close() throws IOException {

        if (suspended) {
//            throw new IOException
//                    (sm.getString("responseStream.suspended"));
        }

        if (!writeContent)
            return;

        if (useChunking) {
            // Write the final chunk.
            writingChunk = true;
            try {
                print("0\r\n\r\n");
            } finally {
                writingChunk = false;
            }
        }
        super.close();

    }


    // -------------------------------------------------------- Package Methods


    void checkChunking(HttpResponseImpl response) {
        // If any data has already been written to the stream, we must not
        // change the chunking mode
        if (count != 0)
            return;
        // Check the basic cases in which we chunk
        useChunking =
                (!response.isCommitted()
                        && response.getContentLength() == -1
                        && response.getStatus() != HttpServletResponse.SC_NOT_MODIFIED);
        if (!response.isChunkingAllowed() && useChunking) {
            // If we should chunk, but chunking is forbidden by the connector,
            // we close the connection
            response.setHeader("Connection", "close");
        }
        // Don't chunk is the connection will be closed
        useChunking = (useChunking && !response.isCloseConnection());
        if (useChunking) {
            response.setHeader("Transfer-Encoding", "chunked");
        } else if (response.isChunkingAllowed()) {
            response.removeHeader("Transfer-Encoding", "chunked");
        }
    }


    protected void checkHead(HttpResponseImpl response) {
        HttpServletRequest servletRequest =
                (HttpServletRequest) response.getRequest();
        if ("HEAD".equals(servletRequest.getMethod())) {
            writeContent = false;
        } else {
            writeContent = true;
        }
    }


}

