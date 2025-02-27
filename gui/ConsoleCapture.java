package gui;

public class ConsoleCapture {
    private static final StringBuilder buffer = new StringBuilder();
    
    public static String getOutput() {
        String output = buffer.toString();
        buffer.setLength(0);
        return output;
    }
    
    public static void captureOutput(Runnable action) {
        buffer.setLength(0);
        
        // Rediriger System.out vers notre buffer
        java.io.PrintStream originalOut = System.out;
        try {
            System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
                @Override
                public void write(int b) {
                    buffer.append((char) b);
                }
            }));
            
            // Exécuter l'action
            action.run();
            
        } finally {
            // Restaurer System.out
            System.setOut(originalOut);
        }
    }
}