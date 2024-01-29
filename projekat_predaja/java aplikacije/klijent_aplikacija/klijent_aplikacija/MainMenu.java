package klijent_aplikacija;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainMenu extends JFrame {

    JTextArea output = new JTextArea();
    JScrollPane sp = new JScrollPane(output);
    Font defaultFont = new Font("Arial", Font.PLAIN, 30);
    Font buttonFont = new Font("Arial", Font.PLAIN, 30);

    public void setOutputText(String text) {
        output.setText(text);
    }

    public MainMenu() {
        setSize(1800, 1200);
        setTitle("Klijentska aplikacija IS1");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        output.setFont(defaultFont);
        populate();
    }

    private void populate() {
        setLayout(new BorderLayout());
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(5, 5));

        for (int i = 1; i <= 25; i++) {
            final int number = i;
            JButton button = new JButton(getButtonActionName(number, true));
            button.setFont(buttonFont);
            button.addActionListener(ae -> {
                openFormWindow(number);
            });
            buttonsPanel.add(button);
        }
        add(buttonsPanel, BorderLayout.CENTER);
        sp.setPreferredSize(new Dimension((int) getContentPane().getWidth(), 700));
        add(sp, BorderLayout.SOUTH);
    }

    private void openFormWindow(int buttonNumber) {
        FormWindow formWindow = new FormWindow(buttonNumber, this);

        // Provera za dugme bez parametara forme
        if (shouldAutoSubmit(buttonNumber)) {
            formWindow.clickSubmitButton();
        } else {
            formWindow.setVisible(true);
        }

    }

    private boolean shouldAutoSubmit(int buttonNumber) {
        int[] skippable = {17, 18, 19, 20, 22};
        int index = Arrays.binarySearch(skippable, buttonNumber);
        return index >= 0;
    }

    public static String getButtonActionName(int buttonNumber, boolean html) {
        String result;
        switch (buttonNumber) {
            case 1:
                result = "Kreiraj grad";
                break;
            case 2:
                result = "Kreiraj korisnika";
                break;
            case 3:
                result = "Promena email-a<br>korisnika";
                break;
            case 4:
                result = "Promena mesta<br>korisnika";
                break;
            case 5:
                result = "Kreiraj kategoriju";
                break;
            case 6:
                result = "Kreiraj video snimak";
                break;
            case 7:
                result = "Promena naziva<br>video snimka";
                break;
            case 8:
                result = "Dodaj kategoriju<br>video snimku";
                break;
            case 9:
                result = "Kreiraj paket";
                break;
            case 10:
                result = "Promena cene paketa";
                break;
            case 11:
                result = "Kreiraj pretplatu<br>korisnika na paket";
                break;
            case 12:
                result = "Kreiraj gledanje<br>video snimka";
                break;
            case 13:
                result = "Kreiraj ocenu";
                break;
            case 14:
                result = "Promeni ocenu";
                break;
            case 15:
                result = "Obrisi ocenu";
                break;
            case 16:
                result = "Obrisi video";
                break;
            case 17:
                result = "Dohvati sva mesta";
                break;
            case 18:
                result = "Dohvati sve korisnike";
                break;
            case 19:
                result = "Dohvati sve kategorije";
                break;
            case 20:
                result = "Dohvati sve<br>video snimke";
                break;
            case 21:
                result = "Dohvati kategorije<br>za video snimak";
                break;
            case 22:
                result = "Dohvati sve pakete";
                break;
            case 23:
                result = "Dohvati sve pretplate<br>korisnika";
                break;
            case 24:
                result = "Dohvati sva gledanja<br>za snimak";
                break;
            case 25:
                result = "Dohvati sve ocene<br>za snimak";
                break;
            default:
                result = "Number out of range (1 to 25)";
                break;
        }
        if (html) {
            return "<html>" + result + "</html>";
        }
        result = result.replaceAll("<br>", " ");
        return result;

    }
}
