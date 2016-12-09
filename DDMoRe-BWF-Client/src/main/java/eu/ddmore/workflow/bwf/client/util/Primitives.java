package eu.ddmore.workflow.bwf.client.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Primitives {
	
	private Primitives() {
		super();
	}
	
	public static String getTempDirectory() {
		return System.getProperty("java.io.tmpdir");
	}

	public static InputStream getInputStreamForFilenameOnClasspath(String filename) throws IOException {
		return Primitives.class.getClassLoader().getResourceAsStream(filename);
	}

	/**
	 * Generate a CRON expression is a string comprising 5 or 6 fields separated by white space.
	 * @param minutes mandatory = yes. allowed values = {@code  0-59    * / , -}
	 * @param hours mandatory = yes. allowed values = {@code 0-23   * / , -}
	 * @param dayOfMonth mandatory = yes. allowed values = {@code 1-31  * / , - ? L W}
	 * @param month mandatory = yes. allowed values = {@code 1-12 or JAN-DEC    * / , -}
	 * @param dayOfWeek mandatory = yes. allowed values = {@code 0-6 or SUN-SAT * / , - ? L #}
	 * @param year mandatory = no. allowed values = {@code 1970–2099    * / , -}
	 * @return a CRON Formatted String.
	 */
	public static String generateCronExpression(final String minutes, final String hours, final String dayOfMonth, final String month, final String dayOfWeek, final String year) {
	    return String.format("%1$s %2$s %3$s %4$s %5$s %6$s", minutes, hours, dayOfMonth, month, dayOfWeek, year);
	}
	
	public static int getRandomInt(int low, int high) {
		Random r = new Random();
		return r.nextInt(high - low) + low;
	}
	
	public static boolean getRandomBoolean() {
		Random r = new Random();
		return r.nextBoolean();
	}

	public static String makeValidFilename(String filename) {
		return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException {
		FileInputStream fileInputStream = null;
        try {
        	byte[] bFile = new byte[(int) file.length()];
        	fileInputStream = new FileInputStream(file);
        	fileInputStream.read(bFile);
        	return bFile;
        } finally {
        	try {
                if (fileInputStream != null) {
                	fileInputStream.close();
                }
            } catch (final IOException ioe) {
                // ignore
            }
        }
	}

	// ------------------------------------------------------------- Assertions
	
	public static boolean assertNull(Object... values) {
		if (values != null) {
			for (Object value : values) {
				if (value == null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static boolean assertString(String value) {
		return value != null && value.trim().length() > 0;
	}
	
	public static boolean assertString(String value, String toCompare) {
		return assertString(value, toCompare, false);
	}

	public static boolean assertStringIgnoreCase(String value, String toCompare) {
		return assertString(value, toCompare, true);
	}

	public static boolean assertString(String value, String toCompare, boolean ignoreCase) {
		if (value != null && toCompare == null) {
			return false;
		} else if (value == null && toCompare != null) {
			return false;
		} else if (value == null && toCompare == null) {
			return true;
		} else {
			if (ignoreCase) {
				return value.equalsIgnoreCase(toCompare);
			} else {
				return value.equals(toCompare);
			}
		}
	}

	public static boolean assertList(List<?> list) {
		return list != null && !list.isEmpty();
	}

	public static boolean assertList(List<?> list, int size) {
		return list != null && list.size() == size;
	}

	public static boolean assertSet(Set<?> set) {
		return set != null && !set.isEmpty();
	}

	public static boolean assertSet(Set<?> set, int size) {
		return set != null && set.size() == size;
	}

	public static boolean assertMap(Map<?, ?> map) {
		return map != null && !map.isEmpty();
	}

	public static boolean assertMap(Map<?, ?> map, int size) {
		return map != null && map.size() == size;
	}

	public static boolean assertArray(Object[] array1) {
		return array1 != null && array1.length > 0;
	}
	
	public static boolean assertArrays(Object[] array1, Object[] array2) {
		if (array1 != null && array2 != null && array1.length == array2.length) {
			for (int i = 0; i < array1.length; i++) {
				if (array1[i] == null || array2[i] == null || !array1[i].equals(array2[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean isEmpty(String value) {
		return !assertString(value);
	}

	public static boolean isNotEmpty(String value) {
		return assertString(value);
	}

	public static boolean isEmpty(boolean all, String... values) {
		if (values == null) {
			return true;
		}
		int emptyCount = 0;
		for (String value : values) {
			if (isEmpty(value)) {
				emptyCount++;
				if (!all) {
					return true;
				}
			} else if (isNotEmpty(value) && all) {
				return false;
			}
		}
		return emptyCount > 0;
	}

	public static boolean isNotEmpty(boolean all, String... values) {
		if (values == null) {
			return false;
		}
		int notEmptyCount = 0;
		for (String value : values) {
			if (isNotEmpty(value)) {
				notEmptyCount++;
				if (!all) {
					return true;
				}
			} else if (isEmpty(value) && all) {
				return false;
			}
		}
		return notEmptyCount > 0;
	}
	
	public static boolean isEmpty(List<?> list) {
		return !assertList(list);
	}

	public static boolean isNotEmpty(List<?> list) {
		return assertList(list);
	}

	public static boolean isEmpty(boolean all, List<?>... lists) {
		if (lists == null) {
			return true;
		}
		int emptyCount = 0;
		for (List<?> list : lists) {
			if (isEmpty(list)) {
				emptyCount++;
				if (!all) {
					return true;
				}
			} else if (isNotEmpty(list) && all) {
				return false;
			}
		}
		return emptyCount > 0;
	}

	public static boolean isNotEmpty(boolean all, List<?>... lists) {
		if (lists == null) {
			return false;
		}
		int notEmptyCount = 0;
		for (List<?> list : lists) {
			if (isNotEmpty(list)) {
				notEmptyCount++;
				if (!all) {
					return true;
				}
			} else if (isEmpty(list) && all) {
				return false;
			}
		}
		return notEmptyCount > 0;
	}
	
	public static boolean isEmpty(Set<?> set) {
		return !assertSet(set);
	}

	public static boolean isNotEmpty(Set<?> set) {
		return assertSet(set);
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return !assertMap(map);
	}

	public static boolean isNotEmpty(Map<?, ?> map) {
		return assertMap(map);
	}

	public static boolean isEmpty(Object[] array) {
		return !assertArray(array);
	}

	public static boolean isNotEmpty(Object[] array) {
		return assertArray(array);
	}
}
