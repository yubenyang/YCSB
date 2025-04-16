package site.ycsb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Corpus loads an entire file into memory and allows
 * sampling fixed-size (4KB) chunks deterministically
 * based on a numeric key.
 */
public class Corpus {
  private final byte[] buffer;
  private final int pageSize = 4096;
  private final int totalPages;
  
  public Corpus(String filename) throws IOException {
    this.buffer = Files.readAllBytes(Paths.get(filename));
    if (buffer.length < pageSize) {
      throw new IllegalArgumentException("File is smaller than one 4KB page.");
    }
    this.totalPages = buffer.length / pageSize;
  }

  public ByteIterator getChunkForKey(long key) {
    int pageIndex = (int) (key % totalPages);
    int offset = pageIndex * pageSize;

    byte[] chunk = new byte[pageSize];
    System.arraycopy(buffer, offset, chunk, 0, pageSize);

    return new ByteArrayByteIterator(chunk);
  }
}
