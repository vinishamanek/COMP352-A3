import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Demo {

    public static void main(String[] args) {
        // Replace "input.txt" with the actual path to your input file
        String inputFile = "/Users/vinishamanek/Downloads/NASTA_test_files/NASTA_test_file1.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            CleverSIDC cleverSIDC = new CleverSIDC();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String operation = tokens[0];

                switch (operation) {
                    case "generate":
                        cleverSIDC.generate();
                        break;

                    case "allKeys":
                        long[] keys = cleverSIDC.allKeys(cleverSIDC);
                        System.out.println("All Keys: " + arrayToString(keys));
                        break;

                    case "add":
                        long key = Long.parseLong(tokens[1]);
                        String value = tokens[2];
                        cleverSIDC.add(cleverSIDC, key, value);
                        break;

                    // Add cases for other operations like remove, getValues, nextKey, prevKey, rangeKey, etc.

                    default:
                        System.out.println("Invalid operation: " + operation);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String arrayToString(long[] array) {
        StringBuilder result = new StringBuilder("[");
        for (long value : array) {
            result.append(value).append(", ");
        }
        if (array.length > 0) {
            result.setLength(result.length() - 2); // Remove the trailing comma and space
        }
        result.append("]");
        return result.toString();
    }
}
