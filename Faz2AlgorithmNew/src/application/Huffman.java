package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

public class Huffman {

	public HuffCode[] huffCodeArray;

	public int huffCodeArraySize = 0;
	int compressedSize;
	int originalSize;
	public String outFileName;

	String filepath;
	FileInputStream scan;
	int size = 0;
	byte[] buffer;
	int[] tmpFreq;
	int tracker = 0;
	int numOfChar = 0;

	byte[] outbuffer;

	FileOutputStream output;

	String[] out;
	File file;

	public void compress(File file) {
		try {

			filepath = file.getPath();
			scan = new FileInputStream(filepath);
			size = 0;
			buffer = new byte[1024]; // Read file as (bytes)
			tmpFreq = new int[256];// ascii code 128 alphabet and barity bit

			getFreqOfChar();
			System.out.println("huffCodeArraySize" + huffCodeArraySize);

			getMinimizeTheFreqArray();

			getHeapTreeAndHuffmanCode();

			getHeaderAndData(file);

		} catch (IOException e) {
			e.getMessage();
		} catch (Exception e) {
			e.getMessage();
		}

	}

	private void getHeaderAndData(File file) throws IOException {

		int index = -1;
		String[] out = new String[256];
		for (int i = 0; i < huffCodeArraySize; i++)
			out[(int) huffCodeArray[i].getCharacter()] = new String(huffCodeArray[i].getHuffCode());

		outFileName = new StringTokenizer(file.getAbsolutePath(), ".").nextToken() + ".huff";
		File f = new File(outFileName);
		
		FileOutputStream output = new FileOutputStream(outFileName);
		byte[] outbuffer = new byte[1024];
		tracker = 0;

		// The Name of The Original File with extention

		for (int i = 0; i < filepath.length(); i++)
			outbuffer[tracker++] = (byte) filepath.charAt(i);
		outbuffer[tracker++] = '\n';

		// Number of Characters in the Original File(all the file)
		String nbchar = String.valueOf(numOfChar);
		for (int i = 0; i < nbchar.length(); i++) {
			outbuffer[tracker++] = (byte) nbchar.charAt(i);
		}
		outbuffer[tracker++] = '\n';

		// Number of Distinct Characters
		for (int i = 0; i < String.valueOf(huffCodeArraySize).length(); i++)
			outbuffer[tracker++] = (byte) String.valueOf(huffCodeArraySize).charAt(i);
		outbuffer[tracker++] = '\n';

		output.write(outbuffer, 0, tracker);
		tracker = 0;
		for (int i = 0; i < outbuffer.length; i++)
			outbuffer[i] = 0;

		// The HuffCode for Each Character
		for (int i = 0; i < huffCodeArraySize; i++) {
			if (tracker == 1024) {
				output.write(outbuffer);
				tracker = 0;
			}
			outbuffer[tracker++] = (byte) huffCodeArray[i].getCharacter();
			if (tracker == 1024) {
				output.write(outbuffer);
				tracker = 0;
			}
			// Add the Counter
			outbuffer[tracker++] = (byte) huffCodeArray[i].getCodeLength();
			String res = "";
			Long HafManCod ;
			if (huffCodeArray[i].getHuffCode().length() > 15) { // (/2) avoid overflow issues during compression.
				for (int z = 0; z < huffCodeArray[i].getHuffCode().length() / 2; z++) {
					res += huffCodeArray[i].getHuffCode().charAt(z) + "";
				}
				HafManCod = Long.parseLong(res);
				res = "";
				for (int z = (huffCodeArray[i].getHuffCode().length() + 1) / 2; z < huffCodeArray[i].getHuffCode()
						.length(); z++) {
					res += huffCodeArray[i].getHuffCode().charAt(z) + "";
				}
				HafManCod += Long.parseLong(res);

			} else {
				HafManCod = Long.parseLong(huffCodeArray[i].getHuffCode());
			}
			byte[] code = new byte[50];
			int l = 0;
			if (HafManCod == 0) {
				outbuffer[tracker++] = 0;
				if (tracker == 1024) {
					output.write(outbuffer);
					tracker = 0;
				}

				outbuffer[tracker++] = 0;
				if (tracker == 1024) {
					output.write(outbuffer);
					tracker = 0;
				}
			} else {
				while (HafManCod != 0) {
					if (tracker == 1024) {
						output.write(outbuffer);
						tracker = 0;
					}
					code[l++] = (byte) (HafManCod % 256); // to save it as 8 byte
					HafManCod /= 256;
				}
				outbuffer[tracker++] = (byte) l;
				if (tracker == 1024) {
					output.write(outbuffer);
					tracker = 0;
				}
				for (int j = 0; j < l; j++) {
					outbuffer[tracker++] = code[j];
					if (tracker == 1024) {
						output.write(outbuffer);
						tracker = 0;
					}
				}
			}

			if (tracker == 1024) {
				output.write(outbuffer);
				tracker = 0;
			}
			outbuffer[tracker++] = '\n';
		} // end for

		// Print Out The Header
		output.write(outbuffer, 0, tracker);

		// Reinitialize the Output Buffer
		for (int i = 0; i < outbuffer.length; i++)
			outbuffer[i] = 0;

		scan.close();
		scan = new FileInputStream(filepath);
		tracker = 0;
		size = scan.read(buffer, 0, 1024);
		do {
			for (int i = 0; i < size; i++) {
				index = buffer[i];
				if (index < 0)// If the Value was negative
					index += 256;
				for (int j = 0; j < out[index].length(); j++) {
					char ch = out[index].charAt(j);
					if (ch == '1')
						outbuffer[tracker / 8] = (byte) (outbuffer[tracker / 8] | 1 << 7 - tracker % 8);
					tracker++;
					if (tracker / 8 == 1024) {
						output.write(outbuffer);
						for (int k = 0; k < outbuffer.length; k++)
							outbuffer[k] = 0;
						tracker = 0;
					}
				}
			}
			size = scan.read(buffer, 0, 1024);
		} while (size > 0);
		scan.close();
		output.write(outbuffer, 0, (tracker / 8) + 1);
		output.close();
		scan = new FileInputStream(f);
		compressedSize = scan.available();
		scan.close();

	}

	private void getHeapTreeAndHuffmanCode() {
//		get the tree to get the huffman code 

		if (huffCodeArraySize != 1) {
			Node[] t = new Node[huffCodeArraySize];
			Heap h = new Heap(huffCodeArraySize + 10);// extra space
			for (int i = 0; i < huffCodeArraySize; i++) {
				if (huffCodeArray[i] != null) {
					t[i] = new Node(huffCodeArray[i].getCounter(), huffCodeArray[i].getCharacter());
					h.addHeap(t[i]);
				}
			} // Add the character and its frequency in tree node m,, then
				// add the tree node to the heap

			for (int i = 1; i <= t.length - 1; i++) {
				Node z = new Node();
				Node x = h.deleteHeap();
				Node y = h.deleteHeap();
				z.freq = x.freq + y.freq;
				z.left = x;
				z.right = y;
				h.addHeap(z);
			} // delete from heap and add a new tree node

			getCodes(h.getHeap()[1], "");
		} else {
			huffCodeArray[0].setHuffCode("1");
			huffCodeArray[0].setCodeLength(1);
		}

	}


	private void getMinimizeTheFreqArray() {
		// Shrink the original array, building the huffCodeArray
		huffCodeArray = new HuffCode[huffCodeArraySize]; // building the HuffmanCodeArray

		if (huffCodeArray != null) {
			for (int i = 0; i < 256; i++) {
				if (tmpFreq[i] != 0) {
					huffCodeArray[tracker++] = new HuffCode((char) i, tmpFreq[i]);// Number of Frequency
					numOfChar += tmpFreq[i];
					tmpFreq[i] = 0;
				}
			}
		} else {
			System.err.println("Error: huffCodeArray is null. Make sure it's initialized properly.");
		}
	}

	private void getFreqOfChar() throws IOException {
		originalSize = scan.available();

		// System.out.println("avalable num : "+ originalSize);

		// file to do compress
		// Reading the file as bytes
		size = scan.read(buffer, 0, 1024);
		int index = -1;
		do {
			for (int i = 0; i < size; i++) {// count the frequency
				index = buffer[i];
				if (index < 0) // If the Value was negative (the range -128 to 127)... the value must to be
								
					index += 256;// positive
				if (tmpFreq[index] == 0)
					huffCodeArraySize++; // Counting number of different characters in the file
				tmpFreq[index]++;
			}
			size = scan.read(buffer, 0, 1024);
		} while (size > 0); // Finish Counting

		for (int i = 0; i < buffer.length; i++)
			buffer[i] = 0;

	}

	// Declare a variable to hold the original file name after decompression
	String originalFileName = "";

	// Decompress method to handle decompression of a file
	public void deCompress(File infile) {
	    try {
	        int size = 0;  // Variable to hold the size of the read data
	        String fileName = infile.getPath();  // Get the path of the input file

	        int bufferIndex = 0; // Index to keep track of the buffer
	        int bufferTracker = 0; // Pointer to track the current position in the buffer
	        boolean flag = true; // Flag to control reading loops

	        File file = new File(fileName);  // Create a file object for the input file
//	        FileInputStream scan = new FileInputStream(file);  // Create a FileInputStream to read the file
	        byte[] buffer = new byte[1024];  // Buffer to hold chunks of the file

	        // Read the original file name (the first line)
	        size = scan.read(buffer, 0, 1024);  // Read the first 1024 bytes
	        char[] tmp = new char[200];  // Temporary character array to store the file name
	        while (flag) {
	            if (buffer[bufferIndex] != '\n')  // Check for newline character
	                tmp[bufferIndex++] = (char) buffer[bufferTracker++];  // Store characters in the array
	            else
	                flag = false;  // Stop reading once newline is found
	        }

	        bufferTracker++;  // Move the tracker to the next byte after the newline
	        originalFileName = String.valueOf(tmp, 0, bufferIndex);  // Convert char array to string for file name

	        // Read the number of characters in the file (second line)
	        long numberOfChar = 0;
	        bufferIndex = 0;
	        flag = true;
	        while (flag) {
	            if (bufferTracker == 1024) {
	                size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                bufferTracker = 0;
	            }
	            if (buffer[bufferTracker] != '\n')
	                tmp[bufferIndex++] = (char) buffer[bufferTracker++];  // Store characters in the array
	            else
	                flag = false;  // Stop reading at newline
	        }
	        numberOfChar = Integer.parseInt(String.valueOf(tmp, 0, bufferIndex));  // Parse the number of characters
	        bufferTracker++;  // Move the tracker to the next byte

	        // Read the number of distinct characters (third line)
	        int loopSize = 0;
	        bufferIndex = 0;
	        flag = true;
	        while (flag) {
	            if (bufferTracker == 1024) {
	                size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                bufferTracker = 0;
	            }
	            if (buffer[bufferTracker] != '\n')
	                tmp[bufferIndex++] = (char) buffer[bufferTracker++];  // Store characters in the array
	            else
	                flag = false;  // Stop at newline
	        }
	        loopSize = Integer.parseInt(String.valueOf(tmp, 0, bufferIndex));  // Parse the number of distinct characters
	        bufferTracker++;  // Move the tracker to the next byte

	        // Read Huffman codes for characters
	        huffCodeArray = new HuffCode[loopSize];  // Initialize HuffCode array for distinct characters
	        huffCodeArraySize = loopSize;  // Store the number of Huffman codes
	        for (int i = 0; i < loopSize; i++) {
	            // Set character for each Huffman code
	            huffCodeArray[i] = new HuffCode((char) Byte.toUnsignedInt(buffer[bufferTracker++]));
	            if (bufferTracker == 1024) {
	                size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                bufferTracker = 0;
	            }
	            // Set code length for the Huffman code
	            huffCodeArray[i].setCodeLength(buffer[bufferTracker++]);
	            if (bufferTracker == 1024) {
	                size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                bufferTracker = 0;
	            }
	            int l = buffer[bufferTracker++];
	            if (bufferTracker == 1024) {
	                size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                bufferTracker = 0;
	            }
	            if (l == 0)
	                bufferTracker++;  // Skip if length is zero
	            if (bufferTracker == 1024) {
	                size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                bufferTracker = 0;
	            }
	            long x = 0;
	            for (int j = 0; j < l; j++) {
	                x += Byte.toUnsignedLong(buffer[bufferTracker++]) * (1 << 8 * j);  // Build the Huffman code
	                if (bufferTracker == 1024) {
	                    size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                    bufferTracker = 0;
	                }
	            }
	            huffCodeArray[i].setHuffCode(String.valueOf(x));  // Set the Huffman code
	            // Ensure Huffman code length is correct by padding with zeros if necessary
	            if (huffCodeArray[i].getHuffCode().length() != huffCodeArray[i].getCodeLength()) {
	                String s = "";
	                for (int j = 0; j < huffCodeArray[i].getCodeLength() - huffCodeArray[i].getHuffCode().length(); j++)
	                    s += "0";  // Pad with zeros
	                s += huffCodeArray[i].getHuffCode();
	                huffCodeArray[i].setHuffCode(s);
	            }
	            bufferTracker++;  // Move to the next byte
	            if (bufferTracker == 1024) {
	                size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                bufferTracker = 0;
	            }
	        }

	        // Build a binary tree for Huffman codes
	        BinaryTree tree = new BinaryTree();
	        for (int i = 0; i < huffCodeArraySize; i++) {
	            tree = BinaryTree.addToBinarryTree(tree, huffCodeArray[i].getHuffCode(), 0,
	                    huffCodeArray[i].getCharacter());
	        }

	        // Initialize tmp array and reset bufferTracker
	        for (int i = 0; i < tmp.length; i++)
	            tmp[i] = '\0';  // Clear temporary array

	        if (bufferTracker == 1024) {
	            size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	            bufferTracker = 0;
	        }

	        // Check if the output file exists and rename it if needed
	        int index = bufferTracker;
	        bufferTracker = 0;
	        byte[] outputBuffer = new byte[1024];  // Buffer for the decompressed data
	        bufferIndex = 0;  // Reset buffer index
	        String name = "";
	        File n = new File(name);

	        // Rename the file if it already exists by appending a counter
	        n = new File(originalFileName);
	        int counter = 1;  // Start counter for renaming
	        while (n.exists()) {
	            String baseName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
	            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
	            originalFileName = baseName + counter + extension;  // New file name with counter
	            n = new File(originalFileName);  // Create the new file object
	            counter++;  // Increment counter
	        }

	        // Write the decompressed data to the new file
	        FileOutputStream output = new FileOutputStream(originalFileName);
	        BinaryTree root = tree;
	        long count = 0;
	        flag = false;
	        do {
	            // Traverse the binary tree using Huffman code and write data to output
	            while (tree.getLeft() != null || tree.getRight() != null) {
	                if ((buffer[index] & (1 << 7 - bufferTracker)) == 0)
	                    tree = tree.getLeft();
	                else
	                    tree = tree.getRight();
	                bufferTracker++;  // Move to the next bit
	                if (bufferTracker == 8) {  // Move to the next byte after 8 bits
	                    bufferTracker = 0;
	                    index++;
	                    if (index == 1024) {
	                        size = scan.read(buffer, 0, 1024);  // Read next 1024 bytes
	                        index = 0;
	                        if (size == -1)
	                            flag = true;  // End if no more data
	                    }
	                }
	            }
	            if (flag)
	                break;
	            outputBuffer[bufferIndex++] = (byte) tree.getCharacter();  // Add the decoded character to the output buffer
	            if (bufferIndex == 1024) {  // If buffer is full, write to output
	                output.write(outputBuffer);
	                bufferIndex = 0;
	            }
	            count++;  // Increment the count of processed characters
	            tree = root;  // Reset tree to root
	            if (count == numberOfChar)  // If all characters are decoded, exit loop
	                break;
	        } while (size != -1);
	        output.write(outputBuffer, 0, bufferIndex);  // Write any remaining data in the buffer
	        output.close();  // Close the output file

	    } catch (Exception e) {
	        System.out.println(e.getMessage());  // Print any exceptions encountered
	    }
	}



	public void getCodes(Node t, String st) {
		if (t.left == t.right && t.right == null)// leafe node
			for (int i = 0; i < huffCodeArraySize; i++) {
				if (huffCodeArray[i].getCharacter() == t.ch) {
					huffCodeArray[i].setHuffCode(st);
					huffCodeArray[i].setCodeLength(st.length());
				}
			}
		else {
			getCodes(t.left, st + '0');
			getCodes(t.right, st + '1');
		}
	}
}