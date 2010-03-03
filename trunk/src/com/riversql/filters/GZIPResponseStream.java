

package com.riversql.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class GZIPResponseStream extends ServletOutputStream {
	protected OutputStream bufferedOutput = null;

	
	protected boolean closed = false;

	protected HttpServletResponse response = null;

	protected ServletOutputStream output = null;
	private int bufferSize = 256*1024;


	public GZIPResponseStream(HttpServletResponse response) throws IOException {
		super();
		closed = false;
		this.response = response;
		this.output = response.getOutputStream();
		bufferedOutput = new ByteArrayOutputStream();
		//gzipstream = new GZIPOutputStream(baos);
	}

	 
	public void close() throws IOException {	
		
		if (bufferedOutput instanceof ByteArrayOutputStream) {
		      // get the content
		      ByteArrayOutputStream baos = (ByteArrayOutputStream) bufferedOutput;

		      // prepare a gzip stream
		      ByteArrayOutputStream compressedContent = new ByteArrayOutputStream();
		      GZIPOutputStream gzipstream = new GZIPOutputStream(compressedContent);
		      byte[] bytes = baos.toByteArray();
		      gzipstream.write(bytes);
		      gzipstream.finish();

		      // get the compressed content
		      byte[] compressedBytes = compressedContent.toByteArray();

		      // set appropriate HTTP headers
		      response.setContentLength(compressedBytes.length);
		      response.addHeader("Content-Encoding", "gzip");
		      output.write(compressedBytes);
		      output.flush();
		      output.close();
		      closed = true;
		    }
		    // if things were not buffered in memory, finish the GZIP stream and
		    // response
		    else if (bufferedOutput instanceof GZIPOutputStream) {
		      // cast to appropriate type
		      GZIPOutputStream gzipstream = (GZIPOutputStream) bufferedOutput;

		      // finish the compression
		      gzipstream.finish();
		      gzipstream.flush();
		      gzipstream.close();
		      // finish the response
		      output.flush();
		      output.close();
		      closed = true;
		    }

		
	}
	private void checkBufferSize(int length) throws IOException {
	    // check if we are buffering too large of a file
	    if (bufferedOutput instanceof ByteArrayOutputStream) {
	      ByteArrayOutputStream baos = (ByteArrayOutputStream) bufferedOutput;

	      if ((baos.size() + length) > bufferSize) {
	        // files too large to keep in memory are sent to the client without
	        // Content-Length specified
	        response.addHeader("Content-Encoding", "gzip");

	        // get existing bytes
	        byte[] bytes = baos.toByteArray();

	        // make new gzip stream using the response output stream
	        GZIPOutputStream gzipstream = new GZIPOutputStream(output);
	        gzipstream.write(bytes);

	        // we are no longer buffering, send content via gzipstream
	        bufferedOutput = gzipstream;
	      }
	    }
	  }

	 
	public void flush() throws IOException {
		if (closed) {
			throw new IOException("Cannot flush a closed output stream");
		}
		bufferedOutput.flush();
	}

	 
	public void write(int b) throws IOException {
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		 checkBufferSize(1);

		    // write the byte to the temporary output
		 bufferedOutput.write((byte) b);

	}

	 
	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}

	 
	public void write(byte b[], int off, int len) throws IOException {
		//System.out.println("writing...");
		if (closed) {
			throw new IOException("Cannot write to a closed output stream");
		}
		checkBufferSize(len);

	    // write the content to the buffer
	    bufferedOutput.write(b, off, len);
	}

	public boolean closed() {
		return (this.closed);
	}

	public void reset() {
		// noop
	}
}
